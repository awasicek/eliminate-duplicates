package com.awasicek;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * ElimDups is a utility class providing test methods for verifying regex functionality
 */
public final class ElimDupsTest {
	
	private ElimDupsTest()
	{
	}
	
	public static String testElimDups(String filePath, String stringWithDups)
	{
		try (PrintWriter pw = new PrintWriter(ElimDupsTest.class.getClassLoader().getResource(filePath).getFile())) {
			// Opening and closing the file will effectively empty the given text file as per the documentation on PrintWriter:
			// If the file exists then it will be truncated to zero size
		} catch (IOException e)
		{
			System.err.println("[Test] Failed to process input file. " + e);
		}
		
		URI fileURI = null;
		try {
			fileURI = ElimDupsTest.class.getClassLoader().getResource(filePath).toURI();
		} catch (URISyntaxException e) {
			System.err.println("[Test] Problem getting file URI. " + e);
		}
		Path path = Paths.get(fileURI);

		// write the string that contains the test duplicates case into the file
		FileUtil.writeTextFile(stringWithDups, path);
				
		// run the elimDups method from the main application
		FileUtil.elimDups(path);
		
		String afterDupsRemoved = FileUtil.readTextFile(path);

		return afterDupsRemoved;
	}
	
}
