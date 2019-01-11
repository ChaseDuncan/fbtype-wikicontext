package edu.illinois.cs.cogcomp.fbtype;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.fbtype.resources.InterlanguageLinksDB;
import edu.illinois.cs.cogcomp.fbtype.resources.TitleIdDB;

import java.io.IOException;
import java.util.ArrayList;

public class MultilingualFreebaseType extends FreebaseType{
    InterlanguageLinksDB interlanguageLinksDB = null;
    TitleIdDB targetTitleIdDB = null;
    public MultilingualFreebaseType(String configFile) throws IOException {
        super(configFile);
        ResourceManager rm = new ResourceManager(configFile);
        interlanguageLinksDB =
                new InterlanguageLinksDB(true, rm.getString("interlanguageLinksDB"));
        targetTitleIdDB = new TitleIdDB(true, rm.getString("targetTitleIdDB"));
    }

    /**
     * Returns course Freebase types of Wikipedia curid.
     * @param curId  page to get types for
     * @return
     */
    public ArrayList<String> getCourseTypesById(String curId){
        String enTitle = interlanguageLinksDB.getENTitle(curId);
        return super.getCourseTypesByTitle(enTitle);
    }

    /**
     * Returns fine Freebase types of Wikipedia curid.
     * @param curId  page to get types for
     * @return
     */
    public ArrayList<String> getFineTypesById(String curId){
        String enTitle = interlanguageLinksDB.getENTitle(curId);
        return super.getFineTypesByTitle(enTitle);
    }

    /**
     * Returns CoNLL Freebase types of Wikipedia curid.
     * @param curId page to get types for
     * @return
     */
    public String getCoNLLTypeById(String curId){
        String enTitle = interlanguageLinksDB.getENTitle(curId);
        return super.getCoNLLTypeByTitle(enTitle);
    }

    // The following 3 functions are as above but lookup by Wikipedia title
    public ArrayList<String> getCourseTypesByTitle(String title){
        String targetCurId = targetTitleIdDB.getCurId(title);
        String enTitle = interlanguageLinksDB.getENTitle(targetCurId);
        return super.getCourseTypesByTitle(enTitle);
    }

    public ArrayList<String> getFineTypesByTitle(String title){
        String targetCurId = targetTitleIdDB.getCurId(title);
        String enTitle = interlanguageLinksDB.getENTitle(targetCurId);
        return super.getFineTypesByTitle(enTitle);
    }

    public String getCoNLLTypeByTitle(String title){
        String targetCurId = targetTitleIdDB.getCurId(title);
        if(targetCurId == null)
            return null;
        String enTitle = interlanguageLinksDB.getENTitle(targetCurId);
        if(null == enTitle)
            return null;
        return super.getCoNLLTypeByTitle(enTitle);
    }
}
