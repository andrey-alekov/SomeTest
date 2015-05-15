import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Andre on 5/15/2015.
 */

public class ParserTest extends TestCase {

    Parser p = new Parser();

    @Test
    public void testLineInvalidXML(){
        assertFalse(p.parseLine("123<xml></xml>123"));
        assertFalse(p.parseLine("123123"));
        assertFalse(p.parseLine(""));
    }

    @Test
    public void testLineValidXML(){
        assertTrue(p.parseLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>1<tag>2</tag><some>3</some></xml>"));
        assertTrue(p.parseLine("<xml>1<tag>2</tag><some>3</some></xml>"));
    }

    @Test
    public void testLineWithValues(){
        assertTrue(p.parseLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>1<tag>2</tag><some>3</some></xml>"));
        assertEquals(p.getXmlVector().get(0), (Long) 3L);
        assertEquals(p.getXmlVector().get(1), (Long) 6L);
        assertTrue(p.parseLine("<xml>1<tag>2</tag><some>3</some></xml>"));
        assertEquals(p.getXmlVector().get(0), (Long) 3L);
        assertEquals(p.getXmlVector().get(1), (Long) 6L);
    }

    @Test
    public void testLineWithoutValues(){
        assertTrue(p.parseLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><tag></tag><some></some></xml>"));
        assertEquals(p.getXmlVector().get(0), (Long) 3L);
        assertEquals(p.getXmlVector().get(1), (Long) 0L);
        assertTrue(p.parseLine("<xml><tag></tag><some></some></xml>"));
        assertEquals(p.getXmlVector().get(0), (Long) 3L);
        assertEquals(p.getXmlVector().get(1), (Long) 0L);
    }

    @Test
    public void testLineWithStringValues(){
        assertTrue(p.parseLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>ABC<tag>ABC</tag><some>ABC</some></xml>"));
        assertEquals(p.getXmlVector().get(0), (Long)3L);
        assertEquals(p.getXmlVector().get(1), (Long)0L);
        assertTrue(p.parseLine("<xml>QA<tag>QA</tag><some>QA</some></xml>"));
        assertEquals(p.getXmlVector().get(0), (Long)3L);
        assertEquals(p.getXmlVector().get(1), (Long)0L);

    }
}