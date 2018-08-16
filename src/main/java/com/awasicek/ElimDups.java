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

import java.nio.file.Path;
import java.nio.file.Paths;

public class ElimDups {
	static String pathStr = "";

	public static void main(String[] args) 
	{
		// Get user input on whether they want to start the program
		InfoUtil.bootReady();
		// Thrilling cutting edge introduction 
		InfoUtil.startingInfo(InfoUtil.useGraphics);
		// Verify the command line argument (and exit with error message if non-conforming)
		VerificationUtil.checkCLArguments(args);
		pathStr = args[0];
		new ElimDups().start();
	}

	// Runner method
	private void start() 
	{
		// If the command line argument is a .txt file (read, write, and .txt extension tested in main method), eliminate duplicates
		if (VerificationUtil.isFile(pathStr)) {
			Path p = Paths.get(pathStr);
			FileUtil.elimDups(p);
		}
		// If the command line argument is a directory, search it
		else if (VerificationUtil.isDirectory(pathStr)) 
		{
			Path startingDir = Paths.get(pathStr);
			// Walk through the directory tree and eliminate duplicates from text files that are readable and writable
			FileUtil.searchDirsAndDestroyDups(startingDir);		
		}
		
		/*
		 * Give the user information about the file(s) that had duplicates destroyed, and if only empty directories were found
		 * then give the user an appropriate message and all of the empty directories searched.
		 */
		InfoUtil.endReport();
		System.out.println("Exiting program.");
	}
}