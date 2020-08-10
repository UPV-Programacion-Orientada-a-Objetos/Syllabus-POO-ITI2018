
package cservidormayusculas;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author telecom
 */
public class CServidorMayusculas {

    
    public static void main(String[] args) throws IOException {
        System.out.println("El servidor para mayusculas se esta ejecutando.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new Mayusculas(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }
    
}
