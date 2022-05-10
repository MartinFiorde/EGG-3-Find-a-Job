package FindAJob.repositorios;

import FindAJob.entidades.Posteo;
import FindAJob.entidades.Referencia;
import FindAJob.entidades.Usuario;
import FindAJob.enums.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT a FROM Usuario a WHERE a.mail = :mailq")
    public Usuario buscarMailEqual(@Param("mailq") String mail);

    @Query("SELECT a FROM Usuario a WHERE a.mail LIKE %:mailq%")
    public List<Usuario> buscarMailsLike(@Param("mailq") String mail); 
   
}
