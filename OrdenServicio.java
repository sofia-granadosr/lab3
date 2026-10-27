public abstract class OrdenServicio {

    protected static final double CARGO_VISITA_ADICIONAL = 40;

    private String codigo;
    private String cliente;
    private String direccion;
    private double horasEstimadas;
    private Zona zona;
    private EstadoOrden estado;
    private int visitasRealizadas;
    private double montoRegistro;
    private double cargosVisitas;

    public OrdenServicio(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }
        if (!(horasEstimadas > 0)) {
            throw new IllegalArgumentException("Las horas estimadas deben ser mayores que 0.");
        }
        if (zona == null) {
            throw new IllegalArgumentException("La zona no es válida.");
        }
        this.codigo = codigo.trim();
        this.cliente = cliente.trim();
        this.direccion = direccion.trim();
        this.horasEstimadas = horasEstimadas;
        this.zona = zona;
        this.estado = EstadoOrden.PENDIENTE;
        this.visitasRealizadas = 0;
        this.montoRegistro = 0;
        this.cargosVisitas = 0;
    }

    public abstract double calcularCosto();

    public abstract int getDiasAtencion();

    public abstract int getVisitasPermitidas();

    public abstract String getCategoria();

    public boolean puedeAtenderCuadrilla(Cuadrilla cuadrilla) {
        return true;
    }

    protected double tarifaBase() {
        if (zona == Zona.METROPOLITANA) {
            return 100;
        }
        return 175;
    }

    public void cobrarRegistro() {
        montoRegistro = calcularCosto();
    }

    public void marcarAsignada() {
        if (estado != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("La orden " + codigo + " no está pendiente (estado: " + estado + ").");
        }
        estado = EstadoOrden.ASIGNADA;
    }

    public void retirarAsignacion() {
        if (estado != EstadoOrden.ASIGNADA) {
            throw new IllegalStateException("La orden " + codigo + " no está asignada (estado: " + estado + ").");
        }
        estado = EstadoOrden.PENDIENTE;
    }

    public void iniciarVisita() {
        if (estado != EstadoOrden.ASIGNADA) {
            throw new IllegalStateException("La orden " + codigo + " no está asignada (estado: " + estado + ").");
        }
        estado = EstadoOrden.EN_ATENCION;
        visitasRealizadas++;
        if (visitasRealizadas > 1) {
            cargosVisitas = cargosVisitas + CARGO_VISITA_ADICIONAL;
        }
    }

    public void completar() {
        if (estado != EstadoOrden.EN_ATENCION) {
            throw new IllegalStateException("La orden " + codigo + " no está en atención (estado: " + estado + ").");
        }
        estado = EstadoOrden.COMPLETADA;
    }

    public void fallarVisita() {
        if (estado != EstadoOrden.EN_ATENCION) {
            throw new IllegalStateException("La orden " + codigo + " no está en atención (estado: " + estado + ").");
        }
        if (visitasRealizadas >= getVisitasPermitidas()) {
            estado = EstadoOrden.CANCELADA;
        } else {
            estado = EstadoOrden.PENDIENTE;
        }
    }

    public double getIngresoTotal() {
        return montoRegistro + cargosVisitas;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getHorasEstimadas() {
        return horasEstimadas;
    }

    public Zona getZona() {
        return zona;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public int getVisitasRealizadas() {
        return visitasRealizadas;
    }

    public double getMontoRegistro() {
        return montoRegistro;
    }

    public double getCargosVisitas() {
        return cargosVisitas;
    }

    @Override
    public String toString() {
        return codigo + " | " + getCategoria() + " | Cliente: " + cliente + " | Dirección: " + direccion
                + " | Zona: " + zona + " | Horas estimadas: " + Formato.numero(horasEstimadas)
                + " | Estado: " + estado + " | Visitas: " + visitasRealizadas + "/" + getVisitasPermitidas()
                + " | Costo: " + Formato.monto(calcularCosto());
    }
}
