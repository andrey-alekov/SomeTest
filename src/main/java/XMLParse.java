import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created by Andre on 5/14/2015.
 */

public class XMLParse {

    private final static Logger LOGGER = Logger.getLogger(XMLParse.class.getName());

    /**
     * @param argv
     */
    public static void main(String[] argv) {

        Parser myParser = new Parser();
        if (argv.length >= 2) {
            LOGGER.info("Try to load XML file by path " + argv[0]);
            try {
                ArrayList<Vector> r = myParser.parse(new FileReader(argv[0]));
                LOGGER.info("Try to write results to file " + argv[1]);
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(argv[1]));
                    Long tags = 0L;
                    Long sum = 0L;
                    for (Vector v: r) {
                        out.write(v.toString());
                        out.newLine();
                        tags += (Long)v.get(0);
                        sum += (Long)v.get(1);
                    }
                    LOGGER.info("Total tags: " + tags + "\tTotal sum: " + sum);
                    out.close();
                    LOGGER.info("Parser results written to file " + argv[1]);
                }
                catch (IOException e){
                    LOGGER.warning("Error occurs during writing...");
                    e.printStackTrace();
                }
            }
            catch (FileNotFoundException e) {
                LOGGER.warning("File " + argv[0] + " not found!");
                e.printStackTrace();
            }
        }
        else {
            LOGGER.info("Usage: XMLParse <input.xml> <output.file>");
        }

    }
}