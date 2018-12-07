package edu.illinois.cs.cogcomp.fbtype.resources;

import org.mapdb.DB;
import org.mapdb.HTreeMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DBUtils {

    static public void closeDB(DB db) {
        if (db != null && !db.isClosed()) {
            db.commit();
            db.close();
        }
    }

    static public void populateDB(WikiDB db, String dataFile)
            throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        String line = null;
        int c =0;
        int keyIdx = -1, valIdx = -1;

        if(db.getDBType().equals("id2type")){
            keyIdx = 0;
            valIdx = 1;
        } else if(db.getDBType().equals("title2id")){
            keyIdx = 1;
            valIdx = 0;
        }

        while ((line = br.readLine())!=null) {
            String[] ssplit = line.split("\t");
            c++;
            if (c % 100000 == 0)
            {
                System.out.println(c+" added to db.");
            }
            db.getMap().put(ssplit[keyIdx].trim(),ssplit[valIdx].trim());
        }
        br.close();
        closeDB(db.getDb());
    }
}
