package edu.illinois.cs.cogcomp.fbtype;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import static org.junit.Assert.*;

public class MultilingualWikiContextTest {
    private final String title1 = "Hydrochloric_acid";
    private final String curid1 = "19916686";

    @Test
    public void testGetContextByENCurId() throws IOException {
        MultilingualWikiContext multilingualWikiContext =
                new MultilingualWikiContext("config/es-test.config");
        ArrayList<Pair<Pair<Integer, Integer>, String>> esContext =
                multilingualWikiContext.getContextByENCurId(curid1);
        Pair<Pair<Integer, Integer>, String> context = new Pair<>(new Pair<>(29, 46),
                        "Es atacado lentamente por el ácido clorhídrico (HCl) en presencia de aire.");
        assertTrue(context.equals(esContext.get(2)));
    }

    @Test
    public void testGetContextByENTitle() throws IOException {
        MultilingualWikiContext multilingualWikiContext =
                new MultilingualWikiContext("config/es-test.config");
        ArrayList<Pair<Pair<Integer, Integer>, String>> esContext =
                multilingualWikiContext.getContextByENTitle(title1);
        Pair<Pair<Integer, Integer>, String> context = new Pair<>(new Pair<>(29, 46),
                        "Es atacado lentamente por el ácido clorhídrico (HCl) en presencia de aire.");
        assertTrue(context.equals(esContext.get(2)));
    }
}
