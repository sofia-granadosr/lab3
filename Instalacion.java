public class Instalacion extends OrdenServicio {

    private int puntos;

    public Instalacion(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int puntos) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (puntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser un entero positivo.");
        }
        this.puntos = puntos;
    }

    public double getHorasPorPuntos() {
        return puntos * 0.75;
    }

    public double getHorasFacturables() {
        double mayor = Math.max(getHorasEstimadas(), getHorasPorPuntos());
        return Math.ceil(mayor);
    }

    @Override
    public double calcularCosto() {
        return tarifaBase() + getHorasFacturables() * 90;
    }

    @Override
    public int getDiasAtencion() {
        if (getZona() == Zona.METROPOLITANA) {
            return 3;
        }
        return 5;
    }

    @Override
    public int getVisitasPermitidas() {
        return 3;
    }

    @Override
    public String getCategoria() {
        return "Instalación";
    }

    public int getPuntos() {
        return puntos;
    }

    @Override
    public String toString() {
        return super.toString() + " | Puntos: " + puntos;
    }
}
