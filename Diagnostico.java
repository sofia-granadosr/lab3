public class Diagnostico extends OrdenServicio {

    private static final double HORAS_MAXIMAS = 1.5;

    public Diagnostico(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (horasEstimadas > HORAS_MAXIMAS) {
            throw new IllegalArgumentException("Un diagnóstico no puede superar 1.5 horas estimadas.");
        }
    }

    @Override
    public double calcularCosto() {
        if (getZona() == Zona.METROPOLITANA) {
            return 150;
        }
        return 225;
    }

    @Override
    public int getDiasAtencion() {
        if (getZona() == Zona.METROPOLITANA) {
            return 1;
        }
        return 2;
    }

    @Override
    public int getVisitasPermitidas() {
        return 3;
    }

    @Override
    public String getCategoria() {
        return "Diagnóstico";
    }
}
