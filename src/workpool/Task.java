package workpool;

import java.util.*;
/**
 * Class that searches for expressions into file's subparts and creates partial result.
 * @author JetLi
 *
 */
public class Task {
	public static HashMap<String, Integer[]> myMap = new HashMap<String, Integer[]>();
	private static final Object LOCK = new Object();
	List<String> list;
	String[] expressions;
	int start;
	int end;

	public Task(List<String> list, String[] expressions, int start, int end) {
		this.list = list;
		this.expressions = expressions;
		this.start = start;
		this.end = end;
	}
	
	public void map() {
		processTask(this.list, this.expressions, this.start, this.end);
	}
	
	public void processTask(List<String> list, String[] expressions, int start, int end){
		for(String expression : expressions){
			processExpression (list, expression, start, end);
		}
	}
	/**
	 * 
	 * @param list - the current file 
	 * @param expression - to be searched
	 * @param start - line nr
	 * @param end - line nr
	 * @return - a pair: searched expression and integer array, 
	 * first integer is the number of expression encounters between start and end, 
	 * and rest of the array - lines nr 
	 */
	public HashMap<String, Integer[]> processExpression(List<String> list, String expression, int start, int end) {
		synchronized (LOCK) {
				ArrayList<Integer> resultList = new ArrayList<Integer>();
				int matches = 0;
				resultList.add(matches);
				for (int i = start - 1; i <= end - 1; i++) {
					if (list.get(i).contains(expression)) {
						matches++;
						resultList.add(i + 1);
					}
				}
				resultList.set(0, matches);
				HashMap<String, Integer[]> pair = new HashMap<String, Integer[]>();
				Integer[] result = resultList.toArray(new Integer[resultList.size()]);
				pair.put(expression, result);
				reduce(pair);
				return pair;
		}
	}
	/**
	 * Reduce1 job - puts every search results into a larger partial results collection for every file
	 * @param pair - expression and its results(nr of encounters and lines nr)
	 */
	public void reduce(HashMap<String, Integer[]> pair) {
		synchronized (LOCK) {

			String expression1 = (String) pair.keySet().toArray()[0];
			Integer[] vector = pair.get(expression1);
			if (myMap.containsKey(expression1) && myMap.get(expression1)[0] != 0) {
				Integer[] old = myMap.get(expression1);
				int sizeOld = old.length;
				int sizeResult = vector.length;
				Integer[] newVector = new Integer[sizeOld + sizeResult - 1];
				newVector[0] = old[0] + vector[0];
				System.arraycopy(old, 1, newVector, 1, sizeOld - 1);
				System.arraycopy(vector, 1, newVector, sizeOld, sizeResult - 1);
				myMap.put(expression1, newVector);
			} else {
				myMap.put(expression1, vector);
			}

		}
	}
}