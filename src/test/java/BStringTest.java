import static org.junit.Assert.fail;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rla.board.main.BString;

public class BStringTest {

    private static final Logger logger = Logger.getLogger(BStringTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        BasicConfigurator.configure();
    }

    @Test
    public void testNewInstance() {
        try {
            BString bs = BString.newInstance();
            logger.info(bs.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNewInstanceInitied() {
        try {
            BString bs = BString.newInstance("bonjour Monsieur Dupond :-) comment allez-vous aujourd'hui ?!");
            logger.info(bs.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNewInstanceAdd() {
        try {
            BString bs = BString.newInstance();
            bs.add("bonjour Monsieur Dupont :-) comment allez-vous aujourd'hui ?!");
            logger.info(bs.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
