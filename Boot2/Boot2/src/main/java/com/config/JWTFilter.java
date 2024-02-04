
package com.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.security.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
private final com.services.PeopleService peopleService;

    public JWTFilter(JWTUtil jwtUtil, com.services.PeopleService peopleService) {
        this.jwtUtil = jwtUtil;
        this.peopleService = peopleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Извлечение заголовка Authorization из запроса
        String authHeader = httpServletRequest.getHeader("Authorization");
        // Проверка наличия и формата заголовка
        if (authHeader!=null && authHeader.isBlank() && authHeader.startsWith("Bearer ")){
            // Извлечение токена из заголовка
            String jwt = authHeader.substring(7);

            // Проверка наличия токена
            if (jwt.isBlank()){
                // В случае отсутствия токена отправляем ошибку
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT token in Bearer Header");
            }else {
                try {
                    // Верификация токена и получение имени пользователя
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    // Загрузка пользователя по имени из сервиса
                    UserDetails userDetails = peopleService.loadUserByUsername(username);

                    // Создание аутентификационного токена
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken( userDetails, userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    // Установка аутентификации в контексте безопасности, если она еще не установлена
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException exc ){
                    // В случае ошибки верификации отправляем ошибку
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                }
            }
        }
        // Продолжаем передачу запроса по цепочке фильтров
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
