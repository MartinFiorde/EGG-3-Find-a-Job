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
        return "registro.html";
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
            return "index.html";
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
        return "/settings/cambioClave";
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
            return"/settings/cambioClave";

        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("clave", clave);
            model.put("clave2", clave2);
            return "/settings/cambioClave";
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
        return "/settings/cambioDatos";
    }

    @PostMapping("usuario/datos2")
    @PreAuthorize("isAuthenticated()")
    public String cargarCambioDatos(ModelMap model, @ModelAttribute Usuario usuario, @RequestParam(required = false) String zona2, @RequestParam(required = false) MultipartFile foto) {
        try {
            usuario.setZona(Zona.valueOf(zona2));
            usuario.setId(usuarioServicio.returnIdSession());
            usuarioServicio.modificarDatos(usuario, foto);
            model.put("error", "Los datos se han guardado correctamente!");
            return "/settings/cambioDatos";

        } catch (Exception ex) {
            System.out.println(ex);
            List<Zona> zonas2 = Arrays.asList(Zona.values());
            model.put("error", ex.getMessage());
            model.put("usuario", usuario);
            model.put("zonas2", zonas2);
            model.put("idZona", usuario.getZona().getNombreCiudad());
            model.put("foto", foto);
            return "/settings/cambioDatos";
        }
    }

    @GetMapping("/usuario")
    @PreAuthorize("isAuthenticated()")
    public String verPerfilUsuario(ModelMap model) throws ErrorServicio {
        Usuario usuario = usuarioServicio.validarId(usuarioServicio.returnIdSession());
        model.put("usuario", usuario);
        return "vistaUsuario.html";
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("isAuthenticated()")
    public String verPerfilUsuario(ModelMap model, @PathVariable String idUsuario) throws ErrorServicio {
        Usuario usuario = usuarioServicio.validarId(idUsuario);
        model.put("usuario", usuario);
        return "vistaUsuario.html";
    }
    
    @GetMapping("/admin/usuarios")
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN')")
    public String verListaUsuarios(ModelMap model) {
        model.put("usuarios", usuarioServicio.findAll());
        return "listaUsers";
    }

    @GetMapping("/usuario/saldo")
    @PreAuthorize("isAuthenticated()")
    public String verMenuDeSaldo(ModelMap model) throws ErrorServicio {
        Usuario usuario = usuarioServicio.validarId(usuarioServicio.returnIdSession());
        model.put("usuario", usuario);
        return "/dinero/dinero.html";
    }

    @PostMapping("/usuario/saldo")
    @PreAuthorize("isAuthenticated()")
    public String procesarCambioDeSaldo(ModelMap model, @RequestParam(required = false) Double carga, @RequestParam(required = false) Double retiro) throws ErrorServicio {
        try {
            if (carga == null) {
                carga = 0d;
            }
            if (retiro == null) {
                retiro = 0d;
            }
            Usuario usuario = usuarioServicio.validarId(usuarioServicio.returnIdSession());
            usuarioServicio.CargarOQuitarDinero(usuario.getId(), carga - retiro);
            model.put("usuario", usuario);
            if (Math.abs(carga)+Math.abs(retiro) != 0) {
            model.put("error", "Su operacion se realizó con éxito!");    
            }
            return "/dinero/dinero.html";
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("usuario", usuarioServicio.validarId(usuarioServicio.returnIdSession()));
            return "/dinero/dinero.html";
        }
    }

}

