package edu.illinois.cs.cogcomp.fbtype.resources;


import org.mapdb.DB;

import java.io.*;
import java.util.logging.Logger;


/**
 * This class implements a bunch of helper functions for constructing the DBs
 * which support this module.
 */
public class DBUtils {

    private final static Logger LOGGER = Logger.getLogger(DBUtils.class.getName());

    static public void closeDB(DB db) {
        if (db != null && !db.isClosed()) {
            db.commit();
            db.close();
        }
    }

    /**
     * Entry point for DB building.
     *
     * @param db the target DB to build
     * @param dataFile the file to use for constructing the DB
     * @throws IOException
     */
    static public void populateDB(WikiDB db, String dataFile)
            throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        String line = null;
        int c = 0;
        int keyIdx = -1, valIdx = -1;

        if (db.getDBType().equals("id2type")) {
            keyIdx = 0;
            valIdx = 1;
        } else if (db.getDBType().equals("title2id")) {
            keyIdx = 1;
            valIdx = 0;
        }

        while ((line = br.readLine()) != null) {
            String[] ssplit = line.split("\t");
            c++;
            if (c % 100000 == 0) {
                LOGGER.info(c + " added to db.");
            }
            db.getMap().put(ssplit[keyIdx].trim(), ssplit[valIdx].trim());
        }

        br.close();
        closeDB(db.getDb());
    }




    //LOGGER.info("Reading lang links " + file);
    //id2en = new HashMap<>();
    //try {
    //    InputStream in = new GZIPInputStream(new FileInputStream(file));
    //    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    //    String line = br.readLine();
    //    while (line != null) {
    //        if (!line.contains("INSERT INTO")) {
    //            line = br.readLine();
    //            continue;
    //        }
    //        int start = line.indexOf("(");
    //        line = line.substring(start + 1, line.length());
    //        String[] tokens = line.split("\\),\\(");
    //        for (String t : tokens) {
    //            String[] ts = t.split(",'");
    //            if (ts.length < 3) continue;
    //            String id = ts[0];
    //            String lang = ts[1].substring(0, ts[1].length()-1);
    //            String en = ts[2].substring(0, ts[2].length()-1);
    //            en = en.replaceAll("\\\\", "");
    //            if (lang.equals(target_lang))
    //                id2en.put(id, en);
    //        }
    //        line = br.readLine();
    //    }
    //} catch (IOException e) {
    //    e.printStackTrace();
    //}

    //LOGGER.info("id2en size " + id2en.size());
}
