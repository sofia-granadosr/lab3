import java.util.ArrayList;

public class Empresa {

    private ArrayList<OrdenServicio> ordenes = new ArrayList<>();
    private ArrayList<Cuadrilla> cuadrillas = new ArrayList<>();
    private ArrayList<Jornada> jornadas = new ArrayList<>();
    private int contadorJornadas = 0;

    public OrdenServicio buscarOrden(String codigo) {
        for (OrdenServicio orden : ordenes) {
            if (orden.getCodigo().equals(codigo.trim())) {
                return orden;
            }
        }
        return null;
    }

    public Cuadrilla buscarCuadrilla(String codigo) {
        for (Cuadrilla cuadrilla : cuadrillas) {
            if (cuadrilla.getCodigo().equals(codigo.trim())) {
                return cuadrilla;
            }
        }
        return null;
    }

    public Jornada buscarJornada(int numero) {
        for (Jornada jornada : jornadas) {
            if (jornada.getNumero() == numero) {
                return jornada;
            }
        }
        return null;
    }

    public void validarCodigoOrden(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (buscarOrden(codigo) != null) {
            throw new IllegalArgumentException("Ya existe una orden con el código " + codigo.trim() + ".");
        }
    }

    public void cargarOrdenInicial(OrdenServicio orden) {
        validarCodigoOrden(orden.getCodigo());
        ordenes.add(orden);
    }

    public void registrarOrden(OrdenServicio orden) {
        validarCodigoOrden(orden.getCodigo());
        orden.cobrarRegistro();
        ordenes.add(orden);
    }

    public void registrarCuadrilla(String codigo, double capacidadHoras, boolean certificada) {
        Cuadrilla nueva = new Cuadrilla(codigo, capacidadHoras, certificada);
        if (buscarCuadrilla(nueva.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe una cuadrilla con el código " + nueva.getCodigo() + ".");
        }
        cuadrillas.add(nueva);
    }

    public Jornada crearJornada(String codigoCuadrilla) {
        Cuadrilla cuadrilla = buscarCuadrilla(codigoCuadrilla);
        if (cuadrilla == null) {
            throw new IllegalArgumentException("No existe la cuadrilla " + codigoCuadrilla.trim() + ".");
        }
        Jornada jornada = new Jornada(contadorJornadas + 1, cuadrilla);
        contadorJornadas++;
        jornadas.add(jornada);
        return jornada;
    }

    private Jornada obtenerJornada(int numero) {
        Jornada jornada = buscarJornada(numero);
        if (jornada == null) {
            throw new IllegalArgumentException("No existe la jornada " + numero + ".");
        }
        return jornada;
    }

    public void asignarOrden(int numeroJornada, String codigoOrden) {
        Jornada jornada = obtenerJornada(numeroJornada);
        OrdenServicio orden = buscarOrden(codigoOrden);
        if (orden == null) {
            throw new IllegalArgumentException("No existe la orden " + codigoOrden.trim() + ".");
        }
        jornada.asignarOrden(orden);
    }

    public void retirarOrden(int numeroJornada, String codigoOrden) {
        Jornada jornada = obtenerJornada(numeroJornada);
        jornada.retirarOrden(codigoOrden.trim());
    }

    public void iniciarJornada(int numeroJornada) {
        Jornada jornada = obtenerJornada(numeroJornada);
        jornada.iniciar();
    }

    public void registrarResultado(int numeroJornada, String codigoOrden, boolean completada) {
        Jornada jornada = obtenerJornada(numeroJornada);
        jornada.registrarResultado(codigoOrden.trim(), completada);
    }

    public ArrayList<OrdenServicio> getOrdenes() {
        return ordenes;
    }

    public ArrayList<Cuadrilla> getCuadrillas() {
        return cuadrillas;
    }

    public ArrayList<Jornada> getJornadas() {
        return jornadas;
    }

    public String reporteOrdenes() {
        if (ordenes.isEmpty()) {
            return "No hay órdenes registradas.";
        }
        ArrayList<String> categorias = new ArrayList<>();
        for (OrdenServicio orden : ordenes) {
            if (!categorias.contains(orden.getCategoria())) {
                categorias.add(orden.getCategoria());
            }
        }
        String texto = "";
        for (String categoria : categorias) {
            texto = texto + categoria + "\n";
            for (EstadoOrden estado : EstadoOrden.values()) {
                int cantidad = 0;
                for (OrdenServicio orden : ordenes) {
                    if (orden.getCategoria().equals(categoria) && orden.getEstado() == estado) {
                        cantidad++;
                    }
                }
                texto = texto + "   " + estado + ": " + cantidad + "\n";
            }
        }
        return texto;
    }

    public String reporteIngresos() {
        double porRegistro = 0;
        double porVisitas = 0;
        ArrayList<String> categorias = new ArrayList<>();
        for (OrdenServicio orden : ordenes) {
            porRegistro = porRegistro + orden.getMontoRegistro();
            porVisitas = porVisitas + orden.getCargosVisitas();
            if (!categorias.contains(orden.getCategoria())) {
                categorias.add(orden.getCategoria());
            }
        }
        String texto = "Ingresos por registro: " + Formato.monto(porRegistro) + "\n";
        texto = texto + "Ingresos por visitas adicionales: " + Formato.monto(porVisitas) + "\n";
        texto = texto + "Ingreso total: " + Formato.monto(porRegistro + porVisitas) + "\n";
        texto = texto + "Ingreso por categoría:\n";
        for (String categoria : categorias) {
            double total = 0;
            for (OrdenServicio orden : ordenes) {
                if (orden.getCategoria().equals(categoria)) {
                    total = total + orden.getIngresoTotal();
                }
            }
            texto = texto + "   " + categoria + ": " + Formato.monto(total) + "\n";
        }
        return texto;
    }

    public String reporteJornadasAbiertas() {
        String texto = "";
        for (Jornada jornada : jornadas) {
            if (jornada.getEstado() != EstadoJornada.CERRADA) {
                texto = texto + jornada + "\n";
            }
        }
        if (texto.isEmpty()) {
            return "No hay jornadas sin cerrar.";
        }
        return texto;
    }
}
