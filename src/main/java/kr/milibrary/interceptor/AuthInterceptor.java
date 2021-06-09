package kr.milibrary.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.milibrary.annotation.Auth;
import kr.milibrary.annotation.AuthIgnore;
import kr.milibrary.exception.ForbiddenException;
import kr.milibrary.exception.UnauthorizedException;
import kr.milibrary.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Optional;

// JWT 인증을 처리하는 인터셉터
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 컨트롤러에서 호출한 메소드를 실행하기 전에 실행된다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;

        Optional<String> jwtOptional = Optional.ofNullable(request.getHeader("Authorization"))
                .filter(t -> t.startsWith("Bearer "))
                .map(t -> t.substring(7));

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 해당 메소드를 호출한 컨트롤러 클래스를 검사한다.
        Class<?> declaringClass = handlerMethod.getMethod().getDeclaringClass();
        Auth classAuth = declaringClass.getAnnotation(Auth.class);

        if (classAuth != null) {
            // 메소드에 AuthIgnore이 존재하면 검사하지 않는다.
            AuthIgnore methodAuthIgnore = handlerMethod.getMethodAnnotation(AuthIgnore.class);
            if (methodAuthIgnore != null)
                return true;

            if (!jwtOptional.isPresent())
                throw new UnauthorizedException("올바르지 않은 토큰입니다.");

            DecodedJWT decodedJWT = validateAuth(classAuth.role(), jwtOptional.get());
            setSessionAttribute(request.getSession(), "narasarangId", decodedJWT.getAudience().get(0));

            return true;
        }

        // 해당 메소드를 검사한다.
        Method method = handlerMethod.getMethod();
        Auth methodAuth = method.getAnnotation(Auth.class);

        if (methodAuth != null) {
            if (!jwtOptional.isPresent())
                throw new UnauthorizedException("올바르지 않은 토큰입니다.");

            DecodedJWT decodedJWT = validateAuth(methodAuth.role(), jwtOptional.get());
            setSessionAttribute(request.getSession(), "narasarangId", decodedJWT.getAudience().get(0));

            return true;
        }

        return true;
    }

    // Not-Null
    private DecodedJWT validateAuth(Auth.Role role, String jwt) {
        // 필요 권한이 ADMIN이라면
        if (role == Auth.Role.ADMIN) {
            try {
                if (!jwtUtil.forAdmin(jwt))
                    throw new ForbiddenException("해당 서비스는 관리자만 이용 가능합니다.");
            } catch (JWTDecodeException jwtDecodeException) {
                throw new UnauthorizedException("만료되었거나 형식에 맞지 않는 토큰입니다.");
            }
        } else {
            if (!jwtUtil.isValid(jwt, JwtUtil.JwtType.ACCESS_TOKEN))
                throw new UnauthorizedException("만료되었거나 형식에 맞지 않는 토큰입니다.");
        }

        return jwtUtil.getDecodedJWT(jwt);
    }

    private void setSessionAttribute(HttpSession httpSession, String name, Object value) {
        httpSession.setAttribute(name, value);
    }
}
