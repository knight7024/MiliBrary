package kr.milibrary.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import kr.milibrary.domain.*;
import kr.milibrary.exception.BadRequestException;
import kr.milibrary.exception.ConflictException;
import kr.milibrary.exception.NotFoundException;
import kr.milibrary.exception.UnauthorizedException;
import kr.milibrary.mapper.TokenMapper;
import kr.milibrary.mapper.UserMapper;
import kr.milibrary.util.EmailUtil;
import kr.milibrary.util.JwtUtil;
import kr.milibrary.util.SHA256Util;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Value("${env.prod.contextURL}")
    private String contextURL;
    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final EmailUtil emailUtil;
    private final JwtUtil jwtUtil;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, TokenMapper tokenMapper, EmailUtil emailUtil, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.tokenMapper = tokenMapper;
        this.emailUtil = emailUtil;
        this.jwtUtil = jwtUtil;
    }

    private User getUserByNarasarangId(String narasarangId) throws BadRequestException, NotFoundException {
        if (narasarangId == null) {
            throw new BadRequestException("???????????? ???????????? ??? ?????? ??? ????????????.");
        }

        return Optional.ofNullable(userMapper.getUserByNarasarangId(narasarangId)).orElseThrow(() -> new NotFoundException("?????? ???????????? ???????????? ???????????? ????????????."));
    }

    private Token getToken(String token) throws BadRequestException {
        if (token == null) {
            throw new BadRequestException("?????? ????????? ??? ?????? ??? ????????????.");
        }

        return Optional.ofNullable(tokenMapper.getToken(token)).orElseThrow(() -> new BadRequestException("???????????? ?????? ?????? ???????????????."));
    }

    @Override
    public BaseResponse signIn(User user) throws UnauthorizedException {
        User dbUser = getUserByNarasarangId(user.getNarasarangId());

        if (BCrypt.checkpw(user.getPassword().toLowerCase(), dbUser.getPassword())) {
            if (!dbUser.getRegistered()) {
                throw new UnauthorizedException("??????????????? ?????? ???????????? ?????? ??????????????????.");
            }
            String hashParentKey = String.format("user:%s", dbUser.getNarasarangId());
            final HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

            // Redis??? ????????? Refresh Token??? ????????? ????????? ??????????????? ?????? ????????????.
            Optional<String> refreshTokenOptional = Optional.ofNullable((String) hashOperations.get(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType()))
                    .filter(token -> !jwtUtil.isExpired(token));
            String refreshToken = refreshTokenOptional.orElseGet(() -> {
                String token = jwtUtil.createRefreshToken(dbUser.getNarasarangId());
                hashOperations.put(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType(), token);
                return token;
            });
            String accessToken = jwtUtil.createAccessToken(dbUser.getNarasarangId(), false);

            dbUser.setJwt(new Jwt(new Jwt.AccessToken(accessToken), new Jwt.RefreshToken(refreshToken)));
        } else
            throw new UnauthorizedException("??????????????? ???????????? ????????????.");

        return new BaseResponse(dbUser, HttpStatus.OK);
    }

    @Override
    public BaseResponse signOut(String narasarangId) {
        User dbUser = getUserByNarasarangId(narasarangId);

        String hashParentKey = String.format("user:%s", dbUser.getNarasarangId());
        final HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

        Optional<String> refreshTokenOptional = Optional.ofNullable((String) hashOperations.get(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType()));
        refreshTokenOptional.ifPresent(token -> hashOperations.delete(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType()));

        return new BaseResponse(HttpStatus.NO_CONTENT);
    }

    @Transactional
    @Override
    public BaseResponse signUp(User user) throws ConflictException, DataAccessException {
        try {
            // ?????? ???????????? ???????????? DB??? ?????? ??? ?????? ??????
            user.setPassword(BCrypt.hashpw(user.getPassword().toLowerCase(), BCrypt.gensalt()));

            // ????????? ???????????? ???????????? ????????? ????????? ????????? ??????.
            LocalDateTime now = LocalDateTime.now();
            Token registerToken = new Token(user.getNarasarangId(), SHA256Util.getEncrypt(user.getNarasarangId(), now.toString()), Token.TokenType.REGISTRATION);

            String anonymousNickname = "??????_" + registerToken.getToken().substring(registerToken.getToken().length() - 10).toLowerCase();
            user.setNickname(anonymousNickname);

            userMapper.createUser(user);
            tokenMapper.createToken(registerToken);

            // ???????????? ?????? ??????
            Mail mail = new Mail("MiliBrary ???????????? ??????", user.getNarasarangId() + "@narasarang.or.kr", "milibrary@gmail.com", new HashMap<String, Object>() {{
                put("token", registerToken.getToken());
                put("contextURL", contextURL);
            }});
            emailUtil.sendEmail(mail, registerToken.getTokenTypeEnum().getTemplateName());
        } catch (DuplicateKeyException e) {
            throw new ConflictException("?????? ???????????? ?????? ???????????? ??????????????????.");
        }

        return new BaseResponse(HttpStatus.NO_CONTENT);
    }

    @Override
    public BaseResponse refresh(Jwt.RefreshToken refreshToken) {
        if (!jwtUtil.isValid(refreshToken.getToken(), JwtUtil.JwtType.REFRESH_TOKEN)) {
            throw new UnauthorizedException("?????????????????? ????????? ?????? ?????? Refresh Token?????????.");
        }

        DecodedJWT decodedRefreshToken = jwtUtil.getDecodedJWT(refreshToken.getToken());

        String narasarangId = decodedRefreshToken.getAudience().get(0);
        User dbUser = getUserByNarasarangId(narasarangId);

        String hashParentKey = String.format("user:%s", dbUser.getNarasarangId());
        final HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

        // Redis??? ????????? Refresh Token??? ???????????? ???????????? ?????? ???????????????
        String dbRefreshToken = Optional.ofNullable((String) hashOperations.get(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType()))
                .filter(t -> t.equals(refreshToken.getToken()))
                .orElseThrow(() -> new UnauthorizedException("????????? ????????? ?????????????????????. ?????? ?????????????????????."));
        String accessToken = jwtUtil.createAccessToken(dbUser.getNarasarangId(), false);

        // ?????? Refresh Token??? ????????? 7??? ???????????? ?????? ???????????? Redis??? ????????????.
        boolean needRefresh = false;
        LocalDate exiresAt = LocalDate.from(decodedRefreshToken.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if (LocalDate.now().plusDays(7).isAfter(exiresAt)) {
            needRefresh = true;
            dbRefreshToken = jwtUtil.createRefreshToken(narasarangId);
            hashOperations.put(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType(), dbRefreshToken);
        }

        return new BaseResponse(new Jwt(new Jwt.AccessToken(accessToken), needRefresh ? new Jwt.RefreshToken(dbRefreshToken) : null), HttpStatus.OK);
    }

    @Override
    public BaseResponse signUpResend(User user) throws ConflictException {
        User dbUser = getUserByNarasarangId(user.getNarasarangId());
        if (dbUser.getRegistered()) {
            throw new ConflictException("?????? ???????????? ?????? ???????????? ??????????????????. ??????????????? ??????????????? ???????????? ???????????? ????????????.");
        }

        LocalDateTime now = LocalDateTime.now();
        Token registerToken = new Token(user.getNarasarangId(), SHA256Util.getEncrypt(user.getNarasarangId(), now.toString()), Token.TokenType.RESEND_REGISTRATION);

        tokenMapper.createToken(registerToken);

        // ???????????? ?????? ??????
        Mail mail = new Mail("MiliBrary ???????????? ??????", user.getNarasarangId() + "@narasarang.or.kr", "milibrary@gmail.com", new HashMap<String, Object>() {{
            put("token", registerToken.getToken());
            put("contextURL", contextURL);
        }});
        emailUtil.sendEmail(mail, registerToken.getTokenTypeEnum().getTemplateName());

        return new BaseResponse(HttpStatus.NO_CONTENT);
    }

    @Override
    public boolean auth(String token) {
        boolean isExpired;
        try {
            Token dbToken = getToken(token);

            User dbUser = userMapper.getUserByToken(token);
            if (dbUser == null) {
                return false;
            }

            if (dbUser.getRegistered()) {
                return false;
            }

            // ?????? ????????? ?????? ?????? ?????? ???????????? ?????????????????? ?????? ??????
            isExpired = dbToken.isExpired(1);
            // ?????? ?????? ?????? ??????
            if (!isExpired) {
                userMapper.updateUserRegistration(dbUser.getNarasarangId(), true, new Timestamp(System.currentTimeMillis()));

                dbToken.setUsed(true);
                tokenMapper.updateToken(dbToken);
            }
        } catch (DataAccessException e) {
            return false;
        }

        return !isExpired;
    }

    @Transactional
    @Override
    public BaseResponse forgotPassword(User user) throws ConflictException {
        User dbUser = getUserByNarasarangId(user.getNarasarangId());
        if (!dbUser.getRegistered()) {
            throw new ConflictException("??????????????? ???????????? ?????? ??????????????????. ?????? ?????? ????????? ?????? ???????????? ?????? ?????? ???????????? ????????????.");
        }

        LocalDateTime now = LocalDateTime.now();
        Token resetToken = new Token(user.getNarasarangId(), SHA256Util.getEncrypt(user.getNarasarangId(), now.toString()), Token.TokenType.FORGOT_PASSWORD);

        tokenMapper.createToken(resetToken);

        Mail mail = new Mail("MiliBrary ???????????? ?????????", user.getNarasarangId() + "@narasarang.or.kr", "milibrary@gmail.com", new HashMap<String, Object>() {{
            put("token", resetToken.getToken());
            put("contextURL", contextURL);
        }});
        emailUtil.sendEmail(mail, resetToken.getTokenTypeEnum().getTemplateName());

        return new BaseResponse(HttpStatus.NO_CONTENT);
    }

    @Override
    public Map<String, Object> resetPasswordAuth(String token) {
        Map<String, Object> variables = new HashMap<String, Object>() {{
            put("contextURL", contextURL);
            put("success", false);
        }};
        Token dbToken;
        try {
            dbToken = getToken(token);

            User dbUser = userMapper.getUserByToken(token);
            if (dbUser == null) {
                return variables;
            }

            variables.put("token", dbToken.getToken());
        } catch (DataAccessException e) {
            return variables;
        }

        variables.put("isExpired", dbToken.isExpired(1));
        variables.replace("success", true);

        return variables;
    }

    @Transactional
    @Override
    public boolean resetPassword(Map<String, Object> variables) {
        if (variables.isEmpty()) {
            return false;
        }

        try {
            Token dbToken = getToken((String) variables.get("token"));

            User dbUser = userMapper.getUserByToken(dbToken.getToken());
            if (dbUser == null) {
                return false;
            }

            dbUser.setPassword(BCrypt.hashpw((String) variables.get("password"), BCrypt.gensalt()));
            userMapper.updateUser(dbUser);

            dbToken.setUsed(true);
            tokenMapper.updateToken(dbToken);
        } catch (DataAccessException e) {
            return false;
        }

        return true;
    }
}
