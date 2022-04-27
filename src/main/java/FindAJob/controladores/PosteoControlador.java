package FindAJob.controladores;

import FindAJob.entidades.Posteo;
import FindAJob.entidades.Usuario;
import FindAJob.enums.Rubro;
import FindAJob.enums.Zona;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.PosteoRepositorio;
import FindAJob.servicios.PosteoServicio;
import FindAJob.servicios.ProfesionServicio;
import FindAJob.servicios.ReferenciaServicio;
import FindAJob.servicios.UsuarioServicio;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PosteoControlador {

    private UsuarioServicio usuarioServicio;
    private PosteoServicio posteoServicio;
    private ReferenciaServicio referenciaServicio;
    private ProfesionServicio profesionServicio;

    @Autowired
    public PosteoControlador(UsuarioServicio usuarioServicio, PosteoServicio posteoServicio, ReferenciaServicio referenciaServicio, ProfesionServicio profesionServicio) {
        this.usuarioServicio = usuarioServicio;
        this.posteoServicio = posteoServicio;
        this.referenciaServicio = referenciaServicio;
        this.profesionServicio = profesionServicio;
    }

    private ModelMap cargarListasDesplegables(ModelMap model) {
        List<Zona> zonas = Arrays.asList(Zona.values());
        model.put("zonas", zonas);
        List<Rubro> rubros = Arrays.asList(Rubro.values());
        model.put("rubros", rubros);
        return model;
    }

    @GetMapping("post/lista")
    @PreAuthorize("isAuthenticated()")
    public String verListaPosts(ModelMap model) throws ErrorServicio {
        // FALTA APLICAR UN FILTRO X TRABAJADOR
        List<Posteo> posteos = posteoServicio.findAll();
        model.put("posteos", posteos);
        return "/post/postLista.html";
    }

    @GetMapping("post/form/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String enviarPosteoForm(ModelMap model,
            @PathVariable(required = false) String idPosteo) throws ErrorServicio {
        Posteo posteo = new Posteo();
        System.out.println("idPosteo: " + idPosteo);
        if (!idPosteo.equals("new")) {
            posteo = posteoServicio.validarId(idPosteo);
            model.put("idZona", posteo.getZona().getNombreCiudad());
            model.put("idRubro", posteo.getProfesion().getRubro().getNombreLindo());
            model.put("tipos", profesionServicio.devolverTiposFiltradosPorRubro(posteo.getProfesion().getRubro().toString()));
            model.put("idTipo", posteo.getProfesion().getTipo());
            model.put("subtipos", profesionServicio.devolverSubtiposFiltradosPorTipo(posteo.getProfesion().getTipo()));
            model.put("idSubtipo", posteo.getProfesion().getSubtipo());

        }
        cargarListasDesplegables(model);
        model.put("posteo", posteo);
        return "/testMAFBEnd/p/post-form-test.html";
    }

    @PostMapping("post/form/")
    @PreAuthorize("isAuthenticated()")
    public String procesarPosteoForm(
            ModelMap model,
            @PathVariable(required = false) @RequestParam(required = false) String idPosteo,
            @ModelAttribute Posteo posteo,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String subtipo) throws ErrorServicio {

        try {
            if (idPosteo != null) {
                posteo.setId(idPosteo);
            }
            posteo.setZona(Zona.valueOf(zona));
            posteo.setProfesion(profesionServicio.devolverProfesionDelSubtipo(subtipo));
            posteo.setTrabajador(usuarioServicio.validarId(usuarioServicio.returnIdSession()));
            //posteo.setReferencia(referenciaServicio.buscarReferenciaParaPosteo(posteo, posteo.getProfesion().getId()));
            if (idPosteo != null) {
                posteoServicio.crearBorradorA(idPosteo, posteo, posteo.getProfesion().getId(), zona);
                System.out.println("edita");
            } else {
                posteoServicio.crearBorradorA(null, posteo, posteo.getProfesion().getId(), zona);
                System.out.println("crea");
            }

            model.put("posteo", posteo);
            model.put("idZona", posteo.getZona().getNombreCiudad());
            model.put("idRubro", posteo.getProfesion().getRubro().getNombreLindo());
            cargarListasDesplegables(model);
            model.put("error", "Los datos se han guardado correctamente!");
            return "/post/postLista.html";

        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("posteo", posteo);
            model.put("idZona", posteo.getZona().getNombreCiudad());
            model.put("idRubro", posteo.getProfesion().getRubro().getNombreLindo());
            cargarListasDesplegables(model);
            return "/testMAFBEnd/p/post-form-test.html";
        }
    }

    @GetMapping("post/alta/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String AltaB(ModelMap model, @PathVariable String idPosteo) throws ErrorServicio {
        posteoServicio.subirPosteoB(idPosteo);
        List<Posteo> posteos = posteoServicio.findAll();
        model.put("posteos", posteos);
        return "redirect:/post/lista";

    }

    @GetMapping("post/baja/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String BajaA(ModelMap model, @PathVariable String idPosteo) throws ErrorServicio {
        posteoServicio.BajaA(idPosteo);
        List<Posteo> posteos = posteoServicio.findAll();
        model.put("posteos", posteos);
        return "redirect:/post/lista";
    }

    @GetMapping("post/ver/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String verPost(ModelMap model, @PathVariable String idPosteo) throws ErrorServicio {
        model.put("posteo", posteoServicio.validarId(idPosteo));
        return "/testMAFBEnd/p/post-ver-test.html";
    }

    @PostMapping("chat/escribir/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String EscribirEnChat(ModelMap model, @PathVariable String idPosteo, @RequestParam String msj) throws ErrorServicio {
        try {
            posteoServicio.escribirChat(idPosteo, msj);
            model.put("posteo", posteoServicio.validarId(idPosteo));
            return "redirect:/post/ver/" + idPosteo;
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("posteo", posteoServicio.validarId(idPosteo));
            return "/testMAFBEnd/p/post-ver-test.html";
        }

    }

    @GetMapping("post/buscador")
    @PreAuthorize("isAuthenticated()")
    public String buscarPosts(ModelMap model, @RequestParam(required = false) String idRubro, @RequestParam(required = false) String idTipo, @RequestParam(required = false) String idSubtipo, @RequestParam(required = false) String idZona) throws ErrorServicio {
        cargarListasDesplegables(model);
        List<Posteo> posteos = null;
        if (idSubtipo != null) {
            posteos = posteoServicio.buscarPostsPorRubroYStatusB(idRubro, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        if (idTipo != null) {
            posteos = posteoServicio.buscarPostsPorRubroYStatusB(idRubro, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        if (idRubro != null) {
            posteos = posteoServicio.buscarPostsPorRubroYStatusB(idRubro, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        posteos = posteoServicio.buscarPostsPorStatusB("B_PUBLICADO");
        model.put("posteos", posteos);
        return "/post/buscador.html";
    }

    @PostMapping("post/buscador")
    @PreAuthorize("isAuthenticated()")
    public String buscarPostsPorRubro(ModelMap model, @RequestParam(required = false) String rubro, @RequestParam(required = false) String tipo, @RequestParam(required = false) String subtipo) throws ErrorServicio {
        cargarListasDesplegables(model);
        List<Posteo> posteos = null;
        if (subtipo != null && !subtipo.equals("null")) {
            System.out.println("filtro por subtipo: "+subtipo);
            posteos = posteoServicio.buscarPostsPorSubtipoYStatusB(subtipo, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        if (tipo != null && !tipo.equals("null")) {
            System.out.println("filtro por tipo: "+tipo);
            posteos = posteoServicio.buscarPostsPorTipoYStatusB(tipo, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        if (rubro != null && !rubro.equals("null")) {
            System.out.println("filtro por rubro: "+rubro);
            posteos = posteoServicio.buscarPostsPorRubroYStatusB(rubro, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        System.out.println("sin filtro");
        posteos = posteoServicio.buscarPostsPorStatusB("B_PUBLICADO");
        model.put("posteos", posteos);
        return "/post/buscador.html";
    }

}
