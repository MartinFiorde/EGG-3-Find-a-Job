package FindAJob.entidades;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Referencia {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne
    private Profesion profesion;

    @OneToMany
    private List<Archivo> archivos;

    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioProfesion;

    public Date getInicioProfesion() {
        return inicioProfesion;
    }

    public void setInicioProfesion(Date inicioProfesion) {
        this.inicioProfesion = inicioProfesion;
    }

    // casilla donde el usuario describe la experiencia y conocimientos que tiene para realizar la profeción
    private String experiencia;

    // casilla donde el usuario describe los elementos de trabajo con los que cuenta para realizar la profeción
    private String herramientas;

    private Integer cantidadTrabajosFinalizados;

    private Integer cantidadValoraciones;

    private Integer puntosValoracionAcumulados;
    
    private Boolean alta;

    //
    public Referencia() {
    }

    //
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Profesion getProfesion() {
        return profesion;
    }

    public void setProfesion(Profesion profesion) {
        this.profesion = profesion;
    }

    public List<Archivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<Archivo> archivos) {
        this.archivos = archivos;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getHerramientas() {
        return herramientas;
    }

    public void setHerramientas(String herramientas) {
        this.herramientas = herramientas;
    }

    public Integer getCantidadTrabajosFinalizados() {
        return cantidadTrabajosFinalizados;
    }

    public void setCantidadTrabajosFinalizados(Integer cantidadTrabajosFinalizados) {
        this.cantidadTrabajosFinalizados = cantidadTrabajosFinalizados;
    }

    public Integer getCantidadValoraciones() {
        return cantidadValoraciones;
    }

    public void setCantidadValoraciones(Integer cantidadValoraciones) {
        this.cantidadValoraciones = cantidadValoraciones;
    }

    public Integer getPuntosValoracionAcumulados() {
        return puntosValoracionAcumulados;
    }

    public void setPuntosValoracionAcumulados(Integer puntosValoracionAcumulados) {
        this.puntosValoracionAcumulados = puntosValoracionAcumulados;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }
    
    

}
