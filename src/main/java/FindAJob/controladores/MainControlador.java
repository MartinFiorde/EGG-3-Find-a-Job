package FindAJob.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@PreAuthorize("permitAll()")
public class MainControlador {

    @GetMapping("testindex")
    // doc con todas las opciones de autorizacion >>> https://www.baeldung.com/spring-security-expressions
    @PreAuthorize("permitAll()")
    public String testIndex() {
        return "testMAFBEnd/index-test.html";
    }

    @GetMapping
    // doc con todas las opciones de autorizacion >>> https://www.baeldung.com/spring-security-expressions
    @PreAuthorize("permitAll()")
    public String Indextemp() {
        return "testMAFBEnd/index-test.html";
    }

}
