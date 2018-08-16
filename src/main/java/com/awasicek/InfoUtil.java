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
import java.util.Scanner;

public final class InfoUtil {
	
	public static boolean useGraphics = true; // used for controlling whether the super cool "graphics" are utilized
	
	private InfoUtil()
	{	
	}
	
	// Utility method that takes user input from console/command line and exits the program or allows it to continue
	public static void bootReady()
	{
		System.out.println("Initializing Search and Destroy v1.0 . . .");
		System.out.println("Are you sure you are ready to launch the ultimate computer program? (yes or no, defaults to no)");
		Scanner scanner = new Scanner(System.in); // open a scanner to read input
		String input = scanner.nextLine(); // take the next line of user input and store it in a string
		if (!input.equals("yes"))
		{
			System.out.println("Search and Destroy v1.0: You indicated you are not ready for the power of this software.  Come back when you are prepared.");
			System.exit(0);
		}
		System.out.println("Do you want to see cutting edge \"graphics\"? (yes or no, defaults to yes)");
		input = scanner.nextLine(); // take the next line of user input and store it in a string
		if (input.equals("no"))
		{
			useGraphics = false; // toggle the graphics state off
		}
		scanner.close(); // close the scanner
	}
	
	// Utility method that makes the start of the program ultra-exciting
	public static void startingInfo(boolean useGraphics)
	{
		if (useGraphics)
		{
			try
			{
				System.out.println("Search and Destroy v1.0: creating shredder drones . . .");
				Thread.sleep(700);
				System.out.println("Search and Destroy v1.0: powering up exterminator nanobots . . .");
				Thread.sleep(700);
				System.out.println("Search and Destroy v1.0: arming annihilator wardroids . . .");
				Thread.sleep(1000);
				System.out.println("Search and Destroy v1.0: verifying puny mortal's targeting data . . .");
				Thread.sleep(1200);
			}
			catch (InterruptedException e)
			{
				System.err.println("Error pausing program: " + e);
			}	
		}
	}
	
	// Utility method that makes the process of eliminating duplicates ultra-exciting too
	public static void elimInfo(boolean useGraphics)
	{
		if (useGraphics)
		{
			try
			{
				System.out.println("	bzzzzzzt . . .");
				Thread.sleep(700);
				System.out.println("		'aahhhh! they're coming!' *noises of duplicates running* . . .");
				Thread.sleep(700);
				System.out.println("			snip, vzzzt, slash . . .");
				Thread.sleep(1000);
				System.out.println("				kaboom *mushroom cloud* . . .");
				Thread.sleep(1200);
			}
			catch (InterruptedException e)
			{
				System.err.println("Error pausing program: " + e);
			}
		}
	}
	
	/*
	 * Utility method that displays information about the file(s) that had duplicates eliminated, or the directory structure if
	 * all no readable/writable text files were found in the directory or subdirectories.
	 */
	public static void endReport()
	{
		if (!FileUtil.filesList.isEmpty())
		{
			System.out.println("Success: Search and Destroy v1.0 successfully eliminated duplicates from the following files:");
			for(Path p : FileUtil.filesList)
			{
				System.out.println(p.toString());
			}
		} 
		else
		{
			System.out.println("No Files Found: Search and Destroy v1.0 did not locate any readable/writable text files in the following directories:");
			for(Path p : FileUtil.dirList)
			{
				System.out.println(p.toString());
			}
		}
	}
	
}
