public class InstalacionElectricaCertificada extends Instalacion {

    private double valorMateriales;

    public InstalacionElectricaCertificada(String codigo, String cliente, String direccion, double horasEstimadas,
            Zona zona, int puntos, double valorMateriales) {
        super(codigo, cliente, direccion, horasEstimadas, zona, puntos);
        if (!(valorMateriales > 0)) {
            throw new IllegalArgumentException("El valor de los materiales debe ser mayor que 0.");
        }
        this.valorMateriales = valorMateriales;
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 120 + valorMateriales * 0.05;
    }

    @Override
    public int getDiasAtencion() {
        return super.getDiasAtencion() + 2;
    }

    @Override
    public int getVisitasPermitidas() {
        return 2;
    }

    @Override
    public String getCategoria() {
        return "Instalación eléctrica certificada";
    }

    public double getValorMateriales() {
        return valorMateriales;
    }

    @Override
    public String toString() {
        return super.toString() + " | Materiales: " + Formato.monto(valorMateriales);
    }
}
