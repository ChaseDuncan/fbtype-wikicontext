package edu.illinois.cs.cogcomp.fbtype.resources;

import org.mapdb.*;

import java.io.File;
import java.io.IOException;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;

public class FreebaseTypeDB extends WikiDB{

    public FreebaseTypeDB(String dbFile){
        this(true, dbFile);
    }

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
        String configFile = "config/default.config";
        if(args.length > 0)
            configFile = args[0];
        ResourceManager rm = new ResourceManager(configFile);

        FreebaseTypeDB freebaseTypeDB = new FreebaseTypeDB(false, rm.getString("typeDbFile"));
        DBUtils.populateDB(freebaseTypeDB, rm.getString("freebaseTypeFile"));
    }
}
