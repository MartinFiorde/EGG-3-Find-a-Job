package FindAJob.controladores;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainControlador {   

    @GetMapping
    public String index() {
        return "index-test.html";
    }
    
  
}
