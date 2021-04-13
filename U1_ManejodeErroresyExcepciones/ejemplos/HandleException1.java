package U1_ManejodeErroresyExcepciones.ejemplos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HandleException1 {

    public static void main(String[] args) {
        String filePath = "U1_ManejodeErroresyExcepciones/ejemplos/lorem_ipsum.txt";

        System.out.println(readFromFile(filePath));
    }

    public static String readFromFile(String path) {
        String text = new String("");

        // try con recursos
        try (Scanner s = new Scanner(new File(path));) {
            while (s.hasNextLine()) {
                text += s.nextLine();
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("No es posible leer el archivo: " + path);
            e.printStackTrace();
        }
        // No necesita bloque finally
        
        return text;
   }

   public static String readFromFile_oldWay(String path) {
       String text = new String("");
       Scanner s = null;

       try {
           s = new Scanner(new File(path));
           while (s.hasNextLine()) {
               text += s.nextLine();
           }
       }
       catch (FileNotFoundException e) {
            System.out.println("No es posible leer el archivo: " + path);
            e.printStackTrace();
       }
       finally {
           if (s != null) s.close();
       }

       return text;
   }

}
