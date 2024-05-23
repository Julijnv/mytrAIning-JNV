/*
 * Repositorio para la entidad Usuario
 * Autor: Julio Navarro Vazquez
 * Versión: 1.0
 * Descripción: Este repositorio proporciona métodos para acceder a los datos de los usuarios en la base de datos.
 */
package com.tfg.mytraining.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.mytraining.Entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{

	Optional<Usuario> findByEmail(String email);

	boolean existsByEmail(String email);

    Optional<Usuario> findByNombre(String nombre);

}
