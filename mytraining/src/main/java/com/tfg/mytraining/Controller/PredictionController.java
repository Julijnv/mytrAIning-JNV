/*
 * Controlador de predicciones
 * Autor: Julio Navarro Vazquez
 * Versión: 1.0
 * Descripción: Este controlador gestiona las solicitudes relacionadas con la predicción de ejercicios.
 *              Recibe la entrada del usuario en forma de objeto UserInput, la convierte a formato JSON
 *              y envía una solicitud POST a un microservicio Python. El microservicio analiza los datos
 *              del formulario y devuelve una rutina personalizada con los pesos sugeridos según las especificaciones
 *              de la persona.
 */
package com.tfg.mytraining.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tfg.mytraining.Dto.UserInput;
import com.tfg.mytraining.Service.CustomUserDetailsService;

@RestController
public class PredictionController {

    @Autowired
    CustomUserDetailsService CustomUserDetailsService;

    /*  Método que convierte la entrada del usuario a JSON y envía una solicitud POST al microservicio Python,
     que analiza los datos del formulario y devuelve una rutina personalizada con los pesos sugeridos según las especificaciones de la persona. */
    @PostMapping("/javaprediction")
    public ResponseEntity<?> predictExerciseWeight(@RequestBody UserInput userInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson;

        try {
            userJson = objectMapper.writeValueAsString(userInput);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al convertir UserInput a JSON");
        }

        HttpEntity<String> request = new HttpEntity<>(userJson, headers);
        String pythonServiceUrl = "http://localhost:5000/predict";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(pythonServiceUrl, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
