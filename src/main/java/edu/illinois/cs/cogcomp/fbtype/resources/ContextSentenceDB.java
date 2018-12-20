package edu.illinois.cs.cogcomp.fbtype.resources;


import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.File;
import java.io.IOException;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;

public class ContextSentenceDB extends WikiDB{

    /**
     * Public facing constructor.
     *
     * @param dbFile the file where the db data resides
     */
    public ContextSentenceDB(String dbFile){
        this(true, dbFile);
    }

    /**
     *  Constructs the database for accessing context sentences. This ctor
     *  is private because no outside class should be messing with the
     *  read_only flag.
     * @param read_only true is db is only being read
     * @param dbfile the file where the db data resides
     */
    private ContextSentenceDB(boolean read_only, String dbfile){
        dbType = "context-sentences";

        if (read_only) {
            db = DBMaker.fileDB(dbfile)
                    .fileChannelEnable()
                    .closeOnJvmShutdown()
                    .readOnly()
                    .make();
            map = db.hashMap("context-sentence")
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
            map = db.hashMap("context-sentence")
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
        ContextSentenceDB contextSentenceDB =
                new ContextSentenceDB(false, rm.getString("sentenceDbFile"));
        DBUtils.populateDB(contextSentenceDB, rm.getString("wikiDataDir"));
    }
}
