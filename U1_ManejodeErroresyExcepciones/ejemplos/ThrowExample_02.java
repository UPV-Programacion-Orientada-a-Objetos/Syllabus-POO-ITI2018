package U1_ManejodeErroresyExcepciones.ejemplos;

import java.io.FileNotFoundException;
import java.util.Vector;

public class ThrowExample_02 {

    public static void main(String[] args) {
        int evenNumbers[] = { 3, 4, 7, 8, 9 };
        Vector<EvenInteger> vei = new Vector<EvenInteger>();

        for (int element : evenNumbers) {
            try {
                vei.add(boxInteger(element));
            // } catch (NotEvenIntegerException e) {
            } catch (NumberFormatException e) {
                // se maneja realiza el flujo alternativo
                vei.add(boxInteger(element -1 )); // se resta 1 al número a ingresar
             }
        }

        vei.forEach(vecItem->System.out.println(vecItem));
        
    }
    
    // No se maneja la Excepción, sólo se propaga hacia arriba de la pila de llamadas
    // public static EvenInteger boxInteger(int n) throws NotEvenIntegerException {
    public static EvenInteger boxInteger(int n) throws NumberFormatException {
        EvenInteger ei = new EvenInteger(n);

        return ei;
    }
}
