package FindAJob.enums;

public enum Rubro {
    SERVICIOSPROFESIONALES("Servicios profesionales"), GREMIOSADOMICILIO("Gremios a domicilio"),
    TAREASDOMESTICAS("Tareas domesticas"), PROGRAMACIONYTECNOLOGIA("Programacion y tecnologia"),
    DISENIOYMULTIMEDIA("Diseño y multimedia"), SALUDYBELLEZA("Salud y belleza"),
    MARKETINGYGESTORESDEVENTAS("Marketing y gestores de ventas"), ARTEYCULTURA("Arte y cultura"), 
    GASTRONOMIA("Gastronomia");
    private String nombreLindo;

    private Rubro(String nombreLindo) {
        this.nombreLindo = nombreLindo;
    }

    public String getNombreLindo() {
        return nombreLindo;
    }

    public void setNombreLindo(String nombreLindo) {
        this.nombreLindo = nombreLindo;
    }

}
