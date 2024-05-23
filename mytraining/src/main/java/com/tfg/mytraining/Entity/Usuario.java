/*
 * Entidad Usuario
 * Autor: Julio Navarro Vazquez
 * Descripción: Esta entidad representa a un usuario en el sistema.
 *              Contiene información como correo electrónico, nombre, contraseña y rol.
 */
package com.tfg.mytraining.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Setter
@EqualsAndHashCode
@Getter
@Entity

/*
   Clase que representa un usuario en el sistema.
*/
public class Usuario {

	@Id
	private String email;
	
	private String nombre;
	
	private String password;
	
	private String rol;
 
	
	/* Constructor para inicializar un objeto Usuario con los atributos proporcionados. */
	public Usuario(String email, String nombre, String password, String rol) {
		this.email = email;
		this.nombre = nombre;
		this.password = password;
		this.rol = rol;
	}

	/* Constructor vacío requerido por JPA. */
	public Usuario() {
		super();
	}
}
