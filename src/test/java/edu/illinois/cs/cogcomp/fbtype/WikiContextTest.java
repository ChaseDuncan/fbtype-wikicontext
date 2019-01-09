package edu.illinois.cs.cogcomp.fbtype;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class WikiContextTest {
    //private final String curId1 = "534366";
    private final String title1 = "\"Weird_Al\"_Yankovic";

    @Test
    public void testGetContextByCurId() throws IOException {
        WikiContext wikiContext = new WikiContext("config/test.config");
        //ArrayList<Pair<String, String>> context1 = wikiContext.getContextByCurId(curId1);
        //System.out.println("SURFACE: " + context1.get(0).getFirst());
        //System.out.println("CONTEXT: " + context1.get(0).getSecond());
        ArrayList<Pair<Pair<Integer, Integer>, String>> context1 = null;
        context1 = wikiContext.getContextByTitle(title1);
        for(Object context : context1){
            System.out.println(context);
        }
    }
}
