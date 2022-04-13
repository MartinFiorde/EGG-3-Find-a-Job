package FindAJob.controladores;

import FindAJob.entidades.Referencia;
import FindAJob.enums.Rubro;
import FindAJob.servicios.ProfesionServicio;
import FindAJob.servicios.ReferenciaServicio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.GetMapping;
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

    
    

    @GetMapping
    // doc con todas las opciones de autorizacion >>> https://www.baeldung.com/spring-security-expressions
    @PreAuthorize("permitAll()")
    public String index() {
        return "index.html";
    }
    
    
    //TESTEO RODRIGO
    //IGNORAR
    @GetMapping("/form")
    public String testeoReferencia(ModelMap model ){ //, @RequestParam("enumRubro")Rubro rubro 
        List<String> rubros = new ArrayList();
            for (Rubro aux : Rubro.values()){
                rubros.add(aux.getNombreLindo().toString());
            }

        model.addAttribute("rubros", rubros);
        model.addAttribute("referencia", new Referencia());
        
        return "testRodrigo/testReferencia";
    }
    
    @PostMapping("/form")
    public String reciboTesteoReferencia(ModelMap model,@RequestParam String idRubro){
        System.out.println("idRubro");
        
        return null; //retornar vista
    }
   
    
  
}
