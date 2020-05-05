package U1_ManejodeErroresyExcepciones.ejemplos;

public class ExceptionExample3 {

    public static void main(String[] args) {
        try {
            metodo1(null);
        }
        catch (Exception e) {
            System.out.println(e.getClass().getCanonicalName());

            e.printStackTrace();

            if (e instanceof NullPointerException) {
                System.out.println("La excepción pertenece a NullPointerException");
            }
            else {
                System.out.println("Es otra excepcion");
            }
        }
    }

    public static void metodo1(String s) {
        if (s.equals("abc")) {
            System.out.println("Equals abc");
        }
        else {
            System.out.println("Not equal");
        }
    }
}