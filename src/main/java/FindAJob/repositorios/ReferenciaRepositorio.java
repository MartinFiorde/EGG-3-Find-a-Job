package FindAJob.repositorios;

import FindAJob.entidades.Posteo;
import FindAJob.entidades.Referencia;
import FindAJob.enums.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenciaRepositorio extends JpaRepository<Referencia, String> {

    @Query("SELECT a FROM Referencia a WHERE a.alta = TRUE")
    public List<Referencia> buscarRefAlta();
    
}
