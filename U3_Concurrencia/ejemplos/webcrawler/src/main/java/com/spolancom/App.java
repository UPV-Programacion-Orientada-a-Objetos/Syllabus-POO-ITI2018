package com.spolancom;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            new WebCrawler6("https://es.wikipedia.org/wiki/Roma", 64).startCrawling();
            //new WebCrawler7("https://es.wikipedia.org/wiki/Roma", 64).startCrawling();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
