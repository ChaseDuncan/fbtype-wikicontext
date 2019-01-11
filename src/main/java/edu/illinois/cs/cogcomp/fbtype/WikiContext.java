package edu.illinois.cs.cogcomp.fbtype;

import edu.illinois.cs.cogcomp.fbtype.resources.ContextSentenceDB;
import edu.illinois.cs.cogcomp.fbtype.resources.TitleIdDB;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.core.datastructures.Pair;

import java.io.IOException;
import java.util.ArrayList;

public class WikiContext {
    protected ContextSentenceDB contextSentenceDB = null;
    protected TitleIdDB titleIdDB = null;

    public WikiContext(String configFile) throws IOException {
        ResourceManager rm = new ResourceManager(configFile);
        contextSentenceDB = new ContextSentenceDB(rm.getString("sentenceDB"));
        titleIdDB = new TitleIdDB(rm.getString("titleIdDB"));
    }

    /**
     * Returns a list of pairs of surface forms and context sentences, e.g.
     * (Barack Obama, Barack Obama was the 44th president of the United States of America)
     * @param curId Wikipedia curId of title to get contexts for
     * @return list of contexts
     */
    public ArrayList<Pair<Pair<Integer, Integer>, String>>  getContextByCurId(String curId){
        return contextSentenceDB.getContext(titleIdDB.getTitle(curId));
    }

    /**
     * Retrieves contexts as above by Wikipedia title, e.g. "Barack_Obama"
     * @param title page to get contexts for
     * @return list of pairs of surface forms and contexts
     */
    public ArrayList<Pair<Pair<Integer, Integer>, String>>  getContextByTitle(String title){
        return contextSentenceDB.getContext(title);
    }
}
