package FindAJob.entidades;

import FindAJob.enums.Status;
import FindAJob.enums.Zona;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Posteo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @OneToOne
    private Profesion profesion;
    @OneToOne
    private Usuario cliente;
    @OneToOne
    private Usuario trabajador;
    @OneToOne
    private Referencia referencia;
    @Enumerated(EnumType.STRING)
    private Zona zona;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Double precio;
    private Double DineroGuardado;
    private String descripcionOferta;
    private String descripcionSolicitud;
    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate entregaTrabajo;
    @ElementCollection // 1
    @CollectionTable(name = "chats", joinColumns = @JoinColumn(name = "id")) // 2
    @Column(name = "chats") // 3
    private List<String> chats;
    private String reclamoCliente;
    private String reclamoTrabajador;
    private String resolucionAdministrador;

    //
    public Posteo() {
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

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Usuario trabajador) {
        this.trabajador = trabajador;
    }

    public Referencia getReferencia() {
        return referencia;
    }

    public void setReferencia(Referencia referencia) {
        this.referencia = referencia;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getDineroGuardado() {
        return DineroGuardado;
    }

    public void setDineroGuardado(Double DineroGuardado) {
        this.DineroGuardado = DineroGuardado;
    }

    public Date getAlta() {
        return alta;
    }

    public void setAlta(Date alta) {
        this.alta = alta;
    }

    public Date getBaja() {
        return baja;
    }

    public void setBaja(Date baja) {
        this.baja = baja;
    }

    public LocalDate getEntregaTrabajo() {
        return entregaTrabajo;
    }

    public void setEntregaTrabajo(LocalDate entregaTrabajo) {
        this.entregaTrabajo = entregaTrabajo;
    }

    public List<String> getChats() {
        return chats;
    }

    public void setChats(List<String> chats) {
        this.chats = chats;
    }

    public String getReclamoCliente() {
        return reclamoCliente;
    }

    public void setReclamoCliente(String reclamoCliente) {
        this.reclamoCliente = reclamoCliente;
    }

    public String getReclamoTrabajador() {
        return reclamoTrabajador;
    }

    public void setReclamoTrabajador(String reclamoTrabajador) {
        this.reclamoTrabajador = reclamoTrabajador;
    }

    public String getResolucionAdministrador() {
        return resolucionAdministrador;
    }

    public void setResolucionAdministrador(String resolucionAdministrador) {
        this.resolucionAdministrador = resolucionAdministrador;
    }

    public String getDescripcionOferta() {
        return descripcionOferta;
    }

    public void setDescripcionOferta(String descripcionOferta) {
        this.descripcionOferta = descripcionOferta;
    }

    public String getDescripcionSolicitud() {
        return descripcionSolicitud;
    }

    public void setDescripcionSolicitud(String descripcionSolicitud) {
        this.descripcionSolicitud = descripcionSolicitud;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Posteo{" + "id=" + id + ", profesion=" + profesion + ", cliente=" + cliente + ", trabajador=" + trabajador + ", referencia=" + referencia + ", zona=" + zona + ", status=" + status + ", precio=" + precio + ", DineroGuardado=" + DineroGuardado + ", descripcionOferta=" + descripcionOferta + ", descripcionSolicitud=" + descripcionSolicitud + ", alta=" + alta + ", baja=" + baja + ", entregaTrabajo=" + entregaTrabajo + ", chats=" + chats + ", reclamoCliente=" + reclamoCliente + ", reclamoTrabajador=" + reclamoTrabajador + ", resolucionAdministrador=" + resolucionAdministrador + '}';
    }

}
