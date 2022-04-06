package FindAJob.servicios;

import FindAJob.entidades.Referencia;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.ReferenciaRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenciaServicio {

private final ReferenciaRepositorio referenciaRepositorio;

    @Autowired
    public ReferenciaServicio(ReferenciaRepositorio referenciaRepositorio) {
        this.referenciaRepositorio = referenciaRepositorio;
    }
    
    
    @Transactional
    public void guardar (Referencia referencia){
        referenciaRepositorio.save(referencia);
    }
    
    public void alta(Referencia referencia){
        referencia.setAlta(true);
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
   
    
}
