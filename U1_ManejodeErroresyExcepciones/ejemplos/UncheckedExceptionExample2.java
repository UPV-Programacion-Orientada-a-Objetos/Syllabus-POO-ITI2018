package U1_ManejodeErroresyExcepciones.ejemplos;

import java.util.Scanner;


public class UncheckedExceptionExample2 {

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);

        System.out.println("Digite el tamaño del array: ");
        
        System.out.println(obtenerArray(in.nextInt()).length);
    }

    public static int[] obtenerArray(int tam) {
        int tmpArray[] = new int[tam];

        return tmpArray;
    }
}