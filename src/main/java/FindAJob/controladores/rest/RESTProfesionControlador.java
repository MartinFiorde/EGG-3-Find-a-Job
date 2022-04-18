package FindAJob.controladores.rest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profesion")
public class RESTProfesionControlador {

    @GetMapping("/tipo/{rubro}")
    public ResponseEntity<List<String>> obtenerTipo(@PathVariable("rubro") String rubro) {

        List<String> tipos = new ArrayList();

        tipos.add("Chiquito 1");
        tipos.add("Chiquito 2");
        tipos.add("Chiquito 3");
        tipos.add("Chiquito 4");
        tipos.add("Chiquito 5");

        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/subtipo/{tipo}")
    public ResponseEntity<List<String>> obtenerSubtipo(@PathVariable("tipo") String tipo) {
        List<String> subtipos = new ArrayList();

        return ResponseEntity.ok(subtipos);
    }

}
