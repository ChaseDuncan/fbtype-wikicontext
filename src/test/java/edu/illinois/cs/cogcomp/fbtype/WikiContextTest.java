package edu.illinois.cs.cogcomp.fbtype;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class WikiContextTest {
    private final String title1 = "\"Weird_Al\"_Yankovic";
    private final String curid1 = "534366";

    @Test
    public void testGetContextByCurId() throws IOException {
        WikiContext wikiContext = new WikiContext("config/test.config");
        ArrayList<Pair<Pair<Integer, Integer>, String>> context1 =
                wikiContext.getContextByTitle(title1);
        Pair<Pair<Integer, Integer>, String> firstContext =
                new Pair<Pair<Integer, Integer>, String>(
                        new Pair<Integer, Integer>(0, 17),
                        "Weird Al Yankovic made a song parody of the virus titled \"Virus Alert\".");
        assertTrue(firstContext.equals(context1.get(0)));
    }

    @Test
    public void testGetContextByTitle() throws IOException{
        WikiContext wikiContext = new WikiContext("config/test.config");
        ArrayList<Pair<Pair<Integer, Integer>, String>> context2 = wikiContext.getContextByCurId(curid1);

        Pair<Pair<Integer, Integer>, String> secondContext =
                new Pair<Pair<Integer, Integer>, String>(
                        new Pair<Integer, Integer>(0, 12),
        "Barack Obama was born and raised in Hawaii (other than a four-year period of his childhood spent in " +
                "Indonesia) and made Illinois his home and base after completing law school and later represented the state in " +
                "the US Senate.");

        assertTrue(secondContext.equals(context2.get(0)));
    }
}
