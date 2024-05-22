package com.example.demo.web.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Класс CustomAccessDeniedHandler реализует интерфейс AccessDeniedHandler и используется для обработки ситуаций, когда пользователь пытается получить доступ к защищенному ресурсу без необходимых прав доступа.
 * */

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException accessDeniedException)
            throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.warn("User: {} attempted to access the protected URL: {}", auth.getName(), req.getRequestURI());
        }
        resp.sendRedirect(req.getContextPath() + "/error/403");
    }
}
