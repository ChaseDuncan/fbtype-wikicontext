package edu.illinois.cs.cogcomp.fbtype.resources;


import edu.illinois.cs.cogcomp.annotation.TextAnnotationBuilder;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.Pair;

import edu.illinois.cs.cogcomp.tokenizer.MultiLingualTokenizer;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.*;
import java.util.*;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import org.mapdb.serializer.SerializerArrayTuple;

import org.json.JSONObject;

import java.util.logging.Logger;

public class ContextSentenceDB {

    private final static Logger LOGGER = Logger.getLogger(ContextSentenceDB.class.getName());

    private DB db;
    private NavigableSet<Object[]> multimap = null;
    String titleIdDBFile;

    public ArrayList<Pair<Pair<Integer, Integer> , String>> getContext(String title){
       Set<Object[]> titleSubset = multimap.subSet(
               new Object[]{title},
               new Object[]{title, null, null, null});
       ArrayList<Pair<Pair<Integer, Integer>, String>> contexts =
               new ArrayList<>();
       for(Object[] context : titleSubset){
           Pair<Integer, Integer> offsets =
                   new Pair<Integer, Integer>((Integer)context[1], (Integer) context[2]);
           String sentence = (String) context[3];
           contexts.add(new Pair<Pair<Integer, Integer>, String>(offsets, sentence));
       }
        return contexts;
    }

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
        if (read_only) {
            db = DBMaker.fileDB(dbfile)
                    .fileChannelEnable()
                    .closeOnJvmShutdown()
                    .readOnly()
                    .make();
            multimap = db.treeSet("context-sentence")
                    .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.INTEGER, Serializer.INTEGER, Serializer.STRING))
                    .open();
        } else {
            if (new File(dbfile).exists()) {
                System.err.println("DB exists! " + dbfile);
                System.exit(-1);
            }
            db = DBMaker.fileDB(dbfile)
                    .closeOnJvmShutdown()
                    .make();
            multimap = db.treeSet("context-sentence")
                    .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.INTEGER, Serializer.INTEGER, Serializer.STRING))
                    .create();
        }
    }


        /**
     * The sentence DB doesn't follow the same pattern as the others. This function
     * handles the particulars of that DB.
     *
     * @param wikiDataDir the path to the Wikipedia data
     * @throws IOException
     */
    private void populateSentenceDB(String wikiDataDir, String language) throws IOException {

        File directory = new File(wikiDataDir);
        File[] jsonFiles = directory.listFiles();
        int c = 0;
        int total = jsonFiles.length;

        LOGGER.info("Building sentence database.");
        TreeMap<String, Integer> titleCounts = new TreeMap<>();
        for (File jsonFile : jsonFiles) {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            String line;
            TextAnnotationBuilder textAnnotationBuilder = MultiLingualTokenizer.getTokenizer(language);

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
                    String title = (String) jsonArray.get(offsetString);
                    String curEntry = null;

                    String[] offsetsSplit = offsetString.split(",");
                    int firstOffset = Integer.parseInt(offsetsSplit[0].substring(1));
                    int firstToken = ta.getTokenIdFromCharacterOffset(firstOffset);
                    if (firstToken == -1)
                        continue;
                    int secondOffset = Integer.parseInt(offsetsSplit[1].substring(1,
                            offsetsSplit[1].length() -1));
                    Sentence contextSentence = ta.getSentenceFromToken(firstToken);
                    String surface = ta.getText().substring(firstOffset, secondOffset);
                    firstOffset = contextSentence.getText().indexOf(surface);
                    secondOffset = firstOffset + surface.length();
                    multimap.add(new Object[]{title, firstOffset, secondOffset, contextSentence.getText()});
                    Integer curCount = titleCounts.get(title) ;
                    if(null == curCount) {
                        titleCounts.put(title, 1);
                    }else{
                        titleCounts.put(title, curCount+1);
                    }
                }
            }
            LOGGER.info("Completed " + c + " of " + total + ".");
            c++;
            BufferedWriter bw = new BufferedWriter(new FileWriter("counts"));
            for(Map.Entry<String, Integer> entry : titleCounts.entrySet()){
                if(entry.getValue() < 2)
                    continue;
                bw.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
            break;
        }
    }

    public static void main(String[] args) throws IOException {
        String configFile = args[0];

        ResourceManager rm = new ResourceManager(configFile);

        ContextSentenceDB contextSentenceDB =
                new ContextSentenceDB(false, rm.getString("sentenceDB"));
        contextSentenceDB.titleIdDBFile = rm.getString("titleIdDB");

        String language = "en";
        if(rm.containsKey("language"))
            language = rm.getString("language");

        contextSentenceDB.populateSentenceDB(rm.getString("wikiDataDir"), language);
    }
}
