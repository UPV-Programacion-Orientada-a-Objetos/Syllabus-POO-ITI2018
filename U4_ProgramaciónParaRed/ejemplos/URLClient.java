package U4_ProgramaciónParaRed.ejemplos;

import java.net.URL;
import java.net.URLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.InputStream;

public class URLClient {

    public static void main(String[] args) {

        // getFromFile();
        // getFromURL();
        postToURL();       

    }

    private static void getFromURL() {
        try {
            URL url = new URL("https://www.google.com/search?q=Java&num=10");
            System.out.println(url.getPath());
            System.out.println(url.getFile());
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept", "text/html");
            conn.setRequestProperty("Connection", "close");
            conn.setRequestProperty("Accept-Language", "en-US");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getFromFile() {
        try {
            URL url = new URL("file:U4_ProgramaciónParaRed/ejemplos/hello.txt");
            System.out.println(url.getPath());
            System.out.println(url.getFile());
            try (InputStream is = url.openStream()) {
                int data = is.read();
                while (data != -1) {
                    System.out.println((char) data);
                    data = is.read();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void postToURL() {
        try {
            URL url = new URL("http://localhost:3333/something");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Method", "POST");
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setDoOutput(true);

            try (OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream())) {
                osw.write("parameter1=value1&parameter2=value2");
                osw.flush();
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}