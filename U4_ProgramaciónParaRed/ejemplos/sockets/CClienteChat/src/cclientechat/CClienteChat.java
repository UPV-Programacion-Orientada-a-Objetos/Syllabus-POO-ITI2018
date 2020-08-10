/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclientechat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author telecom
 */
public class CClienteChat {
    
    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("Ventana de chat");
    JTextField tf = new JTextField(40);
    JTextArea txtArea = new JTextArea(8, 40);
    
    public CClienteChat() {

       
        tf.setEditable(false);
        txtArea.setEditable(false);
        frame.getContentPane().add(tf, "North");
        frame.getContentPane().add(new JScrollPane(txtArea), "Center");
        frame.pack();

  
        tf.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                out.println(tf.getText());
                tf.setText("");
            }
        });
    }
    
    private String direccionServidor() {
        return JOptionPane.showInputDialog(
            frame,
            "Ingresa el nombre del servidor:",
            "Bienvenido al chat",
            JOptionPane.QUESTION_MESSAGE);
    }
    
    private String nombre() {
        return JOptionPane.showInputDialog(
            frame,
            "Ingresa un ID:",
            "Selección de ID",
            JOptionPane.PLAIN_MESSAGE);
    }
    
    private void run() throws IOException {
        
        String ds = direccionServidor();
        Socket sk = new Socket(ds, 9001);
        in = new BufferedReader(new InputStreamReader(
            sk.getInputStream()));
        out = new PrintWriter(sk.getOutputStream(), true);
        
        while (true) {
            String txt = in.readLine();
            if (txt.startsWith("NOMBREE")) {
                out.println(nombre());
            } else if (txt.startsWith("NOMBREA")) {
                tf.setEditable(true);
            } else if (txt.startsWith("Mensaje")) {
                txtArea.append(txt.substring(8) + "\n");
            }
        }
    }
    
        
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        CClienteChat cliente = new CClienteChat();
        cliente.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cliente.frame.setVisible(true);
        cliente.run();
    }
    
}
