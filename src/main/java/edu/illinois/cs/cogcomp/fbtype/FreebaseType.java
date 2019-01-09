package edu.illinois.cs.cogcomp.fbtype;

import edu.illinois.cs.cogcomp.fbtype.resources.FreebaseTypeDB;
import edu.illinois.cs.cogcomp.fbtype.resources.TitleIdDB;

import java.util.*;

public class FreebaseType {

    private final boolean READ_ONLY = true;
    private final String typeDB = "/shared/experiments/cddunca2/freebase-type-project/db/types.db";
    private final String titleDB = "/shared/experiments/cddunca2/freebase-type-project/db/title2id.db";
    private FreebaseTypeDB freebaseTypeDB = null;
    private TitleIdDB titleIdDB = null;

    public FreebaseType(){
        this(true);
    }

    public FreebaseType(boolean initTitleToIdDB){
        if(initTitleToIdDB)
            titleIdDB = new TitleIdDB(READ_ONLY, titleDB);
        freebaseTypeDB = new FreebaseTypeDB(READ_ONLY, typeDB);
    }

    /**
     * Core function for retrieving types based on which subset of types is desired.
     *
     * @param typeString the string of type data which is stored in the DB
     * @param setType which type set to return either, "FINE", "CoNLL" or "COURSE"
     * @return
     */
    private ArrayList<String> getTypes(String typeString, String setType){
        if (typeString.equals(null))
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
            return null;
        assert setType.equals("COURSE");
        return new ArrayList<String>(courseTypes);
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
