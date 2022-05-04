package FindAJob.controladores;

import FindAJob.entidades.Archivo;
import FindAJob.entidades.Referencia;
import FindAJob.enums.Rubro;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.servicios.ArchivoServicio;
import FindAJob.servicios.PosteoServicio;
import FindAJob.servicios.ProfesionServicio;
import FindAJob.servicios.ReferenciaServicio;
import FindAJob.servicios.UsuarioServicio;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/referencia")
@Controller
public class ReferenciaControlador {

    private UsuarioServicio usuarioServicio;
    private ReferenciaServicio referenciaServicio;
    private ArchivoServicio archivoServicio;
    private ProfesionServicio profesionServicio;

    @Autowired

    public ReferenciaControlador(UsuarioServicio usuarioServicio, ReferenciaServicio referenciaServicio, ArchivoServicio archivoServicio, ProfesionServicio profesionServicio) {
        this.usuarioServicio = usuarioServicio;
        this.referenciaServicio = referenciaServicio;
        this.archivoServicio = archivoServicio;
        this.profesionServicio = profesionServicio;

    }

    @GetMapping("/trabajador")
    @PreAuthorize("isAuthenticated()")
    public String verReferencias(ModelMap model) {

        try {

            model.put("listaReferencias", referenciaServicio.traerReferenciasUsuarioLogueado());

        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
        }

        return "testRodrigo/listaReferencia";
    }

    @GetMapping("/form")
    @PreAuthorize("isAuthenticated()")
    public String agregarReferencia(ModelMap model) {

        Referencia referencia = new Referencia();

        List<Rubro> rubros = Arrays.asList(Rubro.values());
        model.put("rubros", rubros);
        
        model.addAttribute("referencia", referencia);

        return "testRodrigo/testReferencia";
    }

    @PostMapping("/form")
    @PreAuthorize("isAuthenticated()")
    public String traerReferencia(@ModelAttribute Referencia referencia,
            ModelMap model,
            @RequestParam(required = false) String rubro,
            MultipartFile archivo,
            @RequestParam(required = false) LocalDate date,
            @RequestParam String subtipo) {

        try {
            

          
            referencia = referenciaServicio.inicializarNuevaReferencia(referencia);              //inicia valores en 0
            Archivo archivo2 = archivoServicio.guardar(archivo);                                 //guardar ya tiene sus validaciones
            System.out.println("archivo " + archivo2);
            referencia = referenciaServicio.asignarArchivo(referencia, archivo2);                //revisar error, sube archivo pero no vincula archivo con referencia
            usuarioServicio.validarProfesionDuplicada(subtipo);
            referencia.setProfesion(profesionServicio.devolverProfesionDelSubtipo(subtipo));
            referenciaServicio.guardar(referencia);                                              //guardar tiene sus validaciones dentro
            usuarioServicio.asignarReferencia(referencia);

            model.put("listaReferencias", referenciaServicio.traerReferenciasUsuarioLogueado()); //envia a la vista lista de referencia con alta dada

        } catch (ErrorServicio ex) {
            model.put("error", ex.getMessage());
            System.out.println(ex);

            List<Rubro> rubros = Arrays.asList(Rubro.values());
            model.put("rubros", rubros);
            
            return "testRodrigo/testReferencia";
        } catch (IOException ex) {
             List<Rubro> rubros = Arrays.asList(Rubro.values());
            model.put("rubros", rubros);
           
            model.put("error2", ex.getMessage());
            return "testRodrigo/testReferencia";
        }
        return "testRodrigo/listaReferencia";

    }

    @GetMapping("/editar/")
    public String Editar(@RequestParam String id,
            ModelMap model) {

        try {

            Referencia referencia2 = referenciaServicio.buscarPorId(id);
            model.addAttribute("referencia", referencia2);

            List<Rubro> rubros = Arrays.asList(Rubro.values());
            model.put("rubros", rubros);

            model.put("idRubro", referencia2.getProfesion().getRubro().getNombreLindo());
            model.put("tipos", profesionServicio.devolverTiposFiltradosPorRubro(referencia2.getProfesion().getRubro().toString()));
            model.put("idTipo", referencia2.getProfesion().getTipo());
            model.put("subtipos", profesionServicio.devolverSubtiposFiltradosPorTipo(referencia2.getProfesion().getTipo()));
            model.put("idSubtipo", referencia2.getProfesion().getSubtipo());

            return "testRodrigo/editarReferencia";
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "testRodrigo/listaReferencia";
        }

    }

    @PostMapping("/editado")
    public String editado(@ModelAttribute Referencia referencia,
            ModelMap model,
            @RequestParam(required = false) LocalDate date,
            @RequestParam String subtipo) {

        try {
            usuarioServicio.validarProfesionDuplicada(subtipo);
            referencia.setProfesion(profesionServicio.devolverProfesionDelSubtipo(subtipo));

            referencia.setAlta(Boolean.TRUE);
            referenciaServicio.guardar(referencia);

            model.put("listaReferencias", referenciaServicio.traerReferenciasUsuarioLogueado()); //envia a la vista lista de referencia con alta dada
            return "testRodrigo/listaReferencia";
        } catch (ErrorServicio e) {
            System.out.println(e);
            model.put("error", e.getMessage());
            return "testRodrigo/editarReferencia";
        }
    }

    @GetMapping("/eliminar")
    public String eliminarReferencia(ModelMap model,
            @RequestParam String id) {

        try {
            Referencia referencia = referenciaServicio.buscarPorId(id);
            referencia = referenciaServicio.baja(id);
            referenciaServicio.guardar(referencia);
            model.put("listaReferencias", referenciaServicio.traerReferenciasUsuarioLogueado());

        } catch (Exception e) {
            model.put("Error", e);
        }

        return "testRodrigo/listaReferencia";
    }

}
