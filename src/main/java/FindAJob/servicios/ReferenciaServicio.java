package FindAJob.servicios;

import FindAJob.entidades.Profesion;
import FindAJob.entidades.Referencia;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.ReferenciaRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenciaServicio {             // PROBADO : CARGA CORRECTAMENTE VALORES EN LA BASE DE DATOS

private final ReferenciaRepositorio referenciaRepositorio;
private final ProfesionServicio profesionServicio;
private final ArchivoServicio archivoServicio;

    @Autowired
    public ReferenciaServicio(ReferenciaRepositorio referenciaRepositorio,
            ProfesionServicio profesionServicio,
            ArchivoServicio archivoServicio) {
        this.referenciaRepositorio = referenciaRepositorio;
        this.profesionServicio = profesionServicio;
        this.archivoServicio = archivoServicio;
    }  
    
    
    //
    
    
    @Transactional
    public void guardar (Referencia referencia){
        referencia.setAlta(Boolean.TRUE);       
        referencia.setCantidadValoraciones(0);
        referencia.setCantidadTrabajosFinalizados(0);  
        referencia.setPuntosValoracionAcumulados(0);
        //metodo para asignar profesion
        //metodo para asignar lista de archivos        
        referenciaRepositorio.save(referencia);
    }
    
            
    
    @Transactional 
    public void alta(Referencia referencia) throws ErrorServicio  {
        if(referencia.getAlta() != true){
            referencia.setAlta(true);            
        }else{
            throw new ErrorServicio("La referencia ya fue resubida");
        }        
    } 

    public Referencia buscarPorId (String id) throws ErrorServicio{     
        
        Optional<Referencia> opReferencia = referenciaRepositorio.findById(id);
        
        if(opReferencia.isPresent()){
            Referencia referencia = opReferencia.get();
            return referencia;
        }else{
            throw new ErrorServicio("No se encontró la referencia");
        }
    }

    @Transactional
    public void baja (String id) throws ErrorServicio{
        
        Optional<Referencia> opReferencia = referenciaRepositorio.findById(id);
        
        if(opReferencia.isPresent()){
            Referencia referencia = opReferencia.get();
            referencia.setAlta(false);
            guardar(referencia);
        }else{
            throw new ErrorServicio("No se encontró la referencia");
        }
    }       
    
    @Transactional//REVISAR ANOTACIONES
    public void asignarProfesion (Referencia referencia, Profesion profesion, Date date)throws ErrorServicio{
                 
        
        //Profesion profesion = profesionServicio.buscarPorId(id); 
        referencia.setInicioProfesion(date);
        //referencia.setProfesion(profesion);
        
        
    }
    
    @Transactional//REVISAR ANOTACIONES
    public void asignarArchivos (Referencia referencia, String id)throws ErrorServicio{
        //************************************************************************
        //FRONT END:
        //para llamar este metodo necesario tener archivo previamente creada!!
        
        //crear buscarPorId en archivoServicio con Optional y
        //verificar que no llegue vacio
        //************************************************************************
        
        
        //List<Archivo> archivos =  archivoServicio.buscarPorId(id); 
        //referencia.setArchivo(archivos);
        
        
    }
    
    @Transactional
    public void agregarExperiencia (Referencia referencia, String experiencia) throws ErrorServicio{
        validarExperiencia(experiencia);
        referencia.setExperiencia(experiencia);
    }
    
    @Transactional
    public void agregarHerramientas (Referencia referencia, String herramientas) throws ErrorServicio{
        validarHerramientas(herramientas);
        referencia.setHerramientas(herramientas);
    }
          
    
    
       
    //VALIDACIONES
    
    
    //REVISAR   
    public void validarExperiencia(String experiencia) throws ErrorServicio{
        if(experiencia.length() < 20){  //REVISAR
            throw new ErrorServicio("Debe contener al menos 20 caracteres");
        }           
        if(experiencia.length() > 500){  //REVISAR  
            throw new ErrorServicio("No debe contener mas de 500 caracteres");
        }        
    }
   
    //REVISAR   
    public void validarHerramientas (String herramientas) throws ErrorServicio{
        if(herramientas.length() < 20){  //REVISAR
            throw new ErrorServicio("Debe contener al menos 20 caracteres");
        }           
        if(herramientas.length() > 500){  //REVISAR  
            throw new ErrorServicio("No debe contener mas de 500 caracteres");
        }
    }
    }
