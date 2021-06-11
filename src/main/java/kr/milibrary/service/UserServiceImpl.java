package kr.milibrary.service;

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
import java.time.LocalDateTime;
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
        if (narasarangId == null)
            throw new BadRequestException("나라사랑 아이디는 빈 값일 수 없습니다.");

        return Optional.ofNullable(userMapper.getUserByNarasarangId(narasarangId)).orElseThrow(() -> new NotFoundException("해당 나라사랑 아이디가 존재하지 않습니다."));
    }

    private Token getToken(String token) throws BadRequestException {
        if (token == null)
            throw new BadRequestException("인증 토큰은 빈 값일 수 없습니다.");

        return Optional.ofNullable(tokenMapper.getToken(token)).orElseThrow(() -> new BadRequestException("올바르지 않은 인증 토큰입니다."));
    }

    @Override
    public BaseResponse signIn(User user) throws UnauthorizedException {
        User dbUser = getUserByNarasarangId(user.getNarasarangId());
        if (BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
            if (!dbUser.getRegistered())
                throw new UnauthorizedException("본인인증이 아직 완료되지 않은 아이디입니다.");

            String hashParentKey = String.format("user:%s", dbUser.getNarasarangId());
            final HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

            // Redis에 저장된 Refresh Token이 없거나 기간이 만료됐다면 새로 발급한다.
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
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");

        return new BaseResponse(dbUser, HttpStatus.OK);
    }

    @Override
    public BaseResponse signOut(String narasarangId) {
        User dbUser = getUserByNarasarangId(narasarangId);

        String hashParentKey = String.format("user:%s", dbUser.getNarasarangId());
        final HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

        Optional<String> refreshTokenOptional = Optional.ofNullable((String) hashOperations.get(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType()));
        refreshTokenOptional.ifPresent(token -> hashOperations.delete(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType()));

        return new BaseResponse(HttpStatus.OK);
    }

    @Transactional
    @Override
    public BaseResponse signUp(User user) throws ConflictException, DataAccessException {
        try {
            // 처음 가입하는 유저라면 DB에 저장 후 메일 전송
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

            // 유저의 나라사랑 아이디와 가입한 시간은 토큰이 된다.
            LocalDateTime now = LocalDateTime.now();
            Token registerToken = new Token(user.getNarasarangId(), SHA256Util.getEncrypt(user.getNarasarangId(), now.toString()), Token.TokenType.REGISTRATION);

            String anonymousNickname = "익명_" + registerToken.getToken().substring(registerToken.getToken().length() - 10).toLowerCase();
            user.setNickname(anonymousNickname);

            userMapper.createUser(user);
            tokenMapper.createToken(registerToken);

            // 본인인증 메일 발송
            Mail mail = new Mail("MiliBrary 회원가입 확인", user.getNarasarangId() + "@narasarang.or.kr", "milibrary@gmail.com", new HashMap<String, Object>() {{
                put("token", registerToken.getToken());
                put("contextURL", contextURL);
            }});
            emailUtil.sendEmail(mail, registerToken.getTokenTypeEnum().getTemplateName());
        } catch (DuplicateKeyException e) {
            throw new ConflictException("이미 가입되어 있는 나라사랑 아이디입니다.");
        }

        return new BaseResponse(HttpStatus.NO_CONTENT);
    }

    @Override
    public BaseResponse refresh(Jwt jwt) {
        if (!jwtUtil.isValid(jwt.getRefreshToken().getToken(), JwtUtil.JwtType.REFRESH_TOKEN))
            throw new UnauthorizedException("만료되었거나 형식에 맞지 않는 Refresh Token입니다.");

        String narasarangId = jwtUtil.getDecodedJWT(jwt.getRefreshToken().getToken()).getAudience().get(0);
        User dbUser = getUserByNarasarangId(narasarangId);

        String hashParentKey = String.format("user:%s", dbUser.getNarasarangId());
        final HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

        // Redis에 저장된 Refresh Token이 존재하고 입력받은 것과 동일하다면
        String refreshToken = Optional.ofNullable((String) hashOperations.get(hashParentKey, JwtUtil.JwtType.REFRESH_TOKEN.getJwtType()))
                .filter(t -> t.equals(jwt.getRefreshToken().getToken()))
                .orElseThrow(() -> new UnauthorizedException("잘못된 경로로 접속하셨습니다. 다시 로그인해주세요."));
        String accessToken = jwtUtil.createAccessToken(dbUser.getNarasarangId(), false);

        return new BaseResponse(new Jwt(new Jwt.AccessToken(accessToken), null), HttpStatus.OK);
    }

    @Override
    public BaseResponse signUpResend(User user) throws ConflictException {
        User dbUser = getUserByNarasarangId(user.getNarasarangId());
        if (dbUser.getRegistered())
            throw new ConflictException("이미 가입되어 있는 나라사랑 아이디입니다. 비밀번호를 잊으셨다면 비밀번호 초기화를 해주세요.");

        LocalDateTime now = LocalDateTime.now();
        Token registerToken = new Token(user.getNarasarangId(), SHA256Util.getEncrypt(user.getNarasarangId(), now.toString()), Token.TokenType.RESEND_REGISTRATION);

        tokenMapper.createToken(registerToken);

        // 본인인증 메일 발송
        Mail mail = new Mail("MiliBrary 회원가입 확인", user.getNarasarangId() + "@narasarang.or.kr", "milibrary@gmail.com", new HashMap<String, Object>() {{
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
            if (dbUser == null)
                return false;

            if (dbUser.getRegistered())
                return false;

            // 현재 시간과 토큰 발송 시간 비교해서 만료되었는지 여부 확인
            isExpired = dbToken.isExpired(1);
            // 유저 가입 처리 승인
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
        if (!dbUser.getRegistered())
            throw new ConflictException("회원가입이 완료되지 않은 아이디입니다. 계속 인증 메일이 오지 않는다면 인증 메일 재전송을 해주세요.");

        LocalDateTime now = LocalDateTime.now();
        Token resetToken = new Token(user.getNarasarangId(), SHA256Util.getEncrypt(user.getNarasarangId(), now.toString()), Token.TokenType.FORGOT_PASSWORD);

        tokenMapper.createToken(resetToken);

        Mail mail = new Mail("MiliBrary 비밀번호 재설정", user.getNarasarangId() + "@narasarang.or.kr", "milibrary@gmail.com", new HashMap<String, Object>() {{
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
            if (dbUser == null)
                return variables;

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
        if (variables.isEmpty())
            return false;

        try {
            Token dbToken = getToken((String) variables.get("token"));

            User dbUser = userMapper.getUserByToken(dbToken.getToken());
            if (dbUser == null)
                return false;

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
