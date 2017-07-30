
import java.io.File;
import java.util.*;

import processFiles.OneFileResults;
import processFiles.ProcessFile;
import processFiles.ProcessResults;
import workpool.*;

/**
 * 
 * ++Finding expressions using MapReduce paradigm++
 * 
 * This project purpose is to build a multithread software that search for
 * multiple expressions in multiple files/folders.The search and process of its
 * partial results are distributed using threads. In order to do this
 * parallelism of tasks I used the ReplicatedWorkers model and the MapReduce
 * paradigm. The software receives a list of expressions and a list of files
 * that are to be searched, along with the number of threads that will be created.It
 * will split all the files that are to be searched in subparts with specified
 * size(a fixed number of lines set by user, except the last parts that may be
 * smaller). Every subpart will be searched for every expression and will return
 * a partial result, how many times has found every expression and at which
 * lines. Than the partial results will be assembled for every file and in the
 * end we'll have the results for every expression, how many times it was found,
 * which files contained it and at which line. The final results will be written
 * in the specified output file.
 * 
 * @author JetLi
 *
 */

public class Main {

	public static void main(String args[]) throws InterruptedException {
		// nr of threads is read from argument set by user
		int nThreads = Integer.valueOf(args[0]);
		File fileIn;
		File fileOut;
		Scanner reader;
		int NL = 0;
		int NE = 0;
		int NF = 0;
		String[] expressions;
		String[] files;

		try {
			// input/output files are specified by user as arguments
			fileIn = new File(args[1]);
			fileOut = new File(args[2]);
			reader = new Scanner(fileIn);
			NL = reader.nextInt();
			NE = reader.nextInt();
			expressions = new String[NE];
			for (int i = 0; i < NE; i++) {
				expressions[i] = reader.next();
			}
			NF = reader.nextInt();
			OneFileResults[] results = new OneFileResults[NF];
			files = new String[NF];
			for (int i = 0; i < NF; i++) {
				files[i] = reader.next();
			}
			int counter = 0;
			/**
			 * For every file, a pool of workers is created, and the file is
			 * split into subparts that will be searched for expressions by a
			 * worker, and upload search results into a temporary collection,
			 * until there are no more subparts to be searched and workers are
			 * terminated.
			 */
			for (int j = 0; j < files.length; j++) {
				ProcessFile processFile = new ProcessFile();
				WorkPool wp = new WorkPool(nThreads);
				int lines = processFile.fileLineCounter(files[j]);
				int[] delimiters = processFile.calculateDelimiters(lines, NL);
				List<String> myList = processFile.getMyList();
				for (int i = 1; i < delimiters.length; i += 2) {
					wp.putWork(new Task(myList, expressions, delimiters[i - 1], delimiters[i]));
				}
				Worker[] wrk = new Worker[nThreads];
				for (int i = 0; i < nThreads; i++) {
					wrk[i] = new Worker(wp);
				}
				for (int i = 0; i < nThreads; i++) {
					wrk[i].start();
				}
				for (int i = 0; i < nThreads; i++) {
					wrk[i].join();
				}
				if (wp.finish) {
					HashMap<String, Integer[]> newMap = new HashMap<String, Integer[]>(Task.myMap);
					Task.myMap.clear();
					results[counter++] = new OneFileResults(files[j], newMap);
				}
			}
			/**
			 * Intermediary results are processed and final results are written
			 * in output file.
			 */
			ProcessResults pr = new ProcessResults(results, fileOut, expressions, files);
			pr.mapReduce2();
			pr.writeFileOut();
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
