package com.spolancom;

import com.spolancom.net.LinkFinder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebCrawler6 implements LinkHandler {

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<String>());
    // private final Collection<String> visitedLinks = Collections.synchronizedList(new ArrayList<String>());
    private String url;
    private ExecutorService executorService;

    public WebCrawler6(String startingURL, int maxThreads) {
        this.url = startingURL;
        executorService = Executors.newFixedThreadPool(maxThreads);
    }

    @Override
    public void queueLink(String link) throws Exception {
        startNewThread(link);
    }

    private void startNewThread(String link) throws Exception {
        executorService.execute(new LinkFinder(link, this));
    }

    public void startCrawling() throws Exception {
        //System.out.println("visitando: " + this.url);
        startNewThread(this.url);
    }

    @Override
    public int size() {
        return visitedLinks.size();
    }

    @Override
    public void addVisited(String s) {
        visitedLinks.add(s);
    }

    @Override
    public boolean visited(String link) {
        return visitedLinks.contains(link);
    }
}
