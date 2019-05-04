package CustomFunctions;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelpingFunctionsTest {

    @Test
    public void getMD5Encryption() {
        assertEquals("683baf9b925204422e6fd5bcb1506ff", HelpingFunctions.getMD5Encryption("Mahmoud1234567891"));
        assertEquals("b98acc94be7ec5125c48158deeb8a59b", HelpingFunctions.getMD5Encryption("Mahmoud123456789"));
        assertEquals("e10adc3949ba59abbe56e057f20f883e", HelpingFunctions.getMD5Encryption("123456"));
        assertEquals("66a8fe31bdf4e932d0cce045a8d59437", HelpingFunctions.getMD5Encryption("Geeks123"));
    }
}