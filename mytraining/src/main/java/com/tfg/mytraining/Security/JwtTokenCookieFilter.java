/*
 * Filtro para gestionar el token JWT almacenado en una cookie
 * Autor: Julio Navarro Vazquez
 * Versión: 1.0
 * Descripción: Este filtro intercepta las peticiones entrantes y verifica si hay un token JWT
 *              almacenado en una cookie. Si el token es válido, se autentica al usuario.
 *              Además, gestiona las solicitudes de logout y redirige al usuario a la página de login.
 */
package com.tfg.mytraining.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenCookieFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    /*
     - Filtra las solicitudes entrantes para gestionar el token JWT almacenado en una cookie.
     - Verifica si el token es válido y autentica al usuario si corresponde.
     - Gestiona las solicitudes de logout y redirige al usuario a la página de login en caso necesario.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie jwtCookie = WebUtils.getCookie(request, "JWT_TOKEN");

        String requestURI = request.getRequestURI();
        String query = request.getQueryString();

        if (requestURI.equals("/login") && query != null && query.equals("logout")) {
            clearAuthenticationAndRedirect(response, jwtCookie);
            return;
        }
        if (jwtCookie != null) {
            String token = jwtCookie.getValue();
            try {
                if (token != null && !jwtUtil.isTokenExpired(token)) {
                    Authentication authentication = jwtUtil.getAuthentication(token);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    throw new ServletException("Token expirado");
                }
            } catch (Exception e) {
                clearAuthenticationAndRedirect(response, jwtCookie);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /*
     - Limpia la autenticación y redirige al usuario a la página de login.
     - Elimina la cookie JWT del navegador, limpia el contexto de seguridad y redirige al usuario a la página de login.
     */
    private void clearAuthenticationAndRedirect(HttpServletResponse response, Cookie jwtCookie) throws IOException {
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        SecurityContextHolder.clearContext();

        response.sendRedirect("/login");
    }
}

