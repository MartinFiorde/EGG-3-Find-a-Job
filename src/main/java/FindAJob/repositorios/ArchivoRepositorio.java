package FindAJob.repositorios;

import FindAJob.entidades.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivoRepositorio extends JpaRepository<Archivo, String>{
        
}
