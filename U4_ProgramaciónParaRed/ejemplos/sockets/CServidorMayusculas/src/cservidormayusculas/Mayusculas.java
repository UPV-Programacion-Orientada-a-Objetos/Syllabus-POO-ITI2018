/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cservidormayusculas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author telecom
 */
public class Mayusculas extends Thread {
     private Socket s;
     private int clieNum;
     
     public Mayusculas(Socket socket, int clientNumber) {
            this.s = socket;
            this.clieNum = clientNumber;
            log("Nueva Conexión con el cliente # " + clieNum + " en " + s);
        }
     
     public void run() {
            try {
                BufferedReader in = new BufferedReader( new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);

                
                out.println("Hola, tu eres el cliente #" + clieNum + ".");
                out.println("Ingrese una linea con sólo un punto para salir\n");
                while (true) {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    out.println(input.toUpperCase());
                }
            } catch (IOException e) {
                log("Error en el cliente # " + clieNum + ": " + e);
            } finally {
                try {
                    s.close();
                } catch (IOException e) {
                    log("¿Deseas cerrar el socket?");
                }
                log("Conexión con el cliente # " + clieNum + " cerrada");
            }
        }
    
     private void log(String message) {
            System.out.println(message);
        }
}
