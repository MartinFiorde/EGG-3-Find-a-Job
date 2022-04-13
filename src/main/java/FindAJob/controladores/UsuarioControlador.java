package FindAJob.controladores;

import FindAJob.entidades.Usuario;
import FindAJob.enums.Zona;
import FindAJob.excepciones.ErrorServicio;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
@PreAuthorize("permitAll()")
public class UsuarioControlador {

    // INSTANCIAS Y CONSTRUCTOR
    UsuarioServicio usuarioServicio;

    @Autowired
    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    // METODOS
    @GetMapping("/register")
    @PreAuthorize("permitAll()")
    public String formularioUsuario(ModelMap model, @RequestParam(required = false) String idSesion) throws ErrorServicio {
        return "/testMAFBEnd/registro-test.html";
    }

    @PostMapping("/register2")
    @PreAuthorize("permitAll()")
    public String cargarUsuario(ModelMap model, String mail, @RequestParam String clave, @RequestParam String clave2) {
        try {
            System.out.println("registrar");
            usuarioServicio.registrarCuenta(mail, clave, clave2);
            model.put("error", "Se ha registrado correctamente!");
            return "registro.html";
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("mail", mail);
            model.put("clave", clave);
            model.put("clave2", clave2);
            return "registro.html";
        }
    }

    @GetMapping("/login")
    @PreAuthorize("permitAll()")
    public String registroUsuario(ModelMap model, @RequestParam(required = false) String mail, @RequestParam(required = false) String clave, @PathVariable(required = false) String error) {
        model.addAttribute("mail", mail);
        model.addAttribute("clave", clave);
        return "login.html";
    }

    @GetMapping("usuario/clave")
    @PreAuthorize("isAuthenticated()")
    public String formularioCambioClave(ModelMap model) throws ErrorServicio {
        return "/testMAFBEnd/cambio-clave-test.html";
    }

    // 3 metodos para traer el id del usuario a un controlador. El mas práctico y seguro es el /*3*/
    @PostMapping("usuario/clave2/{mailpath}"/*1*/)
    @PreAuthorize("isAuthenticated()")
    public String cargarCambioClave(ModelMap model, /*1*/ @PathVariable String mailpath, /*2*/ /*@RequestParam String mailform, */ @RequestParam String clavevieja, @RequestParam String clave, @RequestParam String clave2) {
        try {
            /*3*/ String idsesion = usuarioServicio.returnIdSession();
            usuarioServicio.validarClaveVieja(idsesion, clavevieja);
            usuarioServicio.modificarClave(idsesion, clave, clave2);
            model.put("error", "La clave se ha cambiado correctamente!");
            return "/testMAFBEnd/index-test.html";

        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("clave", clave);
            model.put("clave2", clave2);
            return "/testMAFBEnd/cambio-clave-test.html";
        }
    }

    @GetMapping("usuario/datos")
    @PreAuthorize("isAuthenticated()")
    public String formularioCambioDatos(ModelMap model) throws ErrorServicio {
        Usuario usuario = usuarioServicio.validarId(usuarioServicio.returnIdSession());
        List<Zona> zonas2 = Arrays.asList(Zona.values());
        model.put("usuario", usuario);
        model.put("zonas2", zonas2);
        model.put("idZona", usuario.getZona().getNombreCiudad());
        model.put("foto", usuario.getFoto());
        return "/testMAFBEnd/cambio-datos-test.html";
    }

    @PostMapping("usuario/datos2")
    @PreAuthorize("isAuthenticated()")
    public String cargarCambioDatos(ModelMap model, @ModelAttribute Usuario usuario, @RequestParam(required = false) String zona2, @RequestParam(required = false) MultipartFile foto) {
        try {
            usuario.setZona(Zona.valueOf(zona2));
            usuario.setId(usuarioServicio.returnIdSession());
            usuarioServicio.modificarDatos(usuario, foto);
            model.put("error", "Los datos se han guardado correctamente!");
            return "/testMAFBEnd/index-test.html";

        } catch (Exception ex) {
            System.out.println(ex);
            List<Zona> zonas2 = Arrays.asList(Zona.values());
            model.put("error", ex.getMessage());
            model.put("usuario", usuario);
            model.put("zonas2", zonas2);
            model.put("idZona", usuario.getZona().getNombreCiudad());
            model.put("foto", foto);
            return "/testMAFBEnd/cambio-datos-test.html";
        }
    }

    @GetMapping("/usuario")
    @PreAuthorize("isAuthenticated()")
    public String verPerfilUsuario(ModelMap model) throws ErrorServicio {
        Usuario usuario = usuarioServicio.validarId(usuarioServicio.returnIdSession());
        model.put("usuario", usuario);
        return "/testMAFBEnd/usuario-test.html";
    }

    @GetMapping("usuario/lista-usuarios")
    @PreAuthorize("isAuthenticated()")
    public String verListaUsuarios(ModelMap model, @PathVariable String idUsuario) {

        return "/testMAFBEnd/lista-usuarios-test.html";
    }

}
