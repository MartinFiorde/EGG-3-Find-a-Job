package FindAJob.servicios;

import FindAJob.entidades.Profesion;
import FindAJob.enums.Rubro;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.ProfesionRepositorio;
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
    public Profesion entregarUnaProfesionAleatoriaSegunRubro(String rubro){
        return profesionRepositorio.filtrarPorRubro(Rubro.valueOf(rubro)).get(0);
    }

    public Profesion buscarPorId(String id) throws ErrorServicio {
        Optional<Profesion> opProfesion = profesionRepositorio.findById(id);

        if (opProfesion.isPresent()) {
            Profesion profesion = opProfesion.get();
            return profesion;
        } else {
            throw new ErrorServicio("No se encontro la profesion");
        }
    }

    @Transactional
    public void guardarProfesion(String NombreProfesion) {

    }


    
    public String asignarSubtipo (String id) throws ErrorServicio{
    return null;
    }
    public void asignarRubro() {

    }

   


    public Profesion validarId(String id) throws ErrorServicio {
        Optional<Profesion> res = profesionRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro la profesion solicitada.");
        }
        return res.get();
    }


}
