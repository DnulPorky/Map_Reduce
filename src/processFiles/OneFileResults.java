package processFiles;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that has a name(e.g. - a file name) and
 * an hashmap(e.g. - expressions for keys and integer arrays for values, 
 * first integer the number of expression encounter, and the rest - lines containing expression) 
 * @author JetLi
 *
 */
public class OneFileResults {
	private String fileName;
	private HashMap<String, Integer[]> results;
	
	public OneFileResults(){
		this.fileName = "";
		this.results = new HashMap<String, Integer[]>();
	}
	
	public OneFileResults(String name, HashMap<String, Integer[]> map){
		this.fileName = name;
		results = map;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HashMap<String, Integer[]> getResults() {
		return results;
	}

	public void setResults(HashMap<String, Integer[]> results) {
		this.results = results;
	}
	
	public String toString(){
		String str = this.fileName;
		for(Map.Entry<String, Integer[]> entry : this.results.entrySet()){
			str += '\n' + entry.getKey() + " ";
			for(Integer i : entry.getValue()){
				str += i + " ";
			}
		}
		return str;
	}
}
