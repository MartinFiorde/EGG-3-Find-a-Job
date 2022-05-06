package FindAJob.servicios;

import FindAJob.entidades.Profesion;
import FindAJob.enums.Rubro;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.ProfesionRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
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
    
    public Profesion entregarUnaProfesionAleatoriaSegunTipo(String tipo){
        return profesionRepositorio.filtrarPorTipo(tipo).get(0);
    }
    
    public List<String> devolverTiposFiltradosPorRubro(String rubro){
        List<Profesion> profesiones = profesionRepositorio.filtrarPorRubro(Rubro.valueOf(rubro.trim().toString()));
        TreeSet<String> tipos = new TreeSet();
        for (Profesion aux : profesiones) {
            tipos.add(aux.getTipo());
        }
        List<String> tipos2 = new ArrayList(tipos);
        return tipos2;
    }

    public List<String> devolverSubtiposFiltradosPorTipo(String tipo){
        List<Profesion> profesiones = profesionRepositorio.filtrarPorTipo(tipo);
        TreeSet<String> subtipos = new TreeSet();
        for (Profesion aux : profesiones) {
            subtipos.add(aux.getSubtipo());
        }
        List<String> subtipos2 = new ArrayList(subtipos);
        return subtipos2;
    }
    
    public Profesion devolverProfesionDelSubtipo(String subtipo){
        return profesionRepositorio.filtrarPorSubtipo(subtipo);
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

    public void validarDuplicado (String subtipo){
        
        List<Profesion> listaProfesion; //= referenciaRepositorio.profesionesUsuario;
    }
}
