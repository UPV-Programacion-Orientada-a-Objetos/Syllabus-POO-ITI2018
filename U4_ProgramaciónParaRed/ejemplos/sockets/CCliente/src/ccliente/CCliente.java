/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccliente;

import java.net.*;
import java.io.*;

/**
 *
 * @author telecom
 */
public class CCliente {
    
    static final String host = "localhost";
    static final int puerto = 5000;
    
    public CCliente()
    {
        try
        {
            Socket skCliente = new Socket(host,puerto);
            InputStream is = skCliente.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            System.out.println(dis.readUTF());
            skCliente.close();
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
        new CCliente();
    }
    
}
