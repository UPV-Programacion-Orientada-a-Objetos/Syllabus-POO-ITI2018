package U1_ManejodeErroresyExcepciones.ejemplos;

public class ExceptionExample1 {

    public static void main(String[] args) {
        int x = 11;
        try {
            if (x > 10) {
                throw new NullPointerException("El valor de x esta fuera de rango: " + x);
            }
        }
        catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
            x = -1;
        }

        System.out.println("x = " + x);    
    }
}