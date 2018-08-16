/*
 * Search and Destroy v1.0
 * @author Andrew Wasicek
 * Copyright (c) 2018 Andrew Wasicek
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * limitations under the License.
 */
package com.awasicek;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * Class providing verification utility methods for the Search and Destroy program.
 */
public final class VerificationUtil {

	private VerificationUtil() 
	{
	}

	// Utility method to verify command line arguments
	public static void checkCLArguments(String[] args) 
	{
		// Verify the correct number of arguments
		checkNumArgs(args);
		// Check if it is a directory or if it is a .txt file that exists and is readable and writable
		dirOrFileCheck(args[0]);
	}
	
	// Utility method that tests for a file
	public static boolean isFile(String pathName)
	{
		return new File(pathName).isFile();
	}
	
	// Utility method that tests for a directory
	public static boolean isDirectory(String pathName)
	{
		Path p = Paths.get(pathName);
		return Files.isDirectory(p);
	}
	
	// Overloaded utility method that tests for a directory
	public static boolean isDirectory(Path pathName)
	{
		return Files.isDirectory(pathName);
	}
	
	// Utility method that tests whether a file has the .txt extension
	public static boolean isTxtFile(String pathName)
	{
		String extension = "";
		int i = pathName.lastIndexOf(".");
		if (i >= 0) {
			extension = pathName.substring(i + 1);
		}
		if (extension.equals("txt")) {
			return true;
		} 
		else
		{
			return false;
		}
	}
	
	// Utility method that tests whether a file is readable and writable
	public static boolean canReadAndWrite(URI uri)
	{
		Path p = new File(uri).toPath();
		if (Files.isReadable(p) && Files.isWritable(p)) {
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// Overloaded utility method that tests whether a file is readable and writable
	public static boolean canReadAndWrite(Path pathName)
	{
		if (Files.isReadable(pathName) && Files.isWritable(pathName)) {
			return true;
		}
		else
		{
			return false;
		}
	}

	// Helper method that displays an error message and exits the program if the number of arguments is not 1 
	private static void checkNumArgs(String[] args) {
		int numArguments = args.length;
		if (numArguments != 1) {
			System.err.println(
					"Error: Search and Destroy v1.0 requires exactly one (1) command line argument specifying the text file or "
					+ "directory in which to search and destroy duplicate words.");
			System.exit(1);
		}
	}

	/*
	 *  Helper method that displays an appropriate error message and exits the program if the argument is not a 
	 *  directory or a readable/writable text file.
	 */
	private static void dirOrFileCheck(String commLineArg) {
		Path argPath = Paths.get(commLineArg);
		boolean isFile = isFile(commLineArg);
		boolean isDirectory = isDirectory(argPath);
		boolean isTxtFile = isTxtFile(commLineArg);

		// Check whether the argument provided is an existing directory or file
		if (!Files.exists(argPath))
		{
			System.err.println("Error: the command line argument passed to Search and Destroy v1.0 (" + commLineArg 
					+ ") is neither a file nor directory that exists.");
			System.exit(1);
		}
		
		// Check whether the argument provided is a directory or a file
		if (!isDirectory && !isFile) {
			System.err.println("Error: the command line argument passed to Search and Destroy v1.0 (" + commLineArg
					+ ") was neither a directory nor a file.");
			System.exit(1);
		}
		
		// Check if the file provided by the argument is readable and writable (also checks for existence)
		if(!canReadAndWrite(argPath))
		{
			System.err.println("Error: the file provided via command line argument (" + commLineArg
					+ ") to Search and Destroy v1.0 is not readable and/or writable."); 
			System.exit(1);
		}

		// Check if the file extension is .txt (also check if it is a file so this doesn't check directories for .txt extension)
		if (isFile && !isTxtFile)
		{
			System.err.println("Error: the command line argument passed to Search and Destroy v1.0 (" + commLineArg
				+ ") was not a valid .txt file.");
			System.exit(1);
		}
	}
}
