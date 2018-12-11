package edu.illinois.cs.cogcomp.fbtype;

import com.sun.org.apache.regexp.internal.RE;
import edu.illinois.cs.cogcomp.fbtype.resources.FreebaseTypeDB;
import edu.illinois.cs.cogcomp.fbtype.resources.TitleToIdDB;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class FreebaseType {
    private final boolean READ_ONLY = true;
    private final String typeDB = "/shared/experiments/cddunca2/freebase-type-project/db/types.db";
    private final String titleDB = "/shared/experiments/cddunca2/freebase-type-project/db/title2id.db";
    private FreebaseTypeDB freebaseTypeDB = null;
    private TitleToIdDB titleToIdDB = null;

    public FreebaseType(){
        this(true);
    }

    public FreebaseType(boolean initTitleToIdDB){
        if(initTitleToIdDB)
            titleToIdDB = new TitleToIdDB(READ_ONLY, titleDB);
        freebaseTypeDB = new FreebaseTypeDB(READ_ONLY, typeDB);
    }

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

    public ArrayList<String> getCourseTypesById(String curId){
        return getTypes(freebaseTypeDB.getMap().get(curId), "COURSE");
    }

    public ArrayList<String> getFineTypesById(String curId){
        return getTypes(freebaseTypeDB.getMap().get(curId), "FINE");
    }

    public String getCoNLLTypeById(String curId){
        ArrayList<String> type = getTypes(freebaseTypeDB.getMap().get(curId), "CoNLL");
        if(type == null)
            return null;
        return type.get(0);
    }

    public ArrayList<String> getCourseTypesByTitle(String title){
        String curId = titleToIdDB.getMap().get(title);
        return getTypes(freebaseTypeDB.getMap().get(curId), "COURSE");
    }

    public ArrayList<String> getFineTypesByTitle(String title){
        String curId = titleToIdDB.getMap().get(title);
        return getTypes(freebaseTypeDB.getMap().get(curId), "FINE");
    }

    public String getCoNLLTypeByTitle(String title){
        String curId = titleToIdDB.getMap().get(title);
        ArrayList<String> type = getTypes(freebaseTypeDB.getMap().get(curId), "CoNLL");
        if(type == null)
            return null;
        return type.get(0);
    }
}
