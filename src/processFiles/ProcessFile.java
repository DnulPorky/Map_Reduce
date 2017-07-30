package processFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author JetLi 
 * Class that splits files into equally nr of lines
 * subparts(except last part that may be shorter).
 *
 * 
 */

public class ProcessFile {
	private long flag = 0;
	/*
	 * List containing all lines from file.
	 */
	private ArrayList<String> myList = new ArrayList<String>();

	public long getFlag() {
		return flag;
	}

	public ArrayList<String> getMyList() {
		return myList;
	}

	/**
	 * Creates an integer array containing processing limits(start-end line) for
	 * workers
	 * @param linesNr - total nr of file lines
	 * @param newSize - the size(how many lines) of the file subparts
	 * @return vector of limits(start-end lines)
	 */
	public int[] calculateDelimiters(int linesNr, int newSize) {
		int subpartsNr = 0;
		int[] delimiters = null;
		if (linesNr <= newSize) {
			delimiters = new int[] { 1, linesNr };
			return delimiters;
		}
		subpartsNr = linesNr / newSize;
		if ((linesNr % newSize) != 0) {
			subpartsNr++;
		}
		delimiters = new int[2 * subpartsNr];
		int start = 1;
		int end = newSize;
		int j = 0;
		for (int i = 0; i < subpartsNr; i++) {
			delimiters[j++] = start;
			delimiters[j] = end;
			start += newSize;
			end += newSize;
			if (end >= linesNr) {
				end = linesNr;
			}
			j++;
		}
		return delimiters;
	}

	/**
	 * Counts file's total nr of lines and creates an arraylist with file's lines 
	 * @param s - file's path
	 * @return - total nr of lines
	 */
	public int fileLineCounter(String s) {
		String name = s;
		File file;
		Scanner reader;
		int lineCounter = 0;

		try {
			file = new File(name);
			reader = new Scanner(file);
			while (reader.hasNextLine()) {
				lineCounter++;
				myList.add(reader.nextLine());
			}
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineCounter;
	}
}
