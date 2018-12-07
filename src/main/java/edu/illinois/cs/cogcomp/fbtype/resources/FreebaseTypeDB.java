package edu.illinois.cs.cogcomp.fbtype.resources;

import org.mapdb.*;

import java.io.File;
import java.io.IOException;

public class FreebaseTypeDB extends WikiDB{

    public FreebaseTypeDB(boolean read_only, String dbfile){
        dbType = "id2type";
        if (read_only) {
            db = DBMaker.fileDB(dbfile)
                    .fileChannelEnable()
                    .closeOnJvmShutdown()
                    .readOnly()
                    .make();
            map = db.hashMap("curid2type")
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
            map = db.hashMap("curid2type")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
        }
    }


    public static void main(String[] args) throws IOException {
        String freebaseTypeFile = "/shared/experiments/cddunca2/freebase-type-project/data/wid.fbtypelabels";
        String dbFile = "/shared/experiments/cddunca2/freebase-type-project/db/types.db";
        FreebaseTypeDB freebaseTypeDB = new FreebaseTypeDB(false, dbFile);
        DBUtils.populateDB(freebaseTypeDB, freebaseTypeFile);
    }
}
