package FindAJob.servicios;

import FindAJob.entidades.Referencia;
import FindAJob.entidades.Usuario;
import FindAJob.enums.Rol;
import FindAJob.enums.Zona;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    // INSTANCIAS Y CONSTRUCTOR
    private UsuarioRepositorio usuarioRepositorio;
    private ArchivoServicio archivoServicio;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, ArchivoServicio archivoServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.archivoServicio = archivoServicio;
    }

    // METODOS
    
    @Transactional(rollbackOn = Exception.class)
    public void guardar (Usuario usuario){
        usuarioRepositorio.save(usuario);
    }
    
    
    @Transactional(rollbackOn = Exception.class)
    public void registrarCuenta(String mail, String clave, String clave2) throws ErrorServicio {
        Usuario usuario = new Usuario();
        usuario.setMail(validarMail(null, mail));
        String claveEncriptada = new BCryptPasswordEncoder().encode(validarClaves(clave, clave2));
        usuario.setClave(claveEncriptada);
        usuario.setAlta(new Date());
        usuario.setDineroEnCuenta(0d);
        usuario.setActivo(Boolean.TRUE);
        usuario.setRol(Rol.USER);
        usuario.setZona(Zona.SINDETERMINAR);
        usuarioRepositorio.save(usuario);
    }

    @Transactional(rollbackOn = Exception.class)
    public void modificarClave(String idUsuario, String clave, String clave2) throws ErrorServicio {
        Usuario usuario = validarId(idUsuario);
        String claveEncriptada = new BCryptPasswordEncoder().encode(validarClaves(clave, clave2));
        usuario.setClave(claveEncriptada);
        usuarioRepositorio.save(usuario);
    }

    // PENDIENTE > > > CONECTAR CON SERVICIO DE ARCHIVOS PARA CARGAR LA FOTO
    @Transactional(rollbackOn = Exception.class)
    public void modificarDatos(Usuario usuarioOrigen, MultipartFile archivo) throws ErrorServicio {
        Usuario usuarioDestino = validarId(usuarioOrigen.getId());
        usuarioDestino = validarNombreApellidoNacimiento(usuarioDestino, usuarioOrigen.getNombre(), usuarioOrigen.getApellido(), usuarioOrigen.getNacimiento());
        usuarioDestino.setZona(usuarioOrigen.getZona());
        //usuarioDestino = validarZona(usuarioDestino, idZona);

        String idFoto = null;
        if (usuarioDestino.getFoto() != null) {
            idFoto = usuarioDestino.getFoto().getId();
        }
        // ACA IRIA CARGA DE FOTO
//        usuarioDestino.setFoto(archivoServicio.actualizar(idFoto, archivo));

        usuarioRepositorio.save(usuarioDestino);
    }

//    @Transactional(rollbackOn = Exception.class)
//    public void agregarReferencia(String idUsuario, Referencia referencia) throws ErrorServicio {
//        Usuario usuario = validarId(idUsuario);
//        List<Referencia> referencias = usuario.getReferencias();
//
//        if (referencias == null) {
//            referencias = new ArrayList();
//        }
//
//        int count = 0;
//        for (Referencia aux : referencias) {
//            if (referencia.getId() == aux.getId()) {
//                aux = referencia;
//                count++;
//            }
//        }
//        if (count == 0) {
//            referencias.add(referencia);
//        }
//        if (count > 1) {
//            throw new ErrorServicio("Error de sistema. Se intento guardar referencias con id duplicado");
//        }
//        usuario.setReferencias(referencias);
//        usuarioRepositorio.save(usuario);
//    }

    @Transactional(rollbackOn = Exception.class)
    public void CargarOQuitarDinero(String idUsuario, Double monto) throws ErrorServicio {
        Usuario usuario = validarId(idUsuario);
        usuario.setDineroEnCuenta(usuario.getDineroEnCuenta() + monto);
        if (usuario.getDineroEnCuenta() < 0) {
            throw new ErrorServicio("No hay saldo suficiente para realizar la operacion.");
        }
        usuarioRepositorio.save(usuario);
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

        int contadorPuntos = 0;
        int contadorArroba = 0;
        for (int i = 0; i < mail.length(); i++) {
            if (contadorArroba > 1) {
                throw new ErrorServicio("Debe ingresar un mail válido.");
            }
            if (mail.substring(i, i + 1).equals("@")) {
                contadorArroba++;
                for (int j = i; j < mail.length(); j++) {
                    if (mail.substring(j, j + 1).equals(".")) {
                        contadorPuntos++;
                    }
                }
            }
        }
        if (contadorArroba == 0 || contadorPuntos == 0) {
            throw new ErrorServicio("Debe ingresar un mail válido 2.");
        }

        for (int i = 0; i < mail.length(); i++) {
            if (mail.substring(i).equals("@")) {
                contadorPuntos++;
            }
        }
        if (contadorArroba > 1) {
            throw new ErrorServicio("Debe ingresar un mail válido.");
        }

        if (usuarioRepositorio.buscarMailEqual(mail.toLowerCase()) != null && usuarioRepositorio.buscarMailEqual(mail.toLowerCase()).getId() != id) {
            throw new ErrorServicio("Ya existe un usuario con el mail seleccionado.");
        }
        return mail.toLowerCase();
    }

    public String validarClaves(String clave, String clave2) throws ErrorServicio {
        if (clave == null || clave.trim().isEmpty() || clave.trim().length() < 4) {
            throw new ErrorServicio("La clave debe tener 4 o más carácteres.");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no coinciden.");
        }
        return clave;
    }

    public Usuario validarZona(Usuario usuario, String idZona) throws ErrorServicio {
        int count = 0;
        for (Zona aux : Zona.values()) {
            if (aux.getNombreCiudad().equals(idZona)) {
                usuario.setZona(aux);
                count++;
            }
        }
        if (count != 1) {
            throw new ErrorServicio("Error de sistema. No se pudo guardar la zona correctamente.");
        }

        return usuario;
    }

    public Usuario validarNombreApellidoNacimiento(Usuario usuario, String nombre, String apellido, LocalDate nacimiento) throws ErrorServicio {
        // VALIDACIONES
        if (nombre.toLowerCase() == null || nombre.isEmpty() || nombre.trim() == "") {
            throw new ErrorServicio("Debe ingresar un nombre válido.");
        }

        if (apellido.toLowerCase() == null || apellido.isEmpty() || apellido.trim() == "") {
            throw new ErrorServicio("Debe ingresar un apellido válido.");
        }

        // LocalDate nacimiento = nacimiento1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (nacimiento == null || nacimiento.toString().isEmpty() || nacimiento.isAfter(LocalDate.now())) {
            throw new ErrorServicio("Debe ingresar un año válido.");
        }

        if (nacimiento.plusYears(18l).isAfter(LocalDate.now())) {
            throw new ErrorServicio("Debe tener mas de 18 años.");
        }

        if (nacimiento.plusYears(100l).isBefore(LocalDate.now())) {
            throw new ErrorServicio("Su edad no puede superar los 100 años...");
        }

        // CARGA A OBJETO
        if (usuario.getId() != null) {
            usuario = validarId(usuario.getId());
        }

        char[] arr1 = nombre.toLowerCase().toCharArray();
        arr1[0] = Character.toUpperCase(arr1[0]);
        nombre = String.valueOf(arr1);
        usuario.setNombre(nombre);

        String apellido2 = apellido.substring(0, 1).toUpperCase().concat(apellido.substring(1, (apellido.length())).toLowerCase());
        usuario.setApellido(apellido2);

        usuario.setNacimiento(nacimiento);
        return usuario;
    }

    public void validarClaveVieja(String idsesion, String clavevieja) throws ErrorServicio {
        String claveok = validarId(idsesion).getClave();
        if (!new BCryptPasswordEncoder().matches(clavevieja, claveok)) {
            throw new ErrorServicio("La clave anterior no es correcta");
        }
    }

    public Usuario buscarMailEqual(String mail) {
        return usuarioRepositorio.buscarMailEqual(mail);
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

        // guardado del objeto usuario en la sesion
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("sesionusuario", usuario);

        return new User(usuario.getMail(), usuario.getClave(), permissions);
    }

    public String returnIdSession() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String pathmail = "";
        if (principal instanceof UserDetails) {
            pathmail = ((UserDetails) principal).getUsername();
        } else {
            pathmail = principal.toString();
        }
        String idsesion = buscarMailEqual(pathmail).getId();
        return idsesion;
    }

    public List<Usuario> findAll() {
        return usuarioRepositorio.findAll();
    }
    
    
    
    
    
    
    public void asignarReferencia (Referencia referencia) throws ErrorServicio{
        
        Usuario usuario = validarId(returnIdSession());
        usuario.getReferencias().add(referencia);
        guardar(usuario);
        
    }

}
