package U1_ManejodeErroresyExcepciones.ejemplos;

public class ExceptionExample2 {

    public static void main(String[] args) {
        try {
            int x = 11;
            if (x > 10) {
                throw new RuntimeException("El valor de x esta fuera de rango: " + x);
            }
            // procesamiento normal del método
            System.out.println("Proceso normal del método");
        }
        catch(NullPointerException e) {
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("Este código siempre se llevará a cabo");
        }
    }
}