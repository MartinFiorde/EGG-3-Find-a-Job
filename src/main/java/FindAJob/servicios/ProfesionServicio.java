package FindAJob.servicios;

import FindAJob.entidades.Profesion;
import FindAJob.enums.Rubro;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.ProfesionRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesionServicio {

    private final ProfesionRepositorio profesionRepositorio;

    @Autowired
    public ProfesionServicio(ProfesionRepositorio profesionRepositorio) {
        this.profesionRepositorio = profesionRepositorio;
    }
    
    
    
    //
    
    
    
    public Profesion buscarPorId (String id) throws ErrorServicio{        
        Optional<Profesion> opProfesion = profesionRepositorio.findById(id);

        if(opProfesion.isPresent()){
            Profesion profesion = opProfesion.get();
            return profesion;
        } 
        else{
            throw new ErrorServicio("No se encontro la profesion");
        }
    }
    
    @Transactional
    public void guardarProfesion(String NombreProfesion){        
        
    }
    
    public void asignarRubro(){
        
    }
        
//    public List<Profesion> buscarSubTipoPorRubro(String profesion){ 
//       //Devuelve lista subrubros segun el rubro que llega de la vista, no necesita valicion.
//        return profesionRepositorio.buscarSubtipoPorRubro(profesion); 
//    }
    
    
    public String asignarSubtipo (String id) throws ErrorServicio{
        //llega el id del rubro(con el subrubro elegido) que se elegio en la vista
        Profesion profesion = buscarPorId(id);
        String subtipo = profesion.getSubtipo();
        return subtipo;
    }
    
    
}
