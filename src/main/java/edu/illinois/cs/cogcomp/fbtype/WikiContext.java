package edu.illinois.cs.cogcomp.fbtype;

import edu.illinois.cs.cogcomp.fbtype.resources.ContextSentenceDB;
import edu.illinois.cs.cogcomp.fbtype.resources.TitleToIdDB;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.core.datastructures.Pair;

import java.io.IOException;
import java.util.ArrayList;

public class WikiContext {
    //default configuration
    private static String configFile = "config/default.config";
    private ContextSentenceDB contextSentenceDB = null;
    private TitleToIdDB titleToIdDB = null;

    public WikiContext() throws IOException {
        this(configFile);
    }

    public WikiContext(String configFile) throws IOException {
        ResourceManager rm = new ResourceManager(configFile);
        contextSentenceDB = new ContextSentenceDB(rm.getString("contextSentenceDB"));
        titleToIdDB = new TitleToIdDB(rm.getString("titleToIdDBFile"));
    }

    /**
     * Returns a list of pairs of surface forms and context sentences, e.g.
     * (Barack Obama, Barack Obama was the 44th president of the United States of America)
     * @param curId Wikipedia curId of title to get contexts for
     * @return list of contexts
     */
    public ArrayList<Pair<String, String>>  getContextByCurId(String curId){

        String rawDataString = contextSentenceDB.getMap().get(curId);
        ArrayList<Pair<String, String>> contexts = null;
        if(rawDataString == null)
            return contexts;
         contexts = new ArrayList<Pair<String, String>>();

        String[] surfaceContextStrings = rawDataString.split("\\|");
        for (String surfaceContextString : surfaceContextStrings){
            String[] surfaceContextSplit = surfaceContextString.split("\t");
            Pair<String, String> surfaceContextPair =
                    new Pair<String, String>(surfaceContextSplit[0], surfaceContextSplit[1]);
            contexts.add(surfaceContextPair);
        }
        return contexts;
    }

    /**
     * Retrieves contexts as above by Wikipedia title, e.g. "Barack_Obama"
     * @param title page to get contexts for
     * @return list of pairs of surface forms and contexts
     */
    public ArrayList<Pair<String, String>>  getContextByTitle(String title){
        return getContextByCurId(titleToIdDB.getMap().get(title));
    }
}
