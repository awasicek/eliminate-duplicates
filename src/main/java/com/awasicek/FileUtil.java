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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class FileUtil {

	private FileUtil()
	{
	}
	
	// Lists that will store the paths of directories or files that the program encounters for information display purposes upon program end.
	public static List<Path> filesList = new ArrayList<Path>();
	public static List<Path> dirList = new ArrayList<Path>();

	
	// Utility method that reads the contents of a text file and returns it as a string
	public static String readTextFile(Path pathName)
	{
		/*
		 * Reads all the bytes from a file. The readAllBytes method ensures that the file is closed when all bytes have been read or 
		 * an I/O error, or other runtime exception, is thrown. Note that this method is intended for simple cases where it is 
		 * convenient to read all bytes into a byte array. It is not intended for reading in large files. Since we are dealing 
		 * with .txt files it is a fair assumption that we will not run into memory issues (per documentation: OutOfMemoryError - 
		 * if an array of the required size cannot be allocated, for example the file is larger that 2GB). Moreover, reading
		 * all of the bytes at once, processing them, and writing over the file should be faster than the same process but with a buffer
		 * since the repetition is avoided.
		 */
		byte[] storage = null;
		try {
			/*
			 * Note: one of the reasons to read all of the text data at once is so that we can easily detect duplicates that span 
			 * across the amount read in (either a buffered amount or each line).  If size is an issue this can be refactored to read into
			 * a buffer, eliminate duplicates from the buffer, write the buffer to a temporary target file, repeat until the entire
			 * input file has been transferred via buffer to a temporary file and then write the contents of the temporary file to the
			 * input file itself (but this poses non-trivial issues for detecting duplicates that span across buffers).
			 */
			storage = Files.readAllBytes(pathName); 
		} 
		catch (IOException e) {
			System.err.println("Failed to process input file. " + e);
		}

		/*
		 * Note: It is also a fair assumption that the text files we will be dealing with will not contain more than 
		 * 2,147,483,647 (2^31 - 1) characters (note that the users heap size could also be a limiting factor, but assuming a typical 
		 * 32-bit system that still gives 2gb to work with (max heap size divided by 2 since each char is 2 bytes).  If the program must deal 
		 * with colossal text files, this can be refactored as mentioned above to use buffering.
		 */
		String bigString = new String(storage); // store the bytes read from the input file into a string
		return bigString;
	}
	
	// Utility method that writes a string to an output file
	public static void writeTextFile(String parsedText, Path outputFile)
	{
		// Note: used try with resources so no finally block is required to close the file because the decorators implement AutoCloseable
		try (PrintWriter out = new PrintWriter(new FileWriter(outputFile.toFile().getAbsolutePath()))) 
		{
				out.print(parsedText);
		}
		catch (IOException e)
		{
			System.err.println("Failed to process output file. " + e);
		}

	}
	
	/*
	 * Regex Description: (1) (?i) case insensitive, (2) \b asserts position at word boundary, (3) (\S+) is the first capturing group 
	 * that matches any non-whitespace character (don't use w+ so we can capture words with, e.g., hyphens and apostrophes) and + is greedy 
	 * (matches between 1 and unlimited), (4) \b asserts position at a word boundary, (5) (?:[!,.;\\?]) is the second capture group that 
	 * will match 0 or 1 punctuation marks as defined in the [] greedily, (6) beginning the second capture group, 
	 * \s+ matches any whitespace character * between one and unlimited times, (7) \1 is a backreference meaning it requires a match to
	 * the first group \1 and 0 or 1 punctuation marks (fourth capturing group) greedily. 
	 */	
	private static final String regExNoDups = "(?i)\\b(\\S+)\\b([!,.;\\?])?(\\s+\\1([!,.;\\?])?)+";
	/*
	 *  Compile the regex pattern here so that this only needs to be done once, not each time a file has its duplicates eliminated,
	 *  and so that we avoid passing the regex string into a replaceAll method call on the string being examined (which would require
	 *  the same sort of recompilation of the pattern).
	 */
	private static final Pattern NO_DUPS_PATTERN = Pattern.compile(regExNoDups);

	/*
	 * Utility method that eliminates duplicate words from a file (e.g., "This is an example example duplicate." becomes 
	 * "This is an example duplicate.")
	 */
	public static void elimDups(Path path)
	{				
		// Read the text file and store it in a string
		String fileText = FileUtil.readTextFile(path);
		Matcher m = NO_DUPS_PATTERN.matcher(fileText); // create a matcher that will match the file against the pattern
		while (m.find()) // as long a match is found, keep looping
		{
			String group1 = m.group(1); // the first instance of the word
			String group2 = m.group(2); // possible punctuation following first instance of the word
			String group4 = m.group(4); // punctuation following later duplicate
			String replacement = group1; // start building the replacement string
			/*
			 * Note we don't replace question marks because the first parameter of the replaceFirst method takes a regex
			 * and m.group() returns a String. For example, "marks marks?" will only match "marks marks" not "marks marks?"
			 * since ? needs to be escaped in a regex.  Note: with additional time, this should be refactored into a more robust
			 * solution that does not require this type of escaping for certain situations.
			 */
			if (group2 != null && !group2.equals("?")) // if any punctuation follows the first instance of the word then ...
			{
				// add the punctuation to the string to be used as the replacement
				replacement += group2;
				
			} 
			else if (group4 != null && !group4.equals("?") && group2 == null) // if any punctuation follows a later duplicate but not the first word then...
			{
				// add the punctuation to the string to be used as the replacement
				replacement += group4;
			}
			// When a duplicate is found the first instance of it (plus any punctuation) is used to replace the matching group
			fileText = fileText.replaceFirst(m.group(), replacement); 

		}
		// Write the processed string to the same file that the unprocessed string came from
		FileUtil.writeTextFile(fileText, path);
		// Add the file to the list of files that have been processed
		filesList.add(path);
		// Display exciting special effects to ensure maximum user engagement during processing
		InfoUtil.elimInfo(InfoUtil.useGraphics);
	}
	
	// Helper method that takes a path and adds it to the directory list for information display purposes
	private static void parseDirs(Path path)
	{
		if (VerificationUtil.isDirectory(path))
		{
			dirList.add(path);
		}
	}
	
	// Eliminates duplicate words from files in a given directory and its subdirectories
	public static void searchDirsAndDestroyDups(Path startingDir)
	{
		// Using functional interfaces for use in processing a stream of paths and instantiate with lambda expressions
	    Predicate<Path> isFilePredicate = p -> Files.isRegularFile(p); 
	    Predicate<Path> isTxtFilePredicate = p -> VerificationUtil.isTxtFile(p.toUri().toString());
	    Predicate<Path> canReadWritePredicate = p -> VerificationUtil.canReadAndWrite(p.toUri());
	    Consumer<Path> elimDupsConsumer = p -> FileUtil.elimDups(p);		    		
	    Consumer<Path> parseDirs = p -> FileUtil.parseDirs(p);
		
		try (Stream<Path> pathStream = Files.walk(startingDir))
		{
			/*
			 * (1) Peek at the streams and put directory path names into a list for later info purposes, (2) then filter out paths that
			 * are not files, (3) then filter out files that are not text files, (4) then filter out files that are not readable and
			 * writable, and lastly (4) for each of the remaining files eliminate duplicates.
			 */
			pathStream.peek(parseDirs).filter(isFilePredicate).filter(isTxtFilePredicate).filter(canReadWritePredicate).forEach(elimDupsConsumer);
		} 
		catch (IOException e) 
		{
			System.err.println("Failed to process path stream. " + e);
		}
	}

}
