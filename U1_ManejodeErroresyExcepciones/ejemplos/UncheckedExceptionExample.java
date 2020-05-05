package U1_ManejodeErroresyExcepciones.ejemplos;

public class UncheckedExceptionExample {

    public static void main(String[] args) {

        System.out.println(division(10, -5));
    }

    public static float division(int dividendo, int divisor) {
        float cociente = dividendo / divisor;
        return cociente;
    }
}