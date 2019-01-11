package edu.illinois.cs.cogcomp.fbtype;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MultilingualFreebaseTypeTest {
    String config = "config/es-test.config";
    String curIdPer = "9382";  //person.author person
    String curIdMisc = "7528159"; //written_work
    String curIdLoc = "5492009";  //location

    String titlePer = "Alan_Turing";  //person.author person
    String titleMisc = "Las_siete_maravillas"; //written_work
    String titleLoc = "Teatro_Ruth_Escobar";  //location


    @Test
    public void testGetCoNLLTypeByCurId() throws IOException {
        MultilingualFreebaseType freebaseType = new MultilingualFreebaseType(config);

        assertEquals(freebaseType.getCoNLLTypeById(curIdPer), "person");
        assertEquals(freebaseType.getCoNLLTypeById(curIdMisc), "miscellaneous");
        assertEquals(freebaseType.getCoNLLTypeById(curIdLoc), "location");
    }

    @Test
    public void testGetCourseTypesById() throws IOException {
        MultilingualFreebaseType freebaseType = new MultilingualFreebaseType(config);

        assertTrue(new ArrayList(Arrays.asList("person"))
                .equals(freebaseType.getCourseTypesById(curIdPer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getCourseTypesById(curIdMisc)));
        assertTrue(new ArrayList(Arrays.asList("location"))
                .equals(freebaseType.getCourseTypesById(curIdLoc)));
    }

    @Test
    public void testGetFineTypesById() throws IOException {
        MultilingualFreebaseType freebaseType = new MultilingualFreebaseType(config);

        assertTrue(new ArrayList(Arrays.asList("person.author", "person.athlete", "person"))
                .equals(freebaseType.getFineTypesById(curIdPer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getFineTypesById(curIdMisc)));
        assertTrue(new ArrayList(Arrays.asList("location"))
                .equals(freebaseType.getFineTypesById(curIdLoc)));
    }

    @Test
    public void testGetCoNLLTypeByTitle() throws IOException {
        MultilingualFreebaseType freebaseType = new MultilingualFreebaseType(config);

        assertEquals(freebaseType.getCoNLLTypeByTitle(titlePer), "person");
        assertEquals(freebaseType.getCoNLLTypeByTitle(titleMisc), "miscellaneous");
        assertEquals(freebaseType.getCoNLLTypeByTitle(titleLoc), "location");
    }

    @Test
    public void testGetCourseTypesTitle() throws IOException {
        MultilingualFreebaseType freebaseType = new MultilingualFreebaseType(config);

        assertTrue(new ArrayList(Arrays.asList("person"))
                .equals(freebaseType.getCourseTypesByTitle(titlePer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getCourseTypesByTitle(titleMisc)));
        assertTrue(new ArrayList(Arrays.asList("location"))
                .equals(freebaseType.getCourseTypesByTitle(titleLoc)));
    }

    @Test
    public void testGetFineTypesByTitle() throws IOException {
        MultilingualFreebaseType freebaseType = new MultilingualFreebaseType(config);
        assertTrue(new ArrayList(Arrays.asList("person.author", "person.athlete", "person"))
                .equals(freebaseType.getFineTypesByTitle(titlePer)));
        assertTrue(new ArrayList(Arrays.asList("written_work"))
                .equals(freebaseType.getFineTypesByTitle(titleMisc)));
        assertTrue(new ArrayList(Arrays.asList("location"))
                .equals(freebaseType.getFineTypesByTitle(titleLoc)));
    }

}
