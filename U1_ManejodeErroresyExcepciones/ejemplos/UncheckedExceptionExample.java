package U1_ManejodeErroresyExcepciones.ejemplos;

public class UncheckedExceptionExample {

    public static void main(String[] args) {
    
    int a = 10;
    int b = 0;

        System.out.println(division(a, b));
    }

    public static float division(int dividendo, int divisor) {
        float cociente = dividendo / divisor;
        return cociente;
    }
}
