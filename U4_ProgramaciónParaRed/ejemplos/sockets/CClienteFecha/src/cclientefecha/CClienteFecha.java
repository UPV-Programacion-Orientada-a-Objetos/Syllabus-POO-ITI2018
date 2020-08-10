/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclientefecha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;


/**
 *
 * @author telecom
 */
public class CClienteFecha {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try
        {
            String dirServidor = JOptionPane.showInputDialog("Ingresa el nombre del servidor\nEl servicio esta activo en el puerto 9090");
            Socket s = new Socket(dirServidor,9090);
            BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String resp = bf.readLine();
            JOptionPane.showMessageDialog(null, resp);
            s.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    
}
