package com.spolancom.net;

import com.spolancom.LinkHandler;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class LinkFinderAction extends RecursiveAction {

    private String url;
    private LinkHandler cr;
    private static final long t0 = System.nanoTime();

    public LinkFinderAction(String url, LinkHandler cr) {
        this.url = url;
        this.cr = cr;
    }

    @Override
    protected void compute() {
        if (!cr.visited(url)) {
            try {
                List<RecursiveAction> actions = new ArrayList<RecursiveAction>();
                URL urilink = new URL(url);
                Parser parser = new Parser(urilink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));

                for (int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);

                    if (!extracted.extractLink().isEmpty() && !cr.visited(extracted.extractLink())) {
                        actions.add(new LinkFinderAction(extracted.extractLink(), cr));
                    }
                }
                cr.addVisited(url);

                if (cr.size() == 1500) {
                    System.out.println("Tiempo: " + (System.nanoTime() - t0 ));
                }

                invokeAll(actions);
            }
            catch (Exception e) {
                // ignoramos todas las excepciones
            }
        }
    }
}
