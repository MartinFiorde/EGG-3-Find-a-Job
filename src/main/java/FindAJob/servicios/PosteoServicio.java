package FindAJob.servicios;

import FindAJob.entidades.Posteo;
import FindAJob.entidades.Profesion;
import FindAJob.entidades.Referencia;
import FindAJob.entidades.Usuario;
import FindAJob.enums.Rubro;
import FindAJob.enums.Status;
import FindAJob.enums.Zona;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.PosteoRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class PosteoServicio {

    // INSTANCIAS Y CONSTRUCTOR
    private PosteoRepositorio posteoRepositorio;
    private ReferenciaServicio referenciaServicio;
    private UsuarioServicio usuarioServicio;
    private ProfesionServicio profesionServicio;

    @Autowired
    public PosteoServicio(PosteoRepositorio posteoRepositorio, ReferenciaServicio referenciaServicio, UsuarioServicio usuarioServicio, ProfesionServicio profesionServicio) {
        this.posteoRepositorio = posteoRepositorio;
        this.referenciaServicio = referenciaServicio;
        this.usuarioServicio = usuarioServicio;
        this.profesionServicio = profesionServicio;
    }

    public Posteo validarId(String id) throws ErrorServicio {
        Optional<Posteo> res = posteoRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro el posteo solicitado.");
        }
        return res.get();
    }

    @Transactional(rollbackOn = Exception.class)
    public void escribirChat(String idPosteo, String mensaje) throws ErrorServicio {
        if (mensaje.toLowerCase() == null || mensaje.isEmpty() || mensaje.trim() == "") {
            throw new ErrorServicio("Debe ingresar un mensaje válido");
        }
        if (mensaje.length() > 200) {
            throw new ErrorServicio("Supero el limite de 200 caracteres por mensaje");
        }
        Posteo posteo = validarId(idPosteo);
        String mensajeFinal = (new Date()) + " - " + usuarioServicio.validarId(usuarioServicio.returnIdSession()).getFullName() + ": " + mensaje;
        List<String> chats = posteo.getChats();
        System.out.println("Mensaje: " + mensajeFinal);
        chats.add(mensajeFinal);
        posteo.setChats(chats);
        posteoRepositorio.save(posteo);
    }

    @Transactional(rollbackOn = Exception.class)
    public void BajaA(String id) throws ErrorServicio {
        Posteo posteo = validarId(id);
        crearBorradorA(id, posteo, posteo.getProfesion().getId(), posteo.getZona().toString());
    }

    @Transactional(rollbackOn = Exception.class)
    public void crearBorradorA(String id, Posteo posteoOrigen, String idProfesion, String idZona) throws ErrorServicio {
        // VALIDACION DE CORRELACION ENTRE ESTADOS
        Posteo posteoDestino = new Posteo();
        if (id != null) {
            posteoDestino = validarId(id);
            if (posteoDestino.getStatus() != Status.A_BORRADOR && posteoDestino.getStatus() != Status.B_PUBLICADO) {
                throw new ErrorServicio("La operacion no puede ser realizada sobre este post en su estado actual.");
            }
        }

        if (posteoOrigen.getDescripcionOferta().toLowerCase() == null || posteoOrigen.getDescripcionOferta().isEmpty() || posteoOrigen.getDescripcionOferta().trim() == "") {
            throw new ErrorServicio("Debe ingresar una descripción del servicio ofrecido.");
        }
        if (posteoOrigen.getPrecio() < 0d || posteoOrigen.getPrecio() == null) {
            throw new ErrorServicio("Debe ingresar un precio válido.");
        }
        if (posteoOrigen.getDescripcionOferta() != null) {
            if (posteoOrigen.getDescripcionOferta().isEmpty() || posteoOrigen.getDescripcionOferta().trim() == "") {
                throw new ErrorServicio("Debe ingresar una breve descripción del trabajo ofrecido.");
            }
        }

        posteoDestino.setStatus(Status.A_BORRADOR);
        posteoDestino.setTrabajador(usuarioServicio.validarId(usuarioServicio.returnIdSession()));
        posteoDestino.setDescripcionOferta(posteoOrigen.getDescripcionOferta());
        //posteoDestino.setReferencia(referenciaServicio.buscarReferenciaParaPosteo(posteoDestino, idProfesion));
        posteoDestino.setPrecio(posteoOrigen.getPrecio());
        posteoDestino.setZona(Zona.valueOf(idZona));
        posteoDestino.setProfesion(profesionServicio.validarId(idProfesion));
        posteoDestino.setAlta(null);
        List<String> chats = new ArrayList();
        posteoDestino.setChats(chats);

        posteoRepositorio.save(posteoDestino);
    }

    @Transactional(rollbackOn = Exception.class)
    public void subirPosteoB(String id) throws ErrorServicio {
        Posteo posteo = validarId(id);
        if (posteo.getTrabajador().getId() != usuarioServicio.returnIdSession()) {
            throw new ErrorServicio("Usted no tiene permisos para modificar este post.");
        }
        if (posteo.getStatus() != Status.A_BORRADOR && posteo.getStatus() != Status.B_PUBLICADO) {
            throw new ErrorServicio("La operacion no puede ser realizada sobre este post en su estado actual.");
        }
        
        posteo.setStatus(Status.B_PUBLICADO);
        List<String> chats = new ArrayList();
        chats.add(new Date() + " - " + "Sala de chat pública. Consulta inquietudes o convení términos con el oferente");
        posteo.setReferencia(usuarioServicio.buscarParaPosteo(posteo.getProfesion().getId()));
        posteo.setChats(chats);
        posteo.setAlta(new Date());
        posteoRepositorio.save(posteo);
        System.out.println("1");
    }

    @Transactional(rollbackOn = Exception.class)
    public void solicitarC(String id, String descripcion, LocalDate entregaEstimada, Double dineroGuardado) throws ErrorServicio {
        Posteo posteo = validarId(id);
        System.out.println("id: "+id);
        if (posteo.getTrabajador().getId().equals(usuarioServicio.returnIdSession())) {
            throw new ErrorServicio("No puede contratarse a si mismo.");
        }
        if (posteo.getStatus() != Status.B_PUBLICADO) {
            throw new ErrorServicio("La operacion no puede ser realizada sobre este post en su estado actual.");
        }

        if (entregaEstimada == null) {
            throw new ErrorServicio("Debe ingresar una fecha.");
        }
        if (entregaEstimada.toString().isEmpty()) {
            throw new ErrorServicio("Debe ingresar una fecha.");
        }
        if (entregaEstimada.isBefore(LocalDate.now())) {
            throw new ErrorServicio("La fecha ingresada no es valida.");
        }
        posteo.setStatus(Status.C_ENPROCESO);
        posteo.setCliente(usuarioServicio.validarId(usuarioServicio.returnIdSession()));
        posteo.setDescripcionSolicitud(descripcion);
        posteo.setEntregaTrabajo(entregaEstimada);
        usuarioServicio.CargarOQuitarDinero(posteo.getCliente().getId(), -(dineroGuardado));
        posteo.setDineroGuardado(dineroGuardado);

        //PROBANDO ESTA LINEA
        posteo.getChats().add(new Date() + " - " + "Oferta contratada. Acceso al chat limitado a las partes involucradas");
//        List<String> chats = posteo.getChats();
//        chats.add(new Date() + " - " + "Oferta contratada. Acceso al chat limitado a las partes involucradas");
//        posteo.setChats(chats);

        posteoRepositorio.save(posteo);
    }

    public void entregarD(String id) throws ErrorServicio {
        Posteo posteo = validarId(id);
        if (posteo.getTrabajador().getId() != usuarioServicio.returnIdSession()) {
            throw new ErrorServicio("Usted no tiene permisos para confirmar la entrega.");
        }
        if (posteo.getStatus() != Status.C_ENPROCESO) {
            throw new ErrorServicio("La operacion no puede ser realizada sobre este post en su estado actual.");
        }
        posteo.setStatus(Status.D_ENTREGADO);

        posteoRepositorio.save(posteo);
    }

    public void PagarE(String id) throws ErrorServicio {
        Posteo posteo = validarId(id);
        if (posteo.getCliente().getId() != usuarioServicio.returnIdSession()) {
            throw new ErrorServicio("Usted no tiene permisos para confirmar el pago.");
        }
        if (posteo.getStatus() != Status.D_ENTREGADO) {
            throw new ErrorServicio("La operacion no puede ser realizada sobre este post en su estado actual.");
        }
        posteo.setStatus(Status.E_PAGADO);

        usuarioServicio.CargarOQuitarDinero(posteo.getTrabajador().getId(), +(posteo.getDineroGuardado()));
        posteo.setDineroGuardado(posteo.getDineroGuardado() - posteo.getPrecio());
        posteo.setBaja(new Date());

        posteoRepositorio.save(posteo);
    }

    public List<Posteo> findAll() {
        return posteoRepositorio.findAll();
    }

    public List<Posteo> buscarPostsPorStatusB(String idStatus) {
        return posteoRepositorio.buscarPostsPorStatusB(Status.valueOf(idStatus));
    }

    public List<Posteo> buscarPostsPorSubtipoYStatusB(String idSubtipo, String idStatus) {
        return posteoRepositorio.buscarPostsPorSubtipoYStatusB(idSubtipo, Status.valueOf(idStatus));
    }

    public List<Posteo> buscarPostsPorTipoYStatusB(String idTipo, String idStatus) {
        return posteoRepositorio.buscarPostsPorTipoYStatusB(idTipo, Status.valueOf(idStatus));
    }

    public List<Posteo> buscarPostsPorRubroYStatusB(String idRubro, String idStatus) {
        return posteoRepositorio.buscarPostsPorRubroYStatusB(Rubro.valueOf(idRubro), Status.valueOf(idStatus));
    }

    public List<Posteo> quitarTrabajadorLogeadoDeResultados(List<Posteo> posteosCompletos) {
        List<Posteo> posteosFiltrados = new ArrayList();
        for (Posteo aux : posteosCompletos) {
            if (!usuarioServicio.returnIdSession().equals(aux.getTrabajador().getId())) {
                posteosFiltrados.add(aux);
            }
        }
        return posteosFiltrados;
    }

    public List<Posteo> quitarClienteLogeadoDeResultados(List<Posteo> posteosCompletos) {
        List<Posteo> posteosFiltrados = new ArrayList();
        for (Posteo aux : posteosCompletos) {
            if (!usuarioServicio.returnIdSession().equals(aux.getCliente().getId())) {
                posteosFiltrados.add(aux);
            }
        }
        return posteosFiltrados;
    }

    public List<Posteo> dejarSoloClienteLogeadoDeResultados(List<Posteo> posteosCompletos) {
        List<Posteo> posteosFiltrados = new ArrayList();
        for (Posteo aux : posteosCompletos) {
            if (usuarioServicio.returnIdSession().equals(aux.getCliente().getId())) {
                posteosFiltrados.add(aux);
            }
        }
        return posteosFiltrados;
    }

    public List<Posteo> dejarSoloTrabajadorLogeadoDeResultados(List<Posteo> posteosCompletos) {
        List<Posteo> posteosFiltrados = new ArrayList();
        for (Posteo aux : posteosCompletos) {
            if (usuarioServicio.returnIdSession().equals(aux.getTrabajador().getId())) {
                posteosFiltrados.add(aux);
            }
        }
        return posteosFiltrados;
    }

    public List<Posteo> filtrarListaPorZona(List<Posteo> posteos, String idZona) {
        List<Posteo> filtrados = new ArrayList();
        for (Posteo aux : posteos) {
            if (aux.getZona().equals(Zona.valueOf(idZona))) {
                filtrados.add(aux);
            }
        }
        return filtrados;
    }

    public List<Posteo> buscarTrabajoPorTrabajador(Status idStatus, String idTrabajador) {
        return posteoRepositorio.buscarTrabajoPorTrabajador(idStatus, idTrabajador);
    }

    public List<Posteo> buscarTrabajoPorCliente(Status idStatus, String idCliente) {
        return posteoRepositorio.buscarTrabajoPorCliente(idStatus, idCliente);
    }

}
