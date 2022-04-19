package FindAJob.repositorios;

import FindAJob.entidades.Profesion;
import FindAJob.enums.Rubro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionRepositorio extends JpaRepository<Profesion, String> {


    @Query(value = "SELECT a FROM Profesion a WHERE a.rubro LIKE :rubro")
    public List<Profesion> filtrarPorRubro(@Param("rubro") Rubro rubro);

    @Query(value = "SELECT a FROM Profesion a WHERE a.tipo LIKE :tipo")
    public List<Profesion> filtrarPorTipo(@Param("tipo") String tipo);
    
    @Query(value = "SELECT a FROM Profesion a WHERE a.subtipo LIKE :subtipo")
    public Profesion filtrarPorSubtipo(@Param("subtipo") String subtipo);
    
// @Query("SELECT l FROM profesion l WHERE l.rubro LIKE %rubro%")
// public List<Profesion> buscarSubtipoPorRubro (@Param ("rubro") String rubro);    

}
