package com.awasicek;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testApp()
    {
    	InfoUtil.useGraphics = false; // don't use "graphics" for testing
    	String testString = "This is is an example example of duplicate.\n" + 
    			"This is is is is is is is an example example example of duplicate.\n" + 
    			"This is a very long and winded example that will check to see if you are planning\n" + 
    			"planning to check duplicates between multiple lines.\n" + 
    			"For another example example, here is a duplicate with punctuation marks\n" + 
    			"marks. This example checks semicolons semicolons; and exclamations exclamations!  While this example checks \n" + 
    			"question marks marks? This is an example EXAMPLE of duplicate.\n" + 
    			"For another example, example here is a duplicate with punctuation marks. marks.";
    	String verificationString = "This is an example of duplicate.\n" + 
    			"This is an example of duplicate.\n" + 
    			"This is a very long and winded example that will check to see if you are planning to check duplicates between multiple lines.\n" + 
    			"For another example, here is a duplicate with punctuation marks. This example checks semicolons; and exclamations!  While this example checks \n" + 
    			"question marks? This is an example of duplicate.\n" + 
    			"For another example, here is a duplicate with punctuation marks.";
        assertEquals(ElimDupsTest.testElimDups("test.txt", testString), verificationString);
    }
}
