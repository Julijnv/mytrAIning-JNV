/*
 * Utilidad para manejar tokens JWT
 * Autor: Julio Navarro Vazquez
 * Versión: 1.0
 * Descripción: Esta clase proporciona métodos para generar, validar y manipular tokens JWT
 *              utilizados para la autenticación y autorización en el sistema.
 */
package com.tfg.mytraining.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.tfg.mytraining.Service.CustomUserDetailsService;

@Service
public class JWTUtil {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Clave secreta para firmar y validar los tokens JWT
    private String secret = "mypassword";

    // Extrae el nombre de usuario del token JWT
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    // Extrae la fecha de expiración del token JWT
    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    // Extrae las reclamaciones del token JWT utilizando una función de resolución de reclamaciones
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae todas las reclamaciones del token JWT
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // Verifica si el token JWT ha expirado
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Genera un token JWT para el nombre de usuario y el rol proporcionados
    public String generateToken(String username, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    // Crea un token JWT con las reclamaciones y el sujeto proporcionados
    private String createToken(Map<String,Object> claims, String subject){
        return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10)) // 10 horas de duración
                    .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    // Valida el token JWT frente a los detalles del usuario
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Obtiene la autenticación a partir del token JWT
    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
