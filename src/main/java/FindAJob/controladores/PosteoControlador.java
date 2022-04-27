package FindAJob.controladores;

import FindAJob.entidades.Posteo;
import FindAJob.entidades.Usuario;
import FindAJob.enums.Rubro;
import FindAJob.enums.Status;
import FindAJob.enums.Zona;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.PosteoRepositorio;
import FindAJob.servicios.PosteoServicio;
import FindAJob.servicios.ProfesionServicio;
import FindAJob.servicios.ReferenciaServicio;
import FindAJob.servicios.UsuarioServicio;
import java.time.LocalDate;
import java.util.ArrayList;
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
        List<Posteo> posteos = posteoServicio.dejarSoloTrabajadorLogeadoDeResultados(posteoServicio.findAll());
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
    public String procesarPosteoForm(ModelMap model, @PathVariable(required = false) @RequestParam(required = false) String idPosteo,
            @ModelAttribute Posteo posteo, @RequestParam(required = false) String zona,
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
            List<Posteo> posteos = posteoServicio.dejarSoloTrabajadorLogeadoDeResultados(posteoServicio.findAll());
            model.put("posteos", posteos);

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
    public String AltaB(ModelMap model, @PathVariable String idPosteo) {
        try {
            // activar cuando este referencia funcional
//            if (posteoServicio.validarId(idPosteo).getReferencia() == null) {
//                throw new ErrorServicio("Error. Cargue referencias en la sub profesion e intente nuevamente");
//            }
            posteoServicio.subirPosteoB(idPosteo);
            List<Posteo> posteos = posteoServicio.dejarSoloTrabajadorLogeadoDeResultados(posteoServicio.findAll());
            model.put("posteos", posteos);
            // redirigir a la pagina de carga de referencias
            return "redirect:/post/lista";
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("IdPosteo", idPosteo);
// redirigir a la pagina de carga de referencias
            return "/testMAFBEnd/p/post-form-test.html";
        }

    }

    @GetMapping("post/baja/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String BajaA(ModelMap model, @PathVariable String idPosteo) throws ErrorServicio {
        posteoServicio.BajaA(idPosteo);
        List<Posteo> posteos = posteoServicio.dejarSoloTrabajadorLogeadoDeResultados(posteoServicio.findAll());
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
            posteos = posteoServicio.quitarTrabajadorLogeadoDeResultados(posteoServicio.buscarPostsPorRubroYStatusB(idRubro, "B_PUBLICADO"));
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        if (idTipo != null) {
            posteos = posteoServicio.quitarTrabajadorLogeadoDeResultados(posteoServicio.buscarPostsPorRubroYStatusB(idRubro, "B_PUBLICADO"));
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        if (idRubro != null) {
            posteos = posteoServicio.quitarTrabajadorLogeadoDeResultados(posteoServicio.buscarPostsPorRubroYStatusB(idRubro, "B_PUBLICADO"));
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        posteos = posteoServicio.quitarTrabajadorLogeadoDeResultados(posteoServicio.buscarPostsPorStatusB("B_PUBLICADO"));
        model.put("posteos", posteos);
        return "/post/buscador.html";
    }

    @PostMapping("post/buscador")
    @PreAuthorize("isAuthenticated()")
    public String buscarPostsPorRubro(ModelMap model, @RequestParam(required = false) String rubro, @RequestParam(required = false) String tipo, @RequestParam(required = false) String subtipo) throws ErrorServicio {
        cargarListasDesplegables(model);
        List<Posteo> posteos = null;
        if (subtipo != null && !subtipo.equals("null")) {
            System.out.println("filtro por subtipo: " + subtipo);
            posteos = posteoServicio.buscarPostsPorSubtipoYStatusB(subtipo, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        if (tipo != null && !tipo.equals("null")) {
            System.out.println("filtro por tipo: " + tipo);
            posteos = posteoServicio.buscarPostsPorTipoYStatusB(tipo, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        if (rubro != null && !rubro.equals("null")) {
            System.out.println("filtro por rubro: " + rubro);
            posteos = posteoServicio.buscarPostsPorRubroYStatusB(rubro, "B_PUBLICADO");
            model.put("posteos", posteos);
            return "/post/buscador.html";
        }
        System.out.println("sin filtro");
        posteos = posteoServicio.buscarPostsPorStatusB("B_PUBLICADO");
        model.put("posteos", posteos);
        return "/post/buscador.html";
    }

    @GetMapping("trabajo/form/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String enviarTrabajoForm(ModelMap model, @PathVariable(required = false) String idPosteo) throws ErrorServicio {
        Posteo posteo = posteoServicio.validarId(idPosteo);
        model.put("posteo", posteo);
        return "/testMAFBEnd/p/trabajo-form-test.html";
    }

    @PostMapping("trabajo/form")
    @PreAuthorize("isAuthenticated()")
    public String procesarTrabajoForm(ModelMap model,
            @PathVariable(required = false) @RequestParam(required = false) String idPosteo,
            @ModelAttribute Posteo posteo) throws ErrorServicio {
        try {
            if (idPosteo != null) {
                posteo.setId(idPosteo);
            }
            System.out.println("id: " + idPosteo);
            System.out.println("posteo: " + posteo);
            posteoServicio.solicitarC(idPosteo, posteo.getDescripcionSolicitud(), posteo.getEntregaTrabajo(), posteo.getDineroGuardado());
            System.out.println("edita");

            model.put("posteo", posteo);
            model.put("error", "Los datos se han guardado correctamente!");

            List<Posteo> posteos = posteoServicio.buscarPostsPorStatusB("B_PUBLICADO");
            model.put("posteos", posteos);
            return "/testMAFBEnd/p/trabajo-lista-test.html";

        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("posteo", posteo);
            return "/testMAFBEnd/p/trabajo-form-test.html";
        }
    }

    @GetMapping("trabajo/lista/{tipoUsuario}")
    @PreAuthorize("isAuthenticated()")
    public String verListaTrabajos(ModelMap model, @PathVariable String tipoUsuario) throws ErrorServicio {
        List<Posteo> posteos = new ArrayList();
        if (tipoUsuario.equals("trabajador")) {
            posteos.addAll(posteoServicio.buscarTrabajoPorTrabajador(Status.C_ENPROCESO, usuarioServicio.returnIdSession()));
            posteos.addAll(posteoServicio.buscarTrabajoPorTrabajador(Status.D_ENTREGADO, usuarioServicio.returnIdSession()));
            posteos.addAll(posteoServicio.buscarTrabajoPorTrabajador(Status.E_PAGADO, usuarioServicio.returnIdSession()));
        }
        if (tipoUsuario.equals("cliente")) {
            posteos.addAll(posteoServicio.buscarTrabajoPorCliente(Status.C_ENPROCESO, usuarioServicio.returnIdSession()));
            posteos.addAll(posteoServicio.buscarTrabajoPorCliente(Status.D_ENTREGADO, usuarioServicio.returnIdSession()));
            posteos.addAll(posteoServicio.buscarTrabajoPorCliente(Status.E_PAGADO, usuarioServicio.returnIdSession()));
        }
        model.put("posteos", posteos);
        return "/testMAFBEnd/p/trabajo-lista-test.html";
    }

    @GetMapping("trabajo/ver/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String verTrabajo(ModelMap model, @PathVariable String idPosteo) throws ErrorServicio {
        model.put("idUser", usuarioServicio.returnIdSession());
        model.put("posteo", posteoServicio.validarId(idPosteo));
        return "/testMAFBEnd/p/trabajo-ver-test.html";
    }

    @GetMapping("trabajo/entregar/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String entregarTrabajo(ModelMap model, @PathVariable String idPosteo) throws ErrorServicio {
        Posteo posteo = posteoServicio.validarId(idPosteo);
        if (!posteo.getTrabajador().getId().equals(usuarioServicio.returnIdSession())) {
            throw new ErrorServicio("No tiene autorización para entregar el trabajo");
        }
        try {
            posteoServicio.entregarD(idPosteo);
            model.put("idUser", usuarioServicio.returnIdSession());
            model.put("posteo", posteoServicio.validarId(idPosteo));
            return "/testMAFBEnd/p/trabajo-ver-test.html";
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("posteo", posteoServicio.validarId(idPosteo));
            return "/testMAFBEnd/p/trabajo-ver-test.html";
        }
    }

    @GetMapping("trabajo/pagar/{idPosteo}")
    @PreAuthorize("isAuthenticated()")
    public String pagarTrabajo(ModelMap model, @PathVariable String idPosteo) throws ErrorServicio {
        Posteo posteo = posteoServicio.validarId(idPosteo);
        if (!posteo.getCliente().getId().equals(usuarioServicio.returnIdSession())) {
            throw new ErrorServicio("No tiene autorización para pagar el trabajo");
        }
        try {
            posteoServicio.PagarE(idPosteo);
            model.put("idUser", usuarioServicio.returnIdSession());
            model.put("posteo", posteoServicio.validarId(idPosteo));
            return "/testMAFBEnd/p/trabajo-ver-test.html";
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("posteo", posteoServicio.validarId(idPosteo));
            return "/testMAFBEnd/p/trabajo-ver-test.html";
        }
    }

    @GetMapping("admin/posteos")
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN')")
    public String verListaTrabajosAdmin(ModelMap model) throws ErrorServicio {
        List<Posteo> posteos = posteoServicio.findAll();
        model.put("posteos", posteos);
        return "/testMAFBEnd/p/trabajo-lista-test.html";
    }
    
}
