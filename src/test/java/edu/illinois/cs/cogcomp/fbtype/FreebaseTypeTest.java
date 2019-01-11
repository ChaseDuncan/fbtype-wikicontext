package edu.illinois.cs.cogcomp.fbtype;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FreebaseTypeTest {
    String config = "config/test.config";

    String curIdPer = "15162268";  //person.author person
    String curIdNull = "38608933"; //written_work
    //String curIdLoc = "29551290";  //location geography.glacier
    String curIdLoc = "19342760";  //location geography.glacier

    String titlePer = "Richard_Irving_Dodge";  //person.author person
    String titleNull = "The_Seven_Wonders_(Saylor_novel)"; //written_work
    //String titleLoc = "Mondor_Glacier";  //location geography.glacier

    String titleLoc = "Seven_Wonders_of_the_Ancient_World";  //location geography.glacier

    @Test
    public void testGetCoNLLTypeByCurId() throws IOException {
        FreebaseType freebaseType = new FreebaseType(config);
        assertEquals(freebaseType.getCoNLLTypeById(curIdPer), "person");
        assertEquals(freebaseType.getCoNLLTypeById(curIdNull), "miscellaneous");
        assertEquals(freebaseType.getCoNLLTypeById(curIdLoc), "location");
    }

    @Test
    public void testGetCourseTypesById() throws IOException {
        FreebaseType freebaseType = new FreebaseType(config);
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
    public void testGetFineTypesById() throws IOException {
        FreebaseType freebaseType = new FreebaseType(config);
        assertTrue(new ArrayList(Arrays.asList("person.author", "person"))
                .equals(freebaseType.getFineTypesById(curIdPer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getFineTypesById(curIdNull)));
        assertTrue(new ArrayList(Arrays.asList("location", "geography.glacier"))
                .equals(freebaseType.getFineTypesById(curIdLoc)));
    }

    @Test
    public void testGetCoNLLTypeByTitle() throws IOException {
        FreebaseType freebaseType = new FreebaseType(config);
        assertEquals(freebaseType.getCoNLLTypeByTitle(titlePer), "person");
        assertEquals(freebaseType.getCoNLLTypeByTitle(titleNull), "miscellaneous");
        assertEquals(freebaseType.getCoNLLTypeByTitle(titleLoc), "location");
    }

    @Test
    public void testGetCourseTypesTile() throws IOException {
        FreebaseType freebaseType = new FreebaseType(config);
        assertTrue(new ArrayList(Arrays.asList("person"))
                .equals(freebaseType.getCourseTypesByTitle(titlePer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getCourseTypesByTitle(titleNull)));
        assertTrue(new ArrayList(Arrays.asList("geography", "location"))
                .equals(freebaseType.getCourseTypesByTitle(titleLoc)));
    }

    @Test
    public void testGetFineTypesByTitle() throws IOException {
        FreebaseType freebaseType = new FreebaseType(config);
        assertTrue(new ArrayList(Arrays.asList("person.author", "person"))
                .equals(freebaseType.getFineTypesByTitle(titlePer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getFineTypesByTitle(titleNull)));
        assertTrue(new ArrayList(Arrays.asList("location", "geography.glacier"))
                .equals(freebaseType.getFineTypesByTitle(titleLoc)));
    }
}
