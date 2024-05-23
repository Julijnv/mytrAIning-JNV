/*
 * Controlador para la gestión de usuarios
 * Autor: Julio Navarro Vazquez
 * Versión: 1.0
 * Descripción: Este controlador maneja las operaciones relacionadas con la gestión de usuarios,
 *              incluyendo el registro, login y logout. También utiliza un JWT para la autenticación
 *              y gestiona las cookies para almacenar el token JWT.
 */
package com.tfg.mytraining.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tfg.mytraining.Entity.Usuario;
import com.tfg.mytraining.Repository.UsuarioRepository;
import com.tfg.mytraining.Security.JWTUtil;

@RestController
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registra un nuevo usuario con contraseña cifrada y lo guarda en la base de datos.
    @PostMapping("/register")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario nuevoUsuario) {
        if (usuarioRepository.existsById(nuevoUsuario.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok(usuarioGuardado);
    }

    // Autentica al usuario, genera un token JWT y lo almacena en una cookie.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestMap, HttpServletResponse response) {
        try {
            String email = requestMap.get("email");
            String password = requestMap.get("password");
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
        
            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getPassword());
                Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                response.addCookie(jwtCookie);

                return ResponseEntity.ok().build();
            }
        } catch (Exception exception) {
            
        }
        return new ResponseEntity<String>(
            "{\"mensaje\":\"" + "Credenciales incorrectas" + "\"}",
            HttpStatus.BAD_REQUEST);
    }


    // Desconecta al usuario eliminando la cookie JWT y limpiando el contexto de seguridad.
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie jwtCookie = WebUtils.getCookie(request, "JWT_TOKEN");
            if (jwtCookie != null) {
                jwtCookie.setValue(null);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(0);
                jwtCookie.setSecure(true);
                response.addCookie(jwtCookie);
            }

            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/login").build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error durante el logout");
        }
    }
}
