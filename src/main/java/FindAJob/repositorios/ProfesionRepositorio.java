package FindAJob.repositorios;

import FindAJob.entidades.Profesion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionRepositorio extends JpaRepository<Profesion, String> {


// @Query("SELECT l FROM profesion l WHERE l.rubro LIKE %rubro%")
// public List<Profesion> buscarSubtipoPorRubro (@Param ("rubro") String rubro);    
}
