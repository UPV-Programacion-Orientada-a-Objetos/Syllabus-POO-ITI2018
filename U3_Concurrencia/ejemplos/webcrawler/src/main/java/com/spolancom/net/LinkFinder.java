package com.spolancom.net;

import com.spolancom.LinkHandler;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinkFinder implements Runnable {

    private String url;
    private LinkHandler linkHandler;

    /**
     * Se utiliza para estadísticas
     */
    private static final long t0 = System.nanoTime();

    public LinkFinder(String url, LinkHandler handler){
        this.url = url;
        this.linkHandler = handler;
    }

    @Override
    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        // si no se ha visitado
        if (!linkHandler.visited(url)) {
            try {
                URL urilink = new URL(url);
                Parser parser = new Parser(urilink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
                List<String> urls = new ArrayList<>();

                //System.out.println("Analizando: " + url);
                for (int i=0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);

                    if (!extracted.getLink().isBlank() && !linkHandler.visited(extracted.getLink())) {
                        urls.add(extracted.getLink());
                    }
                }

                // visitatamos el link
                linkHandler.addVisited(url);

                if (linkHandler.size() == 1500) {
                    System.out.println("Tiempo para visitar 1500 links = " + (System.nanoTime() - t0));
                }

                for (String l: urls) {
                    linkHandler.queueLink(l);
                }
            }
            catch(Exception e) {
                // Ignoramos todos las excepciones
                // e.printStackTrace();
            }
        }
    }
}
