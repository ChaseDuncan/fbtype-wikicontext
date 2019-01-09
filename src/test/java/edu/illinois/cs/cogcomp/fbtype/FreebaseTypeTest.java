package edu.illinois.cs.cogcomp.fbtype;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FreebaseTypeTest {
    FreebaseType freebaseType = new FreebaseType();

    String curIdPer = "15162268";  //person.author person
    String curIdNull = "38608933"; //written_work
    String curIdLoc = "29551290";  //location geography.glacier

    String titlePer = "Richard_Irving_Dodge";  //person.author person
    String titleNull = "The_Seven_Wonders_(Saylor_novel)"; //written_work
    String titleLoc = "Mondor_Glacier";  //location geography.glacier

    @Test
    public void testGetCoNLLTypeByCurId(){

        assertEquals(freebaseType.getCoNLLTypeById(curIdPer), "person");
        assertEquals(freebaseType.getCoNLLTypeById(curIdNull), null);
        assertEquals(freebaseType.getCoNLLTypeById(curIdLoc), "location");
    }

    @Test
    public void testGetCourseTypesById(){

        assertTrue(new ArrayList(Arrays.asList("person"))
                .equals(freebaseType.getCourseTypesById(curIdPer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getCourseTypesById(curIdNull)));
        assertTrue(new ArrayList(Arrays.asList("geography", "location"))
                .equals(freebaseType.getCourseTypesById(curIdLoc)));
        System.out.println(freebaseType.getCourseTypesById(curIdPer));
        System.out.println(freebaseType.getCourseTypesById(curIdNull));
        System.out.println(freebaseType.getCourseTypesById(curIdLoc));
    }

    @Test
    public void testGetFineTypesById(){

        assertTrue(new ArrayList(Arrays.asList("person.author", "person"))
                .equals(freebaseType.getFineTypesById(curIdPer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getFineTypesById(curIdNull)));
        assertTrue(new ArrayList(Arrays.asList("location", "geography.glacier"))
                .equals(freebaseType.getFineTypesById(curIdLoc)));
    }

    @Test
    public void testGetCoNLLTypeByTitle(){

        assertEquals(freebaseType.getCoNLLTypeByTitle(titlePer), "person");
        assertEquals(freebaseType.getCoNLLTypeByTitle(titleNull), null);
        assertEquals(freebaseType.getCoNLLTypeByTitle(titleLoc), "location");

    }

    @Test
    public void testGetCourseTypesTile(){

        assertTrue(new ArrayList(Arrays.asList("person"))
                .equals(freebaseType.getCourseTypesByTitle(titlePer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getCourseTypesByTitle(titleNull)));
        assertTrue(new ArrayList(Arrays.asList("geography", "location"))
                .equals(freebaseType.getCourseTypesByTitle(titleLoc)));

    }

    @Test
    public void testGetFineTypesByTitle(){

        assertTrue(new ArrayList(Arrays.asList("person.author", "person"))
                .equals(freebaseType.getFineTypesByTitle(titlePer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getFineTypesByTitle(titleNull)));
        assertTrue(new ArrayList(Arrays.asList("location", "geography.glacier"))
                .equals(freebaseType.getFineTypesByTitle(titleLoc)));
    }
}
