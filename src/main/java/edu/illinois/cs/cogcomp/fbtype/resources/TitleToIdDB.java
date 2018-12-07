package edu.illinois.cs.cogcomp.fbtype.resources;
import org.mapdb.*;

import java.io.File;
import java.io.IOException;

public class TitleToIdDB extends WikiDB{


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
        String title2idFile = "/shared/experiments/cddunca2/freebase-type-project/data/enwiki-20170520.id2t";
        String dbFile = "/shared/experiments/cddunca2/freebase-type-project/db/title2id.db";
        TitleToIdDB titleToIdDB = new TitleToIdDB(false, dbFile);
        DBUtils.populateDB(titleToIdDB, title2idFile);
    }
}
