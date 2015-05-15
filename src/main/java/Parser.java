import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Andre on 5/14/2015.
 */

public class Parser extends DefaultHandler {

    private final static Logger LOGGER = Logger.getLogger(XMLParse.class.getName());
    private BufferedReader reader;
    private final SAXParserFactory spf = SAXParserFactory.newInstance();
    private SAXParser saxParser;
    private Long tagsCount;
    private Long tagsValue;
    private Vector<Long> xmlVector;
    private ArrayList<Vector> result = new ArrayList<>();
    private XMLReader xmlReader;

    /**
     * @return
     */
    public Vector<Long> getXmlVector() {
        return xmlVector;
    }

    /**
     * Default constructor
     */
    public Parser(){
        try {
            this.saxParser = spf.newSAXParser();
            this.xmlReader = saxParser.getXMLReader();
        } catch (SAXException |ParserConfigurationException e) {
            LOGGER.warning("SAXParser creation exception.");
            e.printStackTrace();
        }
        xmlReader.setContentHandler(this);
    }

    /**
     * Handler for characters inside tags.
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            tagsValue += Long.parseLong(String.valueOf(new StringBuilder().append(new String(ch, start, length))));
        }
        catch (NumberFormatException e) {
            LOGGER.warning("Not number format in tag detected. Skipped in calculation.");
        }
    }

    /**
     * Handelr for startDocument
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        this.tagsCount = 0L;
        this.tagsValue = 0L;
        this.xmlVector = new Vector<>();
    }

    /**
     * Handler for startElement
     * @param namespaceURI
     * @param localName
     * @param qName
     * @param atts
     * @throws SAXException
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {
        this.tagsCount += 1L;
    }

    /**
     * Handler for endDocument.
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        LOGGER.info("Found tags: " + tagsCount + "; Tags sums: " + tagsValue);
        this.xmlVector.add(tagsCount);
        this.xmlVector.add(tagsValue);
        this.result.add(this.xmlVector);
        this.tagsCount = 0L;
        this.tagsValue = 0L;
    }

    /**
     * Parse text file with XML entities.
     * @param file FileReader
     * @return ArrayList with result.
     */
    public ArrayList<Vector> parse(FileReader file){
        String line;
        long successful = 0;
        long failed = 0;
        long total = 0;
        try {
            reader = new BufferedReader(file);
            while((line = reader.readLine()) != null) {
                if (this.parseLine(line) == true) {
                    successful += 1;
                }
                else {
                    failed += 1;
                }
                total += 1;
            }
        }
        catch (IOException e){
            LOGGER.warning("File cannot be processed... " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                this.reader.close();
            }
            catch (IOException e){
                LOGGER.warning("File cannot be processed... " + e.getMessage());
                e.printStackTrace();
            }
        }
        LOGGER.info("Total lines processed: " + total + "; OK: " + successful + "; Failed: " + failed);
        return this.result;
    }

    /**
     * Parse single line XML data. This method public just only for unit tests.
     * @param line String
     * @return boolean
     */
    public boolean parseLine(String line) {
        LOGGER.fine("Read line: " + line);
        try {
            xmlReader.parse(new InputSource(new ByteArrayInputStream(line.getBytes("utf-8"))));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}