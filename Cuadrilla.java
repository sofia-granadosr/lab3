public class Cuadrilla {

    private String codigo;
    private double capacidadHoras;
    private boolean certificadaRefrigeracion;
    private boolean ocupada;

    public Cuadrilla(String codigo, double capacidadHoras, boolean certificadaRefrigeracion) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la cuadrilla no puede estar vacío.");
        }
        if (!(capacidadHoras > 0)) {
            throw new IllegalArgumentException("La capacidad de horas debe ser mayor que 0.");
        }
        this.codigo = codigo.trim();
        this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
        this.ocupada = false;
    }

    public void ocupar() {
        if (ocupada) {
            throw new IllegalStateException("La cuadrilla " + codigo + " ya tiene una jornada sin cerrar.");
        }
        ocupada = true;
    }

    public void liberar() {
        ocupada = false;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getCapacidadHoras() {
        return capacidadHoras;
    }

    public boolean isCertificadaRefrigeracion() {
        return certificadaRefrigeracion;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    @Override
    public String toString() {
        String certificada = "No";
        if (certificadaRefrigeracion) {
            certificada = "Sí";
        }
        String estado = "Libre";
        if (ocupada) {
            estado = "Con jornada sin cerrar";
        }
        return codigo + " | Capacidad: " + Formato.numero(capacidadHoras) + " horas | Certificada en refrigeración: "
                + certificada + " | " + estado;
    }
}
