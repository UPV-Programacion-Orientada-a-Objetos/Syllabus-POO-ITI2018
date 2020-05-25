package U4_ProgramaciónParaRed.ejemplos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) {
        try (Socket s = new ServerSocket(3333).accept();
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String msg = dis.readUTF();
                System.out.println("Client said: " + msg);
                if ("end".equalsIgnoreCase(msg)) {
                    break;
                }

                System.out.println("Say something: ");
                msg = console.readLine();
                dos.writeUTF(msg);
                dos.flush();

                if ("end".equalsIgnoreCase(msg)) {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}