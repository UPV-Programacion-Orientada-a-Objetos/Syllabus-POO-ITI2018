/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cservidorfecha;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
/**
 *
 * @author telecom
 */
public class CServidorFecha {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ServerSocket skServer = null;
        try
        {
            skServer = new ServerSocket(9090);
                while(true)
                {
                    Socket skCliente = skServer.accept();
                    try
                    {
                        PrintWriter out = new PrintWriter (skCliente.getOutputStream(),true);
                        out.println(new Date().toString());
                    }
                    finally
                    {
                        skCliente.close();
                    }
                }
  
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if (skServer != null)
            {
                try
                {
                    skServer.close();
                }
                catch(IOException e)
                {
                    System.out.println(e);
                }
                
            }
        }
    }
    
}
