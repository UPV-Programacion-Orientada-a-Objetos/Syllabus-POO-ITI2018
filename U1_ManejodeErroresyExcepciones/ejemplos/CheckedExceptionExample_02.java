package U1_ManejodeErroresyExcepciones.ejemplos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class CheckedExceptionExample_02 {

    public static void main(String[] args) {
        showFileContent("ejemplo.txt");
    }

    public static void showFileContent(String path) {
        File file = new File(path);

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (FileNotFoundException e) {
           // código de recuperación del sistema
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
