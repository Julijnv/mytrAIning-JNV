/*
 * Controlador de autenticación y navegación
 * Autor: Julio Navarro Vazquez
 * Versión: 1.0
 */
package com.tfg.mytraining.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Método que maneja la petición GET para la página de login.
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Método que maneja la petición GET para la página principal (home).
    @GetMapping("/home")
    public String menuPage() {
        return "index";
    }

    // Método que maneja la petición GET para la página del predictor.
    @GetMapping("/predictor")
    public String predictorPage() {
        return "predictor";
    }

    // Método que maneja la petición GET para la página de registro.
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}
