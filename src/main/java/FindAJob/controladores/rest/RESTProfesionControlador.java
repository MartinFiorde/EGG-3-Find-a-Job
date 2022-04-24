package FindAJob.controladores.rest;

import FindAJob.servicios.ProfesionServicio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profesion")
public class RESTProfesionControlador {

    private ProfesionServicio profesionServicio;

    @Autowired
    public RESTProfesionControlador(ProfesionServicio profesionServicio) {
        this.profesionServicio = profesionServicio;
    }
    
    @GetMapping("/tipo/{rubro}")
    public ResponseEntity<List<String>> obtenerTipo(@PathVariable("rubro") String rubro) {
        List<String> tipos = profesionServicio.devolverTiposFiltradosPorRubro(rubro);
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/subtipo/{tipo}")
    public ResponseEntity<List<String>> obtenerSubtipo(@PathVariable("tipo") String tipo) {
        List<String> subtipos = profesionServicio.devolverSubtiposFiltradosPorTipo(tipo);
        return ResponseEntity.ok(subtipos);
    }

}
