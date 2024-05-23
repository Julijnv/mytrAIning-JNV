/*
 * Clase principal de la aplicación MyTraining
 * Autor: Julio Navarro Vazquez
 * Descripción: Esta clase es la entrada principal de la aplicación MyTraining,
 *              que inicia la aplicación Spring Boot.
 */
package com.tfg.mytraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MytrainingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MytrainingApplication.class, args);
	}
}
