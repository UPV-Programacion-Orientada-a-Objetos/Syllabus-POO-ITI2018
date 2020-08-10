/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cservidor;

import java.io.*;
import java.net.*;

/**
 *
 * @author telecom
 */
public class CServidor {
    static final int puerto = 5000;
    
    public CServidor()
    {
        try
        {
            ServerSocket skServidor = new ServerSocket(puerto);
            System.out.println("Escuchado en el puerto: " + puerto);
            for (int numCli = 0; numCli < 4; numCli++)
            {
                Socket skCliente = skServidor.accept();
                System.out.println("Sirvo al cliente: " + numCli);
                OutputStream os = skCliente.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF("hola Cliente " + numCli);
                skCliente.close();
            }
            System.out.println("Demasiados clientes por hoy");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new CServidor();
    }
    
}
