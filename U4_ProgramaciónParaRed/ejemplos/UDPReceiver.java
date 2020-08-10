package U4_ProgramaciónParaRed.ejemplos;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver {

    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket(3333)) {
            DatagramPacket dp = new DatagramPacket(new byte[50], 50);
            int i = 0;

            while (true) {
                ds.receive(dp);
                i = 1;
                for (byte b: dp.getData()) {
                    System.out.print(Character.toString(b));
                    if (i++ == dp.getLength()) {
                        break;
                    }
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}