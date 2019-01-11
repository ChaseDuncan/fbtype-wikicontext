package edu.illinois.cs.cogcomp.fbtype.resources;

import org.mapdb.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import java.util.logging.Logger;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;

import static java.util.stream.Collectors.joining;

public class TitleIdDB {

    private final static Logger LOGGER = Logger.getLogger(TitleIdDB.class.getName());

    private DB db;

    private HTreeMap<String, String> titleToIdMap;
    private HTreeMap<String, String> idToTitleMap;

    public TitleIdDB(String dbfile){
        this(true, dbfile);
    }

    /**
     * Cleanly closes db after population.
     *
     * @param db
     */
    public void closeDB(DB db) {
        if (db != null && !db.isClosed()) {
            db.commit();
            db.close();
        }
    }

    /**
     * Get Wikipedia page title based on curid.
     *
     * @param curId id of page
     * @return Wikipedia page title, null if no matching title
     */
    public String getTitle(String curId){
        return idToTitleMap.get(curId);
    }

    /**
     * Get curId of page based on title.
     *
     * @param title Wikipedia page title
     * @return curid of page, null if no matching curid
     */
    public String getCurId(String title){
        return titleToIdMap.get(title);
    }

    public TitleIdDB(boolean read_only, String dbfile) {
        if (read_only) {
            db = DBMaker.fileDB(dbfile)
                    .fileChannelEnable()
                    .closeOnJvmShutdown()
                    .readOnly()
                    .make();
            titleToIdMap = db.hashMap("title2id")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .open();
            idToTitleMap = db.hashMap("id2title")
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
            titleToIdMap = db.hashMap("title2id")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
            idToTitleMap = db.hashMap("id2title")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
        }
    }

    /**
     * Constructs db which has two maps. One which maps Wikipedia titles to Ids and
     * one which maps Ids to titles.
     *
     * @param dataFile the file to use for constructing the DB
     * @throws IOException
     */
    public void populateDB(String dataFile)
            throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        String line = null;
        int c = 0;
        int curIdIdx= 0;
        int titleIdx = 1;

        while ((line = br.readLine()) != null) {
            String[] ssplit = line.split("\t");
            c++;
            if (c % 100000 == 0) {
                LOGGER.info(c + " added to db.");
            }
            titleToIdMap.put(ssplit[titleIdx].trim(), ssplit[curIdIdx].trim());
            idToTitleMap.put(ssplit[curIdIdx].trim(), ssplit[titleIdx].trim());
        }

        br.close();
        closeDB(db);
    }

    public static void main(String[] args) throws IOException {
        String configFile = args[0];
        ResourceManager rm = new ResourceManager(configFile);
        String pageFile = rm.getString("curIds2Title");
        String dbFile = rm.getString("titleIdDB");
        TitleIdDB titleIdDB = new TitleIdDB(false, dbFile);
        titleIdDB.populateDB(pageFile);
    }
}
