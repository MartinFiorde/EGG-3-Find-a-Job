package FindAJob.repositorios;

import FindAJob.entidades.Posteo;
import FindAJob.enums.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PosteoRepositorio extends JpaRepository<Posteo, String> {

    @Query("SELECT a FROM Posteo a WHERE a.status = :statusq AND a.cliente.id = :idclienteq")
    public List<Posteo> buscarPostsClienteSegunStatus(@Param("statusq") String status, @Param("idclienteq") String idCliente);
    
    @Query(value = "SELECT a FROM Posteo a WHERE a.rubro = :rubroq AND a.status = 'B_SOLICITADO'", nativeQuery = true)
    public List<Posteo> buscarSolicitudesRubro(@Param("rubroq") String rubro);
    
    @Query(value = "SELECT a FROM Posteo a WHERE a.status = :statusq AND a.trabajador.id = :idtrabajadorq", nativeQuery = true)
    public List<Posteo> buscarPostsTrabajadorSegunStatus(@Param("statusq") String status, @Param("idtrabajadorq") String idTrabajador);
    
    @Query(value = "SELECT a FROM Posteo a WHERE a.rubro = :rubroq AND a.status = 'B_OFRECIDO'", nativeQuery = true)
    public List<Posteo> buscarOfertasRubro(@Param("rubroq") String rubro);

}
