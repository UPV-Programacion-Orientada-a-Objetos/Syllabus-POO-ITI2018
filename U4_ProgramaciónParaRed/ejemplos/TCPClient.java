package U4_ProgramaciónParaRed.ejemplos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
    
    public static void main(String[] args) {
        try (Socket s = new Socket("localhost", 3333);
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in)))
        {
            String prompt = "Say something";
            System.out.println(prompt);
            String msg;

            while ((msg = console.readLine()) != null) {
                dos.writeUTF(msg);
                dos.flush();
                if (msg.equalsIgnoreCase("end")) {
                    break;
                }

                msg = dis.readUTF();
                System.out.println("Server said: " + msg);
                if (msg.equalsIgnoreCase("end")) {
                    break;
                }
                System.out.println(prompt);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}