package U3_Concurrencia.ejemplos;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentCollections {

    public static void main(String[] args) {
        // modifyList();
        modifyCopyOnWriteArrayList();
    }

    public static void modifyList() {
        System.out.println("\nmodifyList():");
        List<String> list = Arrays.asList("One", "Two");
        System.out.println(list);

        try {
            for (String e: list) {
                System.out.println(e);
                list.add("Three");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(list);
    }

    public static void modifyCopyOnWriteArrayList() {
        System.out.println("\nmodifyCopyOnWriteArrayList()");

        List<String> list = new CopyOnWriteArrayList<>(Arrays.asList("One", "Two"));
        System.out.println(list);

        try {
            for (String e: list) {
                System.out.print(e + " ");
                list.add("Three");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n" + list);
    }
    
}