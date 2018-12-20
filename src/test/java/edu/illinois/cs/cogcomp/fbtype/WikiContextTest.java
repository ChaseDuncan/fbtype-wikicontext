package edu.illinois.cs.cogcomp.fbtype;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WikiContextTest {
    private final WikiContext wikiContext = new WikiContext();
    private final String curId1 = "534366";
    @Test
    public void testGetContextByCurId(){
        System.out.println(wikiContext.getContextByCurId(curId1));
    }
}
