package edu.illinois.cs.cogcomp.fbtype.resources;

import edu.illinois.cs.cogcomp.annotation.TextAnnotationBuilder;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.nlp.tokenizer.StatefulTokenizer;
import edu.illinois.cs.cogcomp.nlp.utility.TokenizerTextAnnotationBuilder;
import org.mapdb.DB;
import org.json.JSONObject;

import java.io.*;
import java.util.Iterator;
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
        if (db.getDBType().equals("context-sentences")){
            populateSentenceDB(db, dataFile);
        } else {
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
        }
        closeDB(db.getDb());
    }

    /**
     * The sentence DB doesn't follow the same pattern as the others. This function
     * handles the particulars of that DB.
     *
     * @param db the DB to build
     * @param wikiDataDir the path to the Wikipedia data
     * @throws IOException
     */
    private static void populateSentenceDB(WikiDB db, String wikiDataDir) throws IOException {

        TitleToIdDB titleToIdDB = new TitleToIdDB();
        File directory = new File(wikiDataDir);
        File[] jsonFiles = directory.listFiles();
        int c = 0;
        int total = jsonFiles.length;

        LOGGER.info("Building sentence database.");

        for (File jsonFile : jsonFiles) {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            String line;
            TextAnnotationBuilder textAnnotationBuilder =
                    new TokenizerTextAnnotationBuilder(new StatefulTokenizer());

            while ((line = br.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(line);
                String text = jsonObject.getString("text");

                if (text.equals(""))
                    continue;

                TextAnnotation ta = textAnnotationBuilder.createTextAnnotation(text);

                JSONObject jsonArray = jsonObject.getJSONObject("hyperlinks");
                Iterator<String> offsets = jsonArray.keys();
                while (offsets.hasNext()) {
                    String offsetString = offsets.next();
                    String curId = titleToIdDB.getMap().get(jsonArray.get(offsetString));
                    if(curId == null )
                        continue;

                    String curEntry = null;
                    if(db.getMap().get(curId) != null)
                        curEntry = db.getMap().get(curId);

                    if (curEntry != null)
                        continue;

                    String[] offsetsSplit = offsetString.split(",");
                    int firstOffset = Integer.parseInt(offsetsSplit[0].substring(1));
                    int firstToken = ta.getTokenIdFromCharacterOffset(firstOffset);
                    if (firstToken == -1)
                        continue;
                    int secondOffset = Integer.parseInt(offsetsSplit[1].substring(1,
                            offsetsSplit[1].length() -1));
                    String surface = ta.getText().substring(firstOffset, secondOffset);
                    Sentence contextSentence = ta.getSentenceFromToken(firstToken);

                    String entry = String.format("%s\t%s", surface, contextSentence);

                    // TODO: replace with multimap. this method was way too slow.
                    //if(curEntry != null) {
                    //    entry = curEntry + "|" + entry;
                    //}

                    db.getMap().put(curId, entry);
                }
            }
            LOGGER.info("Completed " + c + " of " + total + ".");
            c++;
        }
    }
}
