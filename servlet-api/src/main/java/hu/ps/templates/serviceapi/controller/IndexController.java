package hu.ps.templates.serviceapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller()
public class IndexController {

    @GetMapping("/")
    public String redirectToSwaggerUi() {
        return "redirect:/api-docs/swagger-ui";
    }
}
