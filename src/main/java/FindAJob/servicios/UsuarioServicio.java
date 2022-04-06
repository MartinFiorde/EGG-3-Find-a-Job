package FindAJob.servicios;

import FindAJob.entidades.Usuario;
import FindAJob.enums.Rol;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio implements UserDetailsService {

    // INSTANCIAS Y CONSTRUCTOR
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    // METODOS
    @Transactional(rollbackOn = Exception.class)
    public void registrarCuenta(String mail, String clave, String clave2) throws ErrorServicio {
        Usuario usuario = new Usuario();
        usuario.setMail(validarMail(null, mail));
        String claveEncriptada = new BCryptPasswordEncoder().encode(validarClaves(clave, clave2));
        usuario.setClave(claveEncriptada);
        usuario.setActivo(Boolean.TRUE);
        usuario.setRol(Rol.USER);
        usuarioRepositorio.save(usuario);
    }

    @Transactional(rollbackOn = Exception.class)
    public void registrarDatosUsuario(/*todos los atributos parametrizables por el usuario*/) throws ErrorServicio {
        // validar y guardar en base de datos
    }
    
    @Transactional(rollbackOn = Exception.class)
    public void modificarDatosUsuario(/*todos los atributos parametrizables por el usuario*/) throws ErrorServicio {
        // validar y guardar en base de datos
    }
    
    public Usuario validarId(String id) throws ErrorServicio {
        Optional<Usuario> res = usuarioRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro el usuario solicitado.");
        }
        return res.get();
    }

    public String validarMail(String id, String mail) throws ErrorServicio {
        if (mail == null || mail.trim().isEmpty()) {
            throw new ErrorServicio("Debe ingresar un mail válido.");
        }
        if (usuarioRepositorio.buscarMailEqual(mail.toLowerCase()) != null && usuarioRepositorio.buscarMailEqual(mail.toLowerCase()).getId() != id) {
            throw new ErrorServicio("Ya existe un usuario con el mail seleccionado.");
        }
        return mail.toLowerCase();
    }

    public String validarClaves(String clave, String clave2) throws ErrorServicio {
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no coinciden.");
        }
        return clave;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarMailEqual(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        List<GrantedAuthority> permissions = new ArrayList();
        GrantedAuthority rolePermissions = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
        permissions.add(rolePermissions);
        return new User(usuario.getMail(), usuario.getClave(), permissions);
    }

}
