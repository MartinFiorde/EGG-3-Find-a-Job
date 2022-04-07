package FindAJob.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@PreAuthorize("permitAll()")
public class MainControlador {

    @GetMapping
    // doc con todas las opciones de autorizacion >>> https://www.baeldung.com/spring-security-expressions
    @PreAuthorize("permitAll()")
    public String index() {
        return "index.html";
    }
    
  
}
