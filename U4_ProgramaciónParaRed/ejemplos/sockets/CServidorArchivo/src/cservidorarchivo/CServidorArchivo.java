/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cservidorarchivo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author telecom
 */
public class CServidorArchivo {
    public final static int PUERTO = 13268;  //puerto de conexión
    public final static String ARCHIVO_A_ENVIAR = "c:/temp/source.pdf";  //archivo a ser enviado

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket skServidor = null;
        Socket sk = null;
        
        try {
          skServidor = new ServerSocket(PUERTO);
          while (true) {
            System.out.println("Esperando conexión...");
            try {
              sk = skServidor.accept();
              System.out.println("Conexión aceptada : " + sk);
              // se envía el archivo
              File archivo = new File (ARCHIVO_A_ENVIAR);
              byte [] ab  = new byte [(int)archivo.length()]; //se crea un array de bytes
              fis = new FileInputStream(archivo);
              bis = new BufferedInputStream(fis);
              bis.read(ab,0,ab.length);
              os = sk.getOutputStream();
              System.out.println("Enviando " + ARCHIVO_A_ENVIAR + "(" + ab.length + " bytes)");
              os.write(ab,0,ab.length);
              os.flush();
              System.out.println("Enviado.");
            }
            finally {
              if (bis != null) bis.close();
              if (os != null) os.close();
              if (sk!=null) sk.close();
            }
          }
        }
        finally {
          if (skServidor != null) skServidor.close();
        }
    }
    
}
