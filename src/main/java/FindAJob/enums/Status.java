package FindAJob.enums;

public enum Status {
    A_BORRADOR("Borrador"),
    B_PUBLICADO("Publicado"),
    C_ENPROCESO("En proceso"),
    D_ENTREGADO("Entregado"),
    E_PAGADO("Pagado");

    private String nombreLindo;

    private Status(String nombreLindo) {
        this.nombreLindo = nombreLindo;
    }

    public String getNombreLindo() {
        return nombreLindo;
    }
}
