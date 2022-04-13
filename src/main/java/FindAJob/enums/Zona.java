package FindAJob.enums;

public enum Zona {
    SINDETERMINAR("Sin determinar"), BUENOSAIRES("Buenos Aires"), CORDOBA("Córdoba"), SANTAFE("Santa Fé"), CAPITALFEDERAL("Capital Federal"),
    MENDOZA("Mendoza"), TUCUMAN("Tucumán"), SALTA("Salta"), ENTRERIOS("Entre Ríos"), MISIONES("Misiones"), CHACO("Chaco"),
    CORRIENTES("Corrientes"), SANTIAGODELESTERO("Santiago Del Estero"), SANJUAN("San Juan"),
    JUJUY("Jujuy"), RIONEGRO("Río Negro"), NEUQUEN("Neuquén"), CHUBUT("Chubut"), FORMOSA("Formosa"),
    SANLUIS("San Luis"), CATAMARCA("Catamarca"), LARIOJA("La Rioja"), SANTACRUZ("Santa Cruz"), LAPAMPA("La Pampa"), TIERRADELFUEGO("Tierra Del Fuego");
    
    private String nombreCiudad;

    private Zona(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

}
