package edu.illinois.cs.cogcomp.fbtype;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FreebaseTypeTest {
    FreebaseType freebaseType = new FreebaseType();
    @Test
    public void testGetCoNLLTypeByCurId(){
        String curIdPer = "15162268";  //person.author person
        String curIdNull = "38608933"; //written_work
        String curIdLoc = "29551290";  //location geography.glacier

        assertEquals(freebaseType.getCoNLLTypeById(curIdPer), "person");
        assertEquals(freebaseType.getCoNLLTypeById(curIdNull), null);
        assertEquals(freebaseType.getCoNLLTypeById(curIdLoc), "location");
    }

    @Test
    public void testGetCourseTypesById(){
        String curIdPer = "15162268";  //person.author person
        String curIdNull = "38608933"; //written_work
        String curIdLoc = "29551290";  //location geography.glacier

        assertTrue(new ArrayList(Arrays.asList("person"))
                .equals(freebaseType.getCourseTypesById(curIdPer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getCourseTypesById(curIdNull)));
        assertTrue(new ArrayList(Arrays.asList("geography", "location"))
                .equals(freebaseType.getCourseTypesById(curIdLoc)));

    }

    @Test
    public void testGetFineTypesById(){
        String curIdPer = "15162268";  //person.author person
        String curIdNull = "38608933"; //written_work
        String curIdLoc = "29551290";  //location geography.glacier

        assertTrue(new ArrayList(Arrays.asList("person.author", "person"))
                .equals(freebaseType.getFineTypesById(curIdPer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getFineTypesById(curIdNull)));
        assertTrue(new ArrayList(Arrays.asList("location", "geography.glacier"))
                .equals(freebaseType.getFineTypesById(curIdLoc)));
    }
}
