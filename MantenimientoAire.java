public class MantenimientoAire extends OrdenServicio {

    private int btu;

    public MantenimientoAire(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int btu) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException("El mantenimiento de aire acondicionado solo se acepta en la zona metropolitana.");
        }
        if (btu < 9000 || btu > 60000) {
            throw new IllegalArgumentException("La capacidad debe estar entre 9000 y 60000 BTU.");
        }
        this.btu = btu;
    }

    @Override
    public double calcularCosto() {
        return tarifaBase() + getHorasEstimadas() * 110 + 80;
    }

    @Override
    public int getDiasAtencion() {
        return 2;
    }

    @Override
    public int getVisitasPermitidas() {
        return 1;
    }

    @Override
    public String getCategoria() {
        return "Mantenimiento de aire acondicionado";
    }

    @Override
    public boolean puedeAtenderCuadrilla(Cuadrilla cuadrilla) {
        return cuadrilla.isCertificadaRefrigeracion();
    }

    public int getBtu() {
        return btu;
    }

    @Override
    public String toString() {
        return super.toString() + " | BTU: " + btu;
    }
}
