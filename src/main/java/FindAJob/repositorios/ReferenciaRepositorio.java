package FindAJob.repositorios;
import FindAJob.entidades.Referencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenciaRepositorio extends JpaRepository<Referencia, String> {


    
}
