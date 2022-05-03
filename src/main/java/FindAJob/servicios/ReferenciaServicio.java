package FindAJob.servicios;

import FindAJob.entidades.Archivo;
import FindAJob.entidades.Posteo;
import FindAJob.entidades.Profesion;
import FindAJob.entidades.Referencia;
import FindAJob.entidades.Usuario;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.ReferenciaRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReferenciaServicio {             // PROBADO : CARGA CORRECTAMENTE VALORES EN LA BASE DE DATOS

    private final ReferenciaRepositorio referenciaRepositorio;
    private final ProfesionServicio profesionServicio;
    private final ArchivoServicio archivoServicio;
    private final UsuarioServicio usuarioServicio;

    @Autowired
    public ReferenciaServicio(ReferenciaRepositorio referenciaRepositorio,
            ProfesionServicio profesionServicio,
            ArchivoServicio archivoServicio,
            UsuarioServicio usuarioServicio) {
        this.referenciaRepositorio = referenciaRepositorio;
        this.profesionServicio = profesionServicio;
        this.archivoServicio = archivoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    //
    @Transactional(rollbackOn = Exception.class)
    public Referencia guardar(Referencia referencia) throws ErrorServicio {
        
        validarExperiencia(referencia.getExperiencia());
        validarHerramientas(referencia.getHerramientas());
        validarFecha(referencia.getInicioProfesion());
        //metodo para asignar profesion        
        System.out.println("guardado");
        return referenciaRepositorio.save(referencia);

    }

    @Transactional(rollbackOn = Exception.class)
    public void alta(Referencia referencia) throws ErrorServicio {
        if (referencia.getAlta() != true) {
            referencia.setAlta(true);
        } else {
            throw new ErrorServicio("La referencia ya fue resubida");
        }
    }
    
    @Transactional
    public Referencia buscarPorId(String id) throws ErrorServicio {

        Optional<Referencia> opReferencia = referenciaRepositorio.findById(id);

        if (opReferencia.isPresent()) {
            Referencia referencia = opReferencia.get();           
            return referencia;
        } else {            
            throw new ErrorServicio("No se encontró la referencia");
            
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public Referencia baja(String id) throws ErrorServicio {

        Optional<Referencia> opReferencia = referenciaRepositorio.findById(id);

        if (opReferencia.isPresent()) {
            Referencia referencia = opReferencia.get();
            referencia.setAlta(false);
            return referencia;
        } else {
            throw new ErrorServicio("No se encontró la referencia");
        }
    }

    public Referencia inicializarNuevaReferencia(Referencia referencia) {
        referencia.setAlta(Boolean.TRUE);
        referencia.setCantidadValoraciones(0);
        referencia.setCantidadTrabajosFinalizados(0);
        referencia.setPuntosValoracionAcumulados(0);
        referencia.setArchivos(new ArrayList<>());
        return referencia;
    }

    @Transactional(rollbackOn = Exception.class)//REVISAR ANOTACIONES
    public void asignarProfesion(Referencia referencia, Profesion profesion, Date date) throws ErrorServicio {

        //Profesion profesion = profesionServicio.buscarPorId(id);        
        //referencia.setProfesion(profesion);

    }

    @Transactional(rollbackOn = Exception.class)
    public void agregarExperiencia(Referencia referencia, String experiencia) throws ErrorServicio {
        validarExperiencia(experiencia);
        referencia.setExperiencia(experiencia);
    }

    @Transactional(rollbackOn = Exception.class)
    public void agregarHerramientas(Referencia referencia, String herramientas) throws ErrorServicio {
        validarHerramientas(herramientas);
        referencia.setHerramientas(herramientas);
    }

    //VALIDACIONES
    public void validarExperiencia(String experiencia) throws ErrorServicio {
        if (experiencia.length() < 20) {  //REVISAR
            throw new ErrorServicio("Debe contener al menos 20 caracteres");
        }
        if (experiencia.length() > 500) {  //REVISAR  
            throw new ErrorServicio("No debe contener mas de 500 caracteres");
        }
    }

    public void validarHerramientas(String herramientas) throws ErrorServicio {
        if (herramientas.length() < 20) {
            throw new ErrorServicio("Debe contener al menos 20 caracteres");
        }
        if (herramientas.length() > 500) {
            throw new ErrorServicio("No debe contener mas de 500 caracteres");
        }
    }

    public Referencia buscarReferenciaParaPosteo(Posteo posteo, String idProfPost) throws ErrorServicio {
        Referencia referencia = null;
        int count = 0;
        if (posteo.getTrabajador() != null) {
            for (Referencia aux : posteo.getTrabajador().getReferencias()) {
                if (aux.getProfesion().getId().equals(idProfPost)) {
                    count++;
                    referencia = aux;
                }
            }
        }
        if (count != 1) {
            throw new ErrorServicio("Error de sistema. No se pudo definir la referencia aplicable al posteo.");
        }
        return referencia;
    }

    public Referencia validarId(String id) throws ErrorServicio {
        Optional<Referencia> res = referenciaRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro la referencia solicitada.");
        }
        return res.get();
    }

    public List<Referencia> traerReferenciasUsuarioLogueado() throws ErrorServicio {

        String idUsuarioLogueado = usuarioServicio.returnIdSession();   //traigo id usuario logueado        
        Usuario usuarioLogueado = usuarioServicio.validarId(idUsuarioLogueado);  //a travez del id traigo el usuario   
        
       
        List<Referencia> listaRefAlta = referenciaRepositorio.buscarRefAlta();
        //List<Referencia> listaRefUsuario = usuarioLogueado.getReferencias();   //paso a lista sus referencias (EN ALTA) cargadas

        return listaRefAlta;

    }

    @Transactional(rollbackOn = Exception.class)
    public Referencia asignarArchivo(Referencia referencia, Archivo archivo) throws ErrorServicio {

        
        if (archivo == null) {
            System.out.println("el archivo es nulo");
        } else {
            Archivo a2 = archivoServicio.buscarPorId(archivo.getId());
            List<Archivo> listaArchivo = referencia.getArchivos();  
            System.out.println("archivo a2 " + a2);
            listaArchivo.add(a2);        
            referencia.setArchivos(listaArchivo);
        }        
 
        return referencia;
    }
    
    public void validarFecha(LocalDate fecha) throws ErrorServicio{   
       
        if (fecha == null ) {
            throw new ErrorServicio("Debe ingresar una fecha.");
        }       
        if (fecha.toString().isEmpty()) {
            throw new ErrorServicio("Debe ingresar una fecha.");
        }       
        if (fecha.isAfter(LocalDate.now())) {
            throw new ErrorServicio("La fecha ingresada no es valida.");
        }       
       
    }
    
    public void editarPorId(String id) throws ErrorServicio{
        Optional<Referencia> refOp = referenciaRepositorio.findById(id);
        
        if(!refOp.isPresent()){
            throw new ErrorServicio("No se encontro la referencia");
        }
        
    }

}
