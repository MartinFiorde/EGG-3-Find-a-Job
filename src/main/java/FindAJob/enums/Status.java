package FindAJob.enums;

public enum Status {
    SOLICITADO("Solicitado"), OFRECIDO("Ofrecido"), PORCONFIRMAR("Por confirmar"), ENPROCESO("En proceso"),
    ENTREGADO("Entregado"), FINEXITOSO("Fin exitoso"), FINCONFLICTO("Fin con conflicto"),
    CANCELADOEXITO("Cancelado con exito"), CANCELCONFIRM("Cancelación Confirmada"), BORRADOR("Borrador");

    private String nombreLindo;

    private Status(String nombreLindo) {
        this.nombreLindo = nombreLindo;
    }

    public String getNombreLindo() {
        return nombreLindo;
    }

    public void setNombreLindo(String nombreLindo) {
        this.nombreLindo = nombreLindo;
    }
}
