package edu.illinois.cs.cogcomp.fbtype.resources;

import org.mapdb.DB;
import org.mapdb.HTreeMap;

/**
 * Abstract super class of the various DB managing
 */
public class WikiDB {
    protected DB db;
    protected String dbType;
    public HTreeMap<String, String> map;

    public HTreeMap<String, String> getMap(){
        return map;
    }

    public DB getDb(){
        return db;
    }

    public String getDBType(){
        return dbType;
    }

}
