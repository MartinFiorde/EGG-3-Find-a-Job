package FindAJob.controladores;

import FindAJob.entidades.Usuario;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String formularioUsuario(ModelMap model, @RequestParam(required = false) @PathVariable(required = false) String id) throws ErrorServicio {
        if (id != null) {
            Usuario usuario = usuarioServicio.validarId(id);
            model.put("mail", usuario.getMail());
            model.put("clave", usuario.getClave());
        }
        return "/testMAFBEnd/registro-test.html";
    }

    @PostMapping("/register2")
    @PreAuthorize("permitAll()")
    public String cargarUsuario(ModelMap model, @RequestParam(required = false) String mail, @RequestParam(required = false) String clave, @RequestParam(required = false) String clave2) {
        try {
            System.out.println("registrar");
            usuarioServicio.registrarCuenta(mail, clave, clave2);
            model.put("error", "Se ha registrado correctamente!");
            return "/testMAFBEnd/index-test.html";
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("mail", mail);
            model.put("clave", clave);
            model.put("clave2", clave2);
            return "/testMAFBEnd/registro-test.html";
        }
    }

    @GetMapping("/login")
    @PreAuthorize("permitAll()")
    public String registroUsuario(ModelMap model, @RequestParam(required = false) String mail, @RequestParam(required = false) String clave, @PathVariable(required = false) String error) {
        model.addAttribute("mail", mail);
        model.addAttribute("clave", clave);
        return "/testMAFBEnd/login-test.html";
    }
}
