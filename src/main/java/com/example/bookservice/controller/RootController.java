package com.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * La raiz del servicio no tiene contenido propio: redirige a Swagger UI para que
 * quien abra la URL base de la demo aterrice directamente en la documentacion de la API.
 */
@Hidden
@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:/swagger-ui.html";
    }
}
