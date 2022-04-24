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

    @Query(value = "SELECT a FROM Posteo a WHERE a.status = :statusq AND a.cliente = null"/*, nativeQuery = true*/)
    public List<Posteo> buscarPostsPorStatusB(@Param("statusq") Status idStatus);

    @Query(value = "SELECT a FROM Posteo a WHERE a.profesion.subtipo = :subtipoq AND a.status = :statusq"/*, nativeQuery = true*/)
    public List<Posteo> buscarPostsPorSubtipoYStatusB(@Param("subtipoq") String subtipo, @Param("statusq") Status idStatus);

    @Query(value = "SELECT a FROM Posteo a WHERE a.profesion.tipo = :tipoq AND a.status = :statusq"/*, nativeQuery = true*/)
    public List<Posteo> buscarPostsPorTipoYStatusB(@Param("tipoq") String tipo, @Param("statusq") Status idStatus);

    @Query(value = "SELECT a FROM Posteo a WHERE a.profesion.rubro = :rubroq AND a.status = :statusq"/*, nativeQuery = true*/)
    public List<Posteo> buscarPostsPorRubroYStatusB(@Param("rubroq") Rubro rubro, @Param("statusq") Status idStatus);

    @Query(value = "SELECT a FROM Posteo a WHERE a.status = :statusq AND a.trabajador.id = :idtrabajadorq")
    public List<Posteo> buscarPostsTrabajadorSegunStatus(@Param("statusq") Status idStatus, @Param("idtrabajadorq") String idTrabajador);
    
    @Query(value = "SELECT a FROM Posteo a WHERE a.status = :statusq AND a.trabajador.id = :idtrabajadorq")
    public List<Posteo> buscarTrabajoPorTrabajador(@Param("statusq") Status idStatus, @Param("idtrabajadorq") String idTrabajador);

    @Query(value = "SELECT a FROM Posteo a WHERE a.status = :statusq AND a.cliente.id = :idclienteq")
    public List<Posteo> buscarTrabajoPorCliente(@Param("statusq") Status idStatus, @Param("idclienteq") String idCliente);


}
