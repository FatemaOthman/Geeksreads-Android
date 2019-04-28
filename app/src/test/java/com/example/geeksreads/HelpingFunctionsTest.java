package com.example.geeksreads;

import org.junit.Test;
import java.HelpingFunctions;
import static org.junit.Assert.*;

public class HelpingFunctionsTest {

    @Test
    public void getMD5EncryptionTest() {
        System.out.println(HelpingFunctions.getMD5Encryption("Mah123"));
        assertEquals("b98acc94be7ec5125c48158deeb8a59b", HelpingFunctions.getMD5Encryption("Mahmoud123456789"));
    }
}