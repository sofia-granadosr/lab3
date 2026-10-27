import java.util.Scanner;

public class Main {

    private static Scanner teclado = new Scanner(System.in);
    private static Empresa empresa = new Empresa();

    public static void main(String[] args) {
        cargarDatosIniciales();
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            opcion = leerEntero("Elija una opción: ");
            try {
                if (opcion == 1) {
                    cotizarYRegistrar();
                } else if (opcion == 2) {
                    registrarCuadrilla();
                } else if (opcion == 3) {
                    crearJornada();
                } else if (opcion == 4) {
                    asignarOrden();
                } else if (opcion == 5) {
                    retirarOrden();
                } else if (opcion == 6) {
                    iniciarJornada();
                } else if (opcion == 7) {
                    registrarResultado();
                } else if (opcion == 8) {
                    consultarOrdenes();
                } else if (opcion == 9) {
                    consultarCuadrillas();
                } else if (opcion == 10) {
                    System.out.println(empresa.reporteOrdenes());
                } else if (opcion == 11) {
                    System.out.println(empresa.reporteIngresos());
                } else if (opcion == 12) {
                    System.out.println(empresa.reporteJornadasAbiertas());
                } else if (opcion == 0) {
                    System.out.println("Hasta luego.");
                } else {
                    System.out.println("Opción no válida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Operación rechazada: " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== TecniHogar =====");
        System.out.println("1. Cotizar y registrar orden");
        System.out.println("2. Registrar cuadrilla");
        System.out.println("3. Crear jornada");
        System.out.println("4. Asignar orden a jornada");
        System.out.println("5. Retirar orden de jornada");
        System.out.println("6. Iniciar jornada");
        System.out.println("7. Registrar resultado de visita");
        System.out.println("8. Consultar órdenes");
        System.out.println("9. Consultar cuadrillas");
        System.out.println("10. Reporte de órdenes por categoría y estado");
        System.out.println("11. Reporte de ingresos");
        System.out.println("12. Reporte de jornadas sin cerrar");
        System.out.println("0. Salir");
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, escriba un número entero.");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).replace(",", ".");
            try {
                double valor = Double.parseDouble(texto);
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    System.out.println("Entrada inválida, escriba un número.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, escriba un número.");
            }
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.equalsIgnoreCase("s")) {
                return true;
            }
            if (texto.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Entrada inválida, escriba s o n.");
        }
    }

    private static Zona leerZona() {
        while (true) {
            int opcion = leerEntero("Zona (1 = metropolitana, 2 = periférica): ");
            if (opcion == 1) {
                return Zona.METROPOLITANA;
            }
            if (opcion == 2) {
                return Zona.PERIFERICA;
            }
            System.out.println("Zona no válida, escriba 1 o 2.");
        }
    }

    private static void cargarDatosIniciales() {
        empresa.cargarOrdenInicial(new Diagnostico("DIA-001", "Carlos Méndez", "Zona 10, Ciudad de Guatemala", 1.0, Zona.METROPOLITANA));
        empresa.cargarOrdenInicial(new Diagnostico("DIA-002", "Ana López", "Villa Nueva, Colonia El Frutal", 1.5, Zona.PERIFERICA));
        empresa.cargarOrdenInicial(new Instalacion("INS-001", "Luis Pérez", "Zona 15, Ciudad de Guatemala", 2.5, Zona.METROPOLITANA, 5));
        empresa.cargarOrdenInicial(new Instalacion("INS-002", "María Gómez", "San Miguel Petapa, Zona 2", 3.2, Zona.PERIFERICA, 2));
        empresa.cargarOrdenInicial(new InstalacionElectricaCertificada("ELE-001", "Jorge Ramírez", "Zona 11, Ciudad de Guatemala", 2.5, Zona.METROPOLITANA, 5, 2000));
        empresa.cargarOrdenInicial(new InstalacionElectricaCertificada("ELE-002", "Sofía Castillo", "Mixco, Colonia Monte Real", 3.0, Zona.PERIFERICA, 2, 5000));
        empresa.cargarOrdenInicial(new MantenimientoAire("MAN-001", "Pedro Morales", "Zona 14, Ciudad de Guatemala", 2.0, Zona.METROPOLITANA, 12000));
        empresa.cargarOrdenInicial(new MantenimientoAire("MAN-002", "Lucía Herrera", "Zona 7, Ciudad de Guatemala", 1.5, Zona.METROPOLITANA, 24000));
        empresa.registrarCuadrilla("CUA-01", 8, false);
        empresa.registrarCuadrilla("CUA-02", 6, true);
        empresa.registrarCuadrilla("CUA-03", 4, false);
    }

    private static void cotizarYRegistrar() {
        System.out.println("1. Diagnóstico");
        System.out.println("2. Instalación");
        System.out.println("3. Instalación eléctrica certificada");
        System.out.println("4. Mantenimiento de aire acondicionado");
        int categoria = leerEntero("Categoría: ");
        if (categoria < 1 || categoria > 4) {
            System.out.println("Categoría no válida.");
            return;
        }
        String codigo = leerTexto("Código de la orden: ");
        empresa.validarCodigoOrden(codigo);
        String cliente = leerTexto("Nombre del cliente: ");
        String direccion = leerTexto("Dirección: ");
        double horas = leerDecimal("Horas estimadas: ");
        Zona zona = leerZona();
        OrdenServicio orden;
        if (categoria == 1) {
            orden = new Diagnostico(codigo, cliente, direccion, horas, zona);
        } else if (categoria == 2) {
            int puntos = leerEntero("Cantidad de puntos a instalar: ");
            orden = new Instalacion(codigo, cliente, direccion, horas, zona, puntos);
        } else if (categoria == 3) {
            int puntos = leerEntero("Cantidad de puntos a instalar: ");
            double materiales = leerDecimal("Valor declarado de los materiales: ");
            orden = new InstalacionElectricaCertificada(codigo, cliente, direccion, horas, zona, puntos, materiales);
        } else {
            int btu = leerEntero("Capacidad del equipo en BTU: ");
            orden = new MantenimientoAire(codigo, cliente, direccion, horas, zona, btu);
        }
        System.out.println();
        System.out.println("--- Cotización ---");
        System.out.println("Categoría: " + orden.getCategoria());
        System.out.println("Costo: " + Formato.monto(orden.calcularCosto()));
        System.out.println("Tiempo estimado de atención: " + orden.getDiasAtencion() + " día(s) hábil(es)");
        boolean acepta = leerSiNo("¿El cliente acepta? (s/n): ");
        if (acepta) {
            empresa.registrarOrden(orden);
            System.out.println("Orden registrada y cobrada: " + Formato.monto(orden.getMontoRegistro()) + ". Queda pendiente.");
        } else {
            System.out.println("El cliente no aceptó. No se registró nada.");
        }
    }

    private static void registrarCuadrilla() {
        String codigo = leerTexto("Código de la cuadrilla: ");
        double capacidad = leerDecimal("Capacidad máxima de horas por jornada: ");
        boolean certificada = leerSiNo("¿Está certificada en refrigeración? (s/n): ");
        empresa.registrarCuadrilla(codigo, capacidad, certificada);
        System.out.println("Cuadrilla registrada.");
    }

    private static void crearJornada() {
        String codigo = leerTexto("Código de la cuadrilla: ");
        Jornada jornada = empresa.crearJornada(codigo);
        System.out.println("Jornada creada con número " + jornada.getNumero() + ".");
    }

    private static void asignarOrden() {
        int numero = leerEntero("Número de jornada: ");
        String codigo = leerTexto("Código de la orden: ");
        empresa.asignarOrden(numero, codigo);
        System.out.println("Orden asignada a la jornada " + numero + ".");
    }

    private static void retirarOrden() {
        int numero = leerEntero("Número de jornada: ");
        String codigo = leerTexto("Código de la orden: ");
        empresa.retirarOrden(numero, codigo);
        System.out.println("Orden retirada. Vuelve a quedar pendiente.");
    }

    private static void iniciarJornada() {
        int numero = leerEntero("Número de jornada: ");
        empresa.iniciarJornada(numero);
        System.out.println("Jornada iniciada. Sus órdenes pasan a estar en atención.");
    }

    private static void registrarResultado() {
        int numero = leerEntero("Número de jornada: ");
        String codigo = leerTexto("Código de la orden: ");
        boolean completada = leerSiNo("¿La orden fue completada? (s = completada, n = visita fallida): ");
        empresa.registrarResultado(numero, codigo, completada);
        System.out.println("Resultado registrado.");
        Jornada jornada = empresa.buscarJornada(numero);
        if (jornada.getEstado() == EstadoJornada.CERRADA) {
            System.out.println("La jornada " + numero + " quedó cerrada y la cuadrilla está libre.");
        }
    }

    private static void consultarOrdenes() {
        for (OrdenServicio orden : empresa.getOrdenes()) {
            System.out.println(orden);
        }
    }

    private static void consultarCuadrillas() {
        for (Cuadrilla cuadrilla : empresa.getCuadrillas()) {
            System.out.println(cuadrilla);
        }
    }
}
