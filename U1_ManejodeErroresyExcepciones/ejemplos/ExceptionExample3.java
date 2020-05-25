package U1_ManejodeErroresyExcepciones.ejemplos;

import java.io.FileNotFoundException;

public class ExceptionExample3 {

    public static void main(String[] args) {

        String abc = "otra";

        try {
            metodo1(abc);
        }
        catch(NullPointerException e) {
           // abc = "abc";
           // metodo1(abc);
           e.printStackTrace();
        }
        catch (RuntimeException e) {
            System.out.println("En cas run time");
            e.getClass().getCanonicalName();
            e.printStackTrace();
        }
        catch(Exception e) {
            System.out.println("Otra excepción");
            e.getClass().getCanonicalName();
            e.printStackTrace();
        }
        
    }

    public static void metodo1(String s) throws FileNotFoundException {
        if (s.equals("abc")) {
            System.out.println("Equals abc");
        }
        else {
            throw new FileNotFoundException(" no tiene sentido");
        }
    }
}