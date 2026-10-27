import java.util.Locale;

public class Formato {

    public static String monto(double valor) {
        return "Q" + String.format(Locale.US, "%.2f", valor);
    }

    public static String numero(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }
}
