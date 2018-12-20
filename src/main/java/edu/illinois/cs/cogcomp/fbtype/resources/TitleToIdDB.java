package edu.illinois.cs.cogcomp.fbtype.resources;

import org.mapdb.*;

import java.io.File;
import java.io.IOException;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;

public class TitleToIdDB extends WikiDB{
    // this is used in DBUtils to construct the sentence DB. kinda messy...
    public TitleToIdDB(){
        this(true, "/shared/experiments/cddunca2/freebase-type-project/db/title2id.db");
    }

    public TitleToIdDB(String dbfile){
        this(true, dbfile);
    }

    public TitleToIdDB(boolean read_only, String dbfile) {
        this.dbType = "title2id";
        if (read_only) {
            db = DBMaker.fileDB(dbfile)
                    .fileChannelEnable()
                    .closeOnJvmShutdown()
                    .readOnly()
                    .make();
            map = db.hashMap("title2id")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .open();
        } else {
            if (new File(dbfile).exists()) {
                System.err.println("DB exists! " + dbfile);
                System.exit(-1);
            }
            db = DBMaker.fileDB(dbfile)
                    .closeOnJvmShutdown()
                    .make();
            map = db.hashMap("title2id")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
        }
    }

    public static void main(String[] args) throws IOException {
        String configFile = "config/default.config";
        if(args.length > 0)
            configFile = args[0];
        ResourceManager rm = new ResourceManager(configFile);
        String title2idFile = "/shared/experiments/cddunca2/freebase-type-project/data/enwiki-20170520.id2t";
        String dbFile = "/shared/experiments/cddunca2/freebase-type-project/db/title2id.db";
        TitleToIdDB titleToIdDB = new TitleToIdDB(false, dbFile);
        DBUtils.populateDB(titleToIdDB, title2idFile);
    }
}
