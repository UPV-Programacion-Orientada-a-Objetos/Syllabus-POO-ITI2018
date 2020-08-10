/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cserverchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 *
 * @author telecom
 */
public class CServerChat {
    private static final int PUERTO = 9001;
    private static HashSet<String> ids = new HashSet<String>();
    private static HashSet<PrintWriter> mensajes = new HashSet<PrintWriter>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        System.out.println("El servidor char se esta ejecutando.");
        ServerSocket skServer = new ServerSocket(PUERTO);
        try {
            while (true) {
                new CRecepcion(skServer.accept()).start();
            }
        } finally {
            skServer.close();
        }
    }
    
    private static class CRecepcion extends Thread {
        private String nombre;
        private Socket sk;
        private BufferedReader in;
        private PrintWriter out;
        
        public CRecepcion(Socket s) {
            this.sk = s;
        }
        
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(
                    sk.getInputStream()));
                out = new PrintWriter(sk.getOutputStream(), true);
                while (true) {
                    out.println("NOMBREE");
                    nombre = in.readLine();
                    if (nombre == null) {
                        return;
                    }
                    synchronized (ids) {
                        if (!ids.contains(nombre)) {
                            ids.add(nombre);
                            break;
                        }
                    }
                }

                
                out.println("NOMBREA");
                mensajes.add(out);

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : mensajes) {
                        writer.println("Mensaje " + nombre + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (nombre != null) {
                    ids.remove(nombre);
                }
                if (out != null) {
                    mensajes.remove(out);
                }
                try {
                    sk.close();
                } catch (IOException e) {
                }
            }
        }
    }    
}
