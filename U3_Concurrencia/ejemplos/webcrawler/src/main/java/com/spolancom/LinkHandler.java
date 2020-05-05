package com.spolancom;

public interface LinkHandler {

    /**
     * Guarda el link en la cosa
     * @param link
     * @throws Exception
     */
    void queueLink(String link) throws Exception;

    /**
     * Retorna el número de links visitados
     * @return
     */
    int size();

    /**
     * Verifica si el link ya ha sido visitado
     * @param link
     * @return
     */
    boolean visited(String link);

    /**
     * Marca el link como visitado
     * @param link
     */
    void addVisited(String link);
    
}