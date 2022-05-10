package FindAJob.controladores;

import FindAJob.entidades.Profesion;
import FindAJob.entidades.Referencia;
import FindAJob.enums.Rubro;
import FindAJob.servicios.ProfesionServicio;
import FindAJob.servicios.ReferenciaServicio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@PreAuthorize("permitAll()")
public class MainControlador {

    private final ProfesionServicio profesionServicio;
    private final ReferenciaServicio referenciaServicio;

    public MainControlador(ProfesionServicio profesionServicio, ReferenciaServicio referenciaServicio) {
        this.profesionServicio = profesionServicio;
        this.referenciaServicio = referenciaServicio;
    }

    @GetMapping("testindex")
    // doc con todas las opciones de autorizacion >>> https://www.baeldung.com/spring-security-expressions
    @PreAuthorize("permitAll()")
    public String testIndex() {
        return "testMAFBEnd/index-test.html";
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public String index() {
        return "index.html";
    }
    
    //rubros index
    @PreAuthorize("permitAll()")
    @GetMapping("/programacion")
    public String programacion() {
        return "/rubros/programacion.html";
    }
     @PreAuthorize("permitAll()")
    @GetMapping("/diseñoMultimedia")
    public String diseñoMultimedia() {
        return "/rubros/diseñoMultimedia.html";
    }
     @PreAuthorize("permitAll()")
    @GetMapping("/gremios")
    public String gremios() {
        return "/rubros/gremiosDomicilio.html";
    }
     @PreAuthorize("permitAll()")
    @GetMapping("/marketingGestores")
    public String marketingGestores() {
        return "/rubros/marketingGestores.html";
    }
     @PreAuthorize("permitAll()")
    @GetMapping("/saludBelleza")
    public String saludBelleza() {
        return "/rubros/saludBelleza.html";
    }
     @PreAuthorize("permitAll()")
    @GetMapping("/serviciosProfesionales")
    public String serviciosProfesionales() {
        return "/rubros/serviciosProfesionales.html";
    }
     @PreAuthorize("permitAll()")
    @GetMapping("/tareasDomesticas")
    public String tareasDomesticas() {
        return "/rubros/tareasDomesticas.html";
    }
     @PreAuthorize("permitAll()")
    @GetMapping("/arteCulturaGastronomia")
    public String arteCulturaGastronomia() {
        return "/rubros/arteCulturaGastronomia.html";
    }
    
    

}
