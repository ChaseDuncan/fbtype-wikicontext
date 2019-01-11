package edu.illinois.cs.cogcomp.fbtype.resources;

import org.mapdb.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;

public class FreebaseTypeDB {

    private final static Logger LOGGER = Logger.getLogger(FreebaseTypeDB.class.getName());
    private DB db;
    private HTreeMap<String, String> map;

    public HTreeMap<String, String> getMap(){
        return map;
    }

    /**
     * Cleanly closes db after population.
     *
     * @param db
     */
    private void closeDB(DB db) {
        if (db != null && !db.isClosed()) {
            db.commit();
            db.close();
        }
    }

    public FreebaseTypeDB(boolean read_only, String dbfile){
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

    /**
     * Builds the Type DB.
     *
     * @param dataFile the file to use for constructing the DB
     * @throws IOException
     */
     public void populateDB(String dataFile)
            throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        String line = null;
        int c = 0;

        int keyIdx = 0;
        int valIdx = 1;

        while ((line = br.readLine()) != null) {
            String[] ssplit = line.split("\t");
            c++;
            if (c % 100000 == 0) {
                LOGGER.info(c + " added to db.");
            }
            map.put(ssplit[keyIdx].trim(), ssplit[valIdx].trim());
        }

        br.close();
        closeDB(db);
    }

    public static void main(String[] args) throws IOException {
        String configFile = null;
        if(args.length > 0)
            configFile = args[0];
        ResourceManager rm = new ResourceManager(configFile);

        FreebaseTypeDB freebaseTypeDB = new FreebaseTypeDB(false, rm.getString("typeDbFile"));
        freebaseTypeDB.populateDB(rm.getString("freebaseTypeFile"));
    }
}
