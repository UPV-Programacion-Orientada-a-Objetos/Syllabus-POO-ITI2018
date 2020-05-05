package com.spolancom;

import com.spolancom.example.ExampleClass;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        for (String arg: args) {
            System.out.println(arg + " ");
        }

        String p = System.getProperty("someParameter");
        System.out.println("\n" + p);

//        String x = System.getenv("x");
//        String y = System.getenv("y");
//
//        System.out.println(x + "\t" + y);

        int i = ExampleClass.multiplyByTwo(Integer.parseInt(p));

        System.out.println(i);
    }
}
