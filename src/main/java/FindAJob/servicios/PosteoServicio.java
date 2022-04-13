package FindAJob.servicios;

import FindAJob.entidades.Posteo;
import FindAJob.repositorios.PosteoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PosteoServicio {

private final PosteoRepositorio posteoRepositorio;
private final ReferenciaServicio referenciaServicio;

    
    @Autowired   
    public PosteoServicio(PosteoRepositorio posteoRepositorio, ReferenciaServicio referenciaServicio) {
        this.posteoRepositorio = posteoRepositorio;
        this.referenciaServicio = referenciaServicio;
    }

    
    
    //
    
    
    public void  cargarPosteo (Posteo posteo){
        //VALIDACIONES         
        posteoRepositorio.save(posteo);
    }
    
    
    
}
