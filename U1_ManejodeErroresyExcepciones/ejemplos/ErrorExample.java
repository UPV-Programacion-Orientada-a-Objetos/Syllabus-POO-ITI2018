package U1_ManejodeErroresyExcepciones.ejemplos;

public class ErrorExample {

    public static void main(String[] args) {
        try {
            DropedClass dc = new DropedClass();
        }
        catch(NoClassDefFoundError e) {
            System.out.println("algo pasa aqui");
        }

        System.out.println("El programa sigue");
         
    }
}