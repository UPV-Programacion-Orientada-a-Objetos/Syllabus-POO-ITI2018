/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclientemayusculas;

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
public class CClienteMayusculas {
    
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Cliente a MAYUSCULAS");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);
    
    public CClienteMayusculas()
    {
        
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");

        dataField.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                out.println(dataField.getText());
                   String resp;
                try {
                    resp = in.readLine();
                    if (resp == null || resp.equals("")) {
                          System.exit(0);
                      }
                } catch (IOException ex) {
                       resp = "Error: " + ex;
                }
                messageArea.append(resp + "\n");
                dataField.selectAll();
            }
        });
        
    }
    
    public void connectToServer() throws IOException {

        
        String serverAddress = JOptionPane.showInputDialog(
            frame,
            "Ingresa la IP del servidor:",
            "Bienvenido al programa de conversión a mayusculas",
            JOptionPane.QUESTION_MESSAGE);

        
        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        
        for (int i = 0; i < 3; i++) {
            messageArea.append(in.readLine() + "\n");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        CClienteMayusculas client = new CClienteMayusculas();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }
    
}
