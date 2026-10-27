import java.util.ArrayList;

public class Jornada {

    private int numero;
    private Cuadrilla cuadrilla;
    private ArrayList<OrdenServicio> ordenes;
    private ArrayList<OrdenServicio> sinResultado;
    private EstadoJornada estado;

    public Jornada(int numero, Cuadrilla cuadrilla) {
        cuadrilla.ocupar();
        this.numero = numero;
        this.cuadrilla = cuadrilla;
        this.ordenes = new ArrayList<>();
        this.sinResultado = new ArrayList<>();
        this.estado = EstadoJornada.EN_PREPARACION;
    }

    public double getHorasAsignadas() {
        double total = 0;
        for (OrdenServicio orden : ordenes) {
            total = total + orden.getHorasEstimadas();
        }
        return total;
    }

    public double getCapacidadDisponible() {
        return cuadrilla.getCapacidadHoras() - getHorasAsignadas();
    }

    public void asignarOrden(OrdenServicio orden) {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new IllegalStateException("La jornada " + numero + " no está en preparación (estado: " + estado + ").");
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("La orden " + orden.getCodigo() + " no está pendiente (estado: "
                    + orden.getEstado() + "), no puede asignarse.");
        }
        if (!orden.puedeAtenderCuadrilla(cuadrilla)) {
            throw new IllegalArgumentException("La orden " + orden.getCodigo() + " no es compatible con la cuadrilla "
                    + cuadrilla.getCodigo() + " (requiere certificación en refrigeración).");
        }
        double nuevoTotal = getHorasAsignadas() + orden.getHorasEstimadas();
        if (nuevoTotal > cuadrilla.getCapacidadHoras() + 0.000001) {
            throw new IllegalArgumentException("La orden " + orden.getCodigo() + " excede la capacidad de la cuadrilla "
                    + cuadrilla.getCodigo() + ". Disponible: " + Formato.numero(getCapacidadDisponible())
                    + " horas, requiere: " + Formato.numero(orden.getHorasEstimadas()) + " horas.");
        }
        orden.marcarAsignada();
        ordenes.add(orden);
    }

    public void retirarOrden(String codigoOrden) {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new IllegalStateException("La jornada " + numero + " no está en preparación (estado: " + estado + ").");
        }
        OrdenServicio encontrada = null;
        for (OrdenServicio orden : ordenes) {
            if (orden.getCodigo().equals(codigoOrden)) {
                encontrada = orden;
            }
        }
        if (encontrada == null) {
            throw new IllegalArgumentException("La orden " + codigoOrden + " no está asignada a la jornada " + numero + ".");
        }
        encontrada.retirarAsignacion();
        ordenes.remove(encontrada);
    }

    public void iniciar() {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new IllegalStateException("La jornada " + numero + " no está en preparación (estado: " + estado + ").");
        }
        if (ordenes.isEmpty()) {
            throw new IllegalStateException("No se puede iniciar una jornada vacía.");
        }
        for (OrdenServicio orden : ordenes) {
            orden.iniciarVisita();
            sinResultado.add(orden);
        }
        estado = EstadoJornada.EN_ATENCION;
    }

    public void registrarResultado(String codigoOrden, boolean completada) {
        if (estado != EstadoJornada.EN_ATENCION) {
            throw new IllegalStateException("La jornada " + numero + " no está en atención (estado: " + estado + ").");
        }
        OrdenServicio encontrada = null;
        for (OrdenServicio orden : sinResultado) {
            if (orden.getCodigo().equals(codigoOrden)) {
                encontrada = orden;
            }
        }
        if (encontrada == null) {
            throw new IllegalArgumentException("La orden " + codigoOrden + " no está esperando resultado en la jornada "
                    + numero + ".");
        }
        if (completada) {
            encontrada.completar();
        } else {
            encontrada.fallarVisita();
        }
        sinResultado.remove(encontrada);
        if (sinResultado.isEmpty()) {
            estado = EstadoJornada.CERRADA;
            cuadrilla.liberar();
        }
    }

    public int getNumero() {
        return numero;
    }

    public Cuadrilla getCuadrilla() {
        return cuadrilla;
    }

    public EstadoJornada getEstado() {
        return estado;
    }

    public ArrayList<OrdenServicio> getOrdenes() {
        return ordenes;
    }

    @Override
    public String toString() {
        String codigos = "";
        for (OrdenServicio orden : ordenes) {
            codigos = codigos + orden.getCodigo() + " ";
        }
        return "Jornada " + numero + " | Cuadrilla: " + cuadrilla.getCodigo() + " | Estado: " + estado
                + " | Horas asignadas: " + Formato.numero(getHorasAsignadas())
                + " | Capacidad disponible: " + Formato.numero(getCapacidadDisponible())
                + " | Órdenes: " + codigos;
    }
}
