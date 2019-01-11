package edu.illinois.cs.cogcomp.fbtype;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.fbtype.resources.InterlanguageLinksDB;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.fbtype.resources.TitleIdDB;

import java.io.IOException;
import java.util.ArrayList;

public class MultilingualWikiContext extends WikiContext{
    InterlanguageLinksDB interlanguageLinksDB = null;
    TitleIdDB targetTitleIdDB = null;
    public MultilingualWikiContext(String configFile) throws IOException {
        super(configFile);
        ResourceManager rm = new ResourceManager(configFile);
        interlanguageLinksDB =
                new InterlanguageLinksDB(true, rm.getString("interlanguageLinksDB"));
        targetTitleIdDB =
                new TitleIdDB(true, rm.getString("targetTitleIdDB"));
    }

    /**
     * Returns a list of pairs of surface forms and context sentences, e.g.
     * (Barack Obama, Barack Obama was the 44th president of the United States of America)
     * @param enCurId English Wikipedia curId to get target language context for
     * @return list of contexts
     */
    public ArrayList<Pair<Pair<Integer, Integer>, String>> getContextByENCurId(String enCurId){
        String enTitle = titleIdDB.getTitle(enCurId);
        String targetCurId = interlanguageLinksDB.getTargetCurId(enTitle);
        return contextSentenceDB.getContext(targetTitleIdDB.getTitle(targetCurId));
    }

    /**
     * Retrieves contexts as above by Wikipedia title, e.g. "Barack_Obama"
     * @param enTitle English page to get contexts for
     * @return list of pairs of surface forms and contexts
     */
    public ArrayList<Pair<Pair<Integer, Integer>, String>>  getContextByENTitle(String enTitle){
        String targetCurId = interlanguageLinksDB.getTargetCurId(enTitle);
        return contextSentenceDB.getContext(targetTitleIdDB.getTitle(targetCurId));
    }
}
