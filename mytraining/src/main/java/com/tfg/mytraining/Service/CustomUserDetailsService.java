/*
 * Servicio personalizado para cargar detalles de usuario
 * Autor: Julio Navarro Vazquez
 * Versión: 1.0
 * Descripción: Esta clase proporciona un servicio personalizado para cargar los detalles
 *              de un usuario a partir de su correo electrónico, utilizado para la autenticación
 *              basada en Spring Security.
 */
package com.tfg.mytraining.Service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

import com.tfg.mytraining.Entity.Usuario;
import com.tfg.mytraining.Repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    /* Repositorio de usuarios para acceder a la base de datos */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /* Usuario actual obtenido durante la autenticación */
    private Usuario user;

    /* Método para cargar los detalles del usuario a partir del correo electrónico */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRol()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    /* Método para obtener los detalles del usuario */
    public Usuario getUserDetail(){
        return user;
    }
}
