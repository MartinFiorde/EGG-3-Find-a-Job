package FindAJob.repositorios;

import FindAJob.entidades.Posteo;
import FindAJob.enums.Rubro;
import FindAJob.enums.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PosteoRepositorio extends JpaRepository<Posteo, String> {

    @Query("SELECT a FROM Posteo a WHERE a.status = :statusq AND a.cliente.id = :idclienteq")
    public List<Posteo> buscarPostsClienteSegunStatus(@Param("statusq") Status status, @Param("idclienteq") String idCliente);
    
//    @Query(value = "SELECT a FROM Posteo a WHERE a.rubro = :rubroq AND a.status = :statusq", nativeQuery = true)
//    public List<Posteo> buscarSolicitudesRubro(@Param("rubroq") Rubro rubro, @Param("statusq") Status status);
    
    @Query(value = "SELECT a FROM Posteo a WHERE a.status = :statusq AND a.trabajador.id = :idtrabajadorq")
    public List<Posteo> buscarPostsTrabajadorSegunStatus(@Param("statusq") Status status, @Param("idtrabajadorq") String idTrabajador);
    
}
