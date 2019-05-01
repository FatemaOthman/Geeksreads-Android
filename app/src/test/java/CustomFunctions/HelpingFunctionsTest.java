package CustomFunctions;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelpingFunctionsTest {

    @Test
    public void getMD5Encryption() {
        System.out.println("Mahmoud1234567891 : " + HelpingFunctions.getMD5Encryption("Mahmoud1234567891"));
        System.out.println("Mahmoud123456789 : " + HelpingFunctions.getMD5Encryption("Mahmoud123456789"));
        System.out.println("Mah1142020 : " + HelpingFunctions.getMD5Encryption("Mah1142020"));
    }
}