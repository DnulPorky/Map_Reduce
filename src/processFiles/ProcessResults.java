package processFiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
/**
 * Class that executes the final part of results assembly and write it into output file.
 * @author JetLi
 *
 */
public class ProcessResults {
	OneFileResults[] results;
	File fileOut;
	String[] expressions;
	String[] files;
	OneFileResults[] newResults;
	Integer[] counters;

	public ProcessResults(OneFileResults[] results, File fileOut, String[] expressions, String[] files) {
		this.results = results;
		this.fileOut = fileOut;
		this.files = files;
		this.expressions = expressions;
		counters = new Integer[expressions.length];
	}
	/**
	 * Assembly final results
	 */
	public void mapReduce2() {
		int size = expressions.length;
		newResults = new OneFileResults[size];
		for (int i = 0; i < size; i++) {
			newResults[i] = new OneFileResults();
			newResults[i].setFileName(expressions[i]);
			HashMap<String, Integer[]> temporaryMap = new HashMap<String, Integer[]>();

			for (int j = 0; j < results.length; j++) {
				Integer[] vector = results[j].getResults().get(expressions[i]);
				Integer[] newVector = new Integer[vector.length - 1];
				System.arraycopy(vector, 1, newVector, 0, vector.length - 1);
				temporaryMap.put(files[j], newVector);
			}
			newResults[i].setResults(temporaryMap);
		}
	}
	/**
	 * Calculates expression encounters
	 */
	public void calculateCounters() {
		Integer[] counter = new Integer[expressions.length];
		for (int i = 0; i < counter.length; i++) {
			counter[i] = 0;
		}
		for (int i = 0; i < results.length; i++) {
			for (int j = 0; j < expressions.length; j++) {
				counter[j] += results[i].getResults().get(expressions[j])[0];
			}
		}
		this.counters = counter;
	}
	/**
	 * Writes final results into output file
	 */
	public void writeFileOut() {
		calculateCounters();
		FileWriter file;
		try{
			file = new FileWriter(fileOut);
			for (int i = 0; i < newResults.length; i++) {
				file.write(System.lineSeparator() + newResults[i].getFileName() + ":[" + counters[i] + "]");
				for (Map.Entry<String, Integer[]> entry : newResults[i].getResults().entrySet()) {
					if (entry.getValue().length > 0) {
						file.write(System.lineSeparator() + "[" + entry.getKey() + "]:");
						for (Integer n : entry.getValue()) {
							file.write(n + " ");
						}
					}
				}
			}
			if(file != null){
				file.close();	
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
