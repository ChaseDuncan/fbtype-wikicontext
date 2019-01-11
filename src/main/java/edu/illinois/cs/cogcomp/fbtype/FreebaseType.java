package edu.illinois.cs.cogcomp.fbtype;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.fbtype.resources.FreebaseTypeDB;
import edu.illinois.cs.cogcomp.fbtype.resources.TitleIdDB;
import sun.misc.Resource;

import java.io.IOException;
import java.util.*;

public class FreebaseType {

    private final boolean READ_ONLY = true;
    private FreebaseTypeDB freebaseTypeDB = null;
    private TitleIdDB titleIdDB = null;

    public FreebaseType(String configFile) throws IOException {
        ResourceManager rm = new ResourceManager(configFile);
        initialize(rm);

    }

    private void initialize(ResourceManager rm){
        if(rm.containsKey("titleIdDB"))
            titleIdDB = new TitleIdDB(READ_ONLY, rm.getString("titleIdDB"));
        freebaseTypeDB = new FreebaseTypeDB(READ_ONLY, rm.getString("freebaseTypeDB"));
    }

    /**
     * Core function for retrieving types based on which subset of types is desired.
     *
     * @param typeString the string of type data which is stored in the DB
     * @param setType which type set to return either, "FINE", "CoNLL" or "COURSE"
     * @return
     */
    private ArrayList<String> getTypes(String typeString, String setType){
        if (typeString == null)
                return null;
        String[] fineTypes = typeString.split(" ");
        if(setType.equals("FINE"))
            return new ArrayList( Arrays.asList(fineTypes));
        HashSet<String> courseTypes = new HashSet<String>();
        for (String fineType : fineTypes){
            String courseType = fineType.split("\\.")[0];
            if(setType.equals("CoNLL") &&
                    (courseType.equals("location") || courseType.equals("person")
                            || courseType.equals("organization")))
                return new ArrayList<String>(Collections.singleton(courseType));
            courseTypes.add(courseType);
        }
        if(setType.equals("CoNLL"))
            return new ArrayList<>(Collections.singleton("miscellaneous"));
        return new ArrayList<>(courseTypes);
    }

    /**
     * Returns course Freebase types of Wikipedia curid.
     * @param curId  page to get types for
     * @return
     */
    public ArrayList<String> getCourseTypesById(String curId){
        return getTypes(freebaseTypeDB.getMap().get(curId), "COURSE");
    }

    /**
     * Returns fine Freebase types of Wikipedia curid.
     * @param curId  page to get types for
     * @return
     */
    public ArrayList<String> getFineTypesById(String curId){
        return getTypes(freebaseTypeDB.getMap().get(curId), "FINE");
    }

    /**
     * Returns CoNLL Freebase types of Wikipedia curid.
     * @param curId page to get types for
     * @return
     */
    public String getCoNLLTypeById(String curId){
        ArrayList<String> type = getTypes(freebaseTypeDB.getMap().get(curId), "CoNLL");
        if(type == null)
            return null;
        return type.get(0);
    }

    // The following 3 functions are as above but lookup by Wikipedia title
    public ArrayList<String> getCourseTypesByTitle(String title){
        String curId = titleIdDB.getCurId(title);
        return getTypes(freebaseTypeDB.getMap().get(curId), "COURSE");
    }

    public ArrayList<String> getFineTypesByTitle(String title){
        String curId = titleIdDB.getCurId(title);
        return getTypes(freebaseTypeDB.getMap().get(curId), "FINE");
    }

    public String getCoNLLTypeByTitle(String title){
        String curId = titleIdDB.getCurId(title);
        ArrayList<String> type = getTypes(freebaseTypeDB.getMap().get(curId), "CoNLL");
        if(type == null)
            return null;
        return type.get(0);
    }
}
