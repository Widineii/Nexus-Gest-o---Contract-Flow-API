package com.nexus.contractflow.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Redireciona a raiz do servidor para o Swagger UI (não há página HTML própria neste backend).
 */
@Controller
@Hidden
public class HomeController {

    @GetMapping("/")
    public RedirectView root() {
        return new RedirectView("/swagger-ui.html");
    }
}
