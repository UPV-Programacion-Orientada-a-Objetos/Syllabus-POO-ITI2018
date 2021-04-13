package U1_ManejodeErroresyExcepciones.ejemplos;

// public class NotEvenIntegerException extends Exception {
public class NotEvenIntegerException extends NumberFormatException {

    public NotEvenIntegerException() {
        super();
    }

    public NotEvenIntegerException(String s) {
        super(s);
    }
}
