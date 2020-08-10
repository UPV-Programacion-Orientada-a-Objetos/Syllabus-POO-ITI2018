/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclientearchivo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author telecom
 */
public class CClienteArchivo {
    public final static int PUERTO = 13268;      // puerto de conexión
    public final static String NOMBRE_SERVIDOR = "127.0.0.1";  //localhost
    public final static String ARCHIVO = "c:/temp/source-downloaded.pdf";  // nombre con el que se guardará el archivo recibido 
    public final static int TAM_ARCHIVO = 6022386; // Tamaño del archivo. Tiene que ser mucho mayor que el archivo original.

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int bytesLeidos;
        int actual = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket skCliente = null;
        try {
          skCliente = new Socket(NOMBRE_SERVIDOR, PUERTO);
          System.out.println("Conectando...");

          // se recibe el archivo
          byte [] ab  = new byte [TAM_ARCHIVO];
          InputStream is = skCliente.getInputStream();
          fos = new FileOutputStream(ARCHIVO);
          bos = new BufferedOutputStream(fos);
          bytesLeidos = is.read(ab,0,ab.length);
          actual = bytesLeidos;

          do {
             bytesLeidos =
                is.read(ab, actual, (ab.length-actual));
             if(bytesLeidos >= 0) actual += bytesLeidos;
          } while(bytesLeidos > -1);

          bos.write(ab, 0 , actual);
          bos.flush();
          System.out.println("Archivo " + ARCHIVO + " descargado (" + actual + " bytes leidos)");
        }
        finally {
          if (fos != null) fos.close();
          if (bos != null) bos.close();
          if (skCliente != null) skCliente.close();
        }
    }
    
}
