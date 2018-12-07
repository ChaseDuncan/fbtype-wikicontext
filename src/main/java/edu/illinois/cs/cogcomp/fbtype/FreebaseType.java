package edu.illinois.cs.cogcomp.fbtype;

import com.sun.org.apache.regexp.internal.RE;
import edu.illinois.cs.cogcomp.fbtype.resources.FreebaseTypeDB;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class FreebaseType {
    private final boolean READ_ONLY = true;
    private final String dbFile = "/shared/experiments/cddunca2/freebase-type-project/db/types.db";
    private FreebaseTypeDB freebaseTypeDB = null;
    private TreeMap<String, String> titleMap = null;

    public FreebaseType(){
        freebaseTypeDB = new FreebaseTypeDB(READ_ONLY, dbFile);
    }

    public FreebaseType(boolean initTitleMap){

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
}
