package FindAJob.entidades;

import FindAJob.enums.Rol;
import FindAJob.enums.Zona;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;

    private String apellido;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate nacimiento;

    private String mail;

    private String clave;

    private Double dineroEnCuenta;

    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;

    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;

    private Boolean activo;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    private Zona zona;

    @OneToOne
    private Archivo foto;

    @OneToMany
    private List<Referencia> referencias;

    // CONSTRUCTOR VACIO
    public Usuario() {
    }

    // GETERS Y SETERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Archivo getFoto() {
        return foto;
    }

    public void setFoto(Archivo foto) {
        this.foto = foto;
    }

    public List<Referencia> getReferencias() {
        return referencias;
    }

    public void setReferencias(List<Referencia> referencias) {
        this.referencias = referencias;
    }

    public Double getDineroEnCuenta() {
        return dineroEnCuenta;
    }

    public void setDineroEnCuenta(Double dineroEnCuenta) {
        this.dineroEnCuenta = dineroEnCuenta;
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(LocalDate nacimiento) {
        this.nacimiento = nacimiento;
    }

    // METODOS
    public String getFullName() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", nacimiento=" + nacimiento + ", mail=" + mail + ", clave=" + clave + ", dineroEnCuenta=" + dineroEnCuenta + ", alta=" + alta + ", baja=" + baja + ", activo=" + activo + ", rol=" + rol + ", zona=" + zona + ", foto=" + foto + ", referencias=" + referencias + '}';
    }
    
    
}
