package U1_ManejodeErroresyExcepciones.ejemplos;

import java.io.FileNotFoundException;

public class ExceptionExample3_03 {

    public static void main(String[] args) {

        try {
            metodo1(null);
        }
        catch(NullPointerException e) {
           
           e.printStackTrace();
        }
        // catch (RuntimeException e) {
        //     e.printStackTrace();
        // }
        catch (Exception e) {
            e.printStackTrace();
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
