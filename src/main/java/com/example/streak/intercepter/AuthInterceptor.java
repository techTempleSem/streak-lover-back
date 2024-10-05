package com.example.streak.intercepter;

import com.example.streak.user.db.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Transactional
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Auth Interceptor url : {}", request.getRequestURI());
        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        Long accessInfo = (Long)(request.getSession().getAttribute("USER"));

        if(accessInfo == null) {
            return false;
        }

        return true;
    }
}
