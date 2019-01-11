package edu.illinois.cs.cogcomp.fbtype.resources;

import org.mapdb.*;

import java.io.*;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import java.util.logging.Logger;

import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;

import static java.util.stream.Collectors.joining;

public class InterlanguageLinksDB {

    private final static Logger LOGGER = Logger.getLogger(TitleIdDB.class.getName());

    private DB db;

    private HTreeMap<String, String> en2tid;
    private HTreeMap<String, String> tid2en;

    public InterlanguageLinksDB(boolean read_only, String dbfile) {
        if (read_only) {
            db = DBMaker.fileDB(dbfile)
                    .fileChannelEnable()
                    .closeOnJvmShutdown()
                    .readOnly()
                    .make();
            en2tid = db.hashMap("en2tid")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .open();
            tid2en = db.hashMap("tid2en")
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
            en2tid = db.hashMap("en2tid")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
            tid2en = db.hashMap("tid2en")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
        }
    }

    /**
     * Count the number of lines in the file so we can provide information about the progress
     * of the parsing.
     * @param  langLinkFile compressed interlanguage link file from Wikipedia
     *
     * @return number of lines in the file
     */
    private int countLines(String langLinkFile) throws IOException {
        InputStream in = new GZIPInputStream(new FileInputStream(langLinkFile));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int lines = 0;
        while (br.readLine() != null) lines++;
        br.close();
        return lines;
    }

    private void populateDB(String langLinkFile) throws IOException {
        InputStream in = new GZIPInputStream(new FileInputStream(langLinkFile));
        int lineCt = countLines(langLinkFile);
        LOGGER.info("Reading lang links " + langLinkFile + ". There are " + lineCt + " lines in the file.");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int curLineCt = 0;
        String line;
        while ((line = br.readLine()) != null) {
            curLineCt+=1;

            double complete = 100*(curLineCt / lineCt);
            LOGGER.info("Processing line " + curLineCt + " of " + lineCt + ". " + complete + "% complete.");
            if (!line.contains("INSERT INTO")) {
                continue;
            }
            int start = line.indexOf("(");
            line = line.substring(start + 1, line.length());
            String[] tokens = line.split("\\),\\(");
            for (String t : tokens) {
                String[] ts = t.split(",'");
                if (ts.length < 3) continue;
                String id = ts[0];
                String lang = ts[1].substring(0, ts[1].length()-1);
                String title = ts[2].substring(0, ts[2].length()-1);
                title = title.replaceAll("\\\\", "");
                if (lang.equals("en") && !title.equals("")) {
                    // replace whitespace with underscores in title
                    String[] splitTitle = title.split("\\s+");
                    title = Arrays.asList(splitTitle).stream().collect(joining("_"));
                    tid2en.put(id, title);
                    en2tid.put(title, id);
                }
            }
        }
    }

    /**
     * Retrieves English Wikipedia title based on target language curid.
     * @param tid target language curid
     * @return EN Wikipedia title or null if none exists
     */
    public String getENTitle(String tid){
        return tid2en.get(tid);
    }

    /**
     * Retrieve target language curid based on English Wikipedia title
     *
     * @param title English Wikipedia title
     * @return target language curid or null if there is no link
     */
    public String getTargetCurId(String title){
        return en2tid.get(title);
    }

    /**
     * For building an interlanguage link db.
     *
     * @param args arg[0] needs to be the config file.
     */
    public static void main(String[] args) throws IOException {
        String configFile = args[0];
        ResourceManager rm = new ResourceManager(configFile);

        InterlanguageLinksDB interlanguageLinksDB =
                new InterlanguageLinksDB(false, rm.getString("interlanguageLinkDB"));

        interlanguageLinksDB.populateDB(rm.getString("wikipediaLangLinksFile"));
    }
}
