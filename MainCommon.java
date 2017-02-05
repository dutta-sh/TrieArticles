import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

//contains attributes and methods used by both
//MainTrie and MainSearch
abstract public class MainCommon {

	protected static List<Character> chars = new ArrayList<>();				//stores non special chars to scan
	protected static String article;										//stores normailized article from console
	protected static int totalWords = 0;									//stores total word count in article
	protected static Map<String, Set<String>> compMap = new HashMap<>();	//unnormalized comp name and synonym set
	protected static Map<String, Integer> compCount = new HashMap<>();		//unnormalized comp name and count of occurrence
	
	static {
		char[] eligible = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ".toCharArray();
		for(char e : eligible) {											//ignoring special characters
			chars.add(e);
		}
	}
		
	protected static void readCompanies() {
		Scanner sc = readFile("companies.dat");
		while(sc.hasNext()) {									//take a line
			String compNames[] = sc.nextLine().split("\t");		//split line based on tabs
			String key = compNames[0];							//unnormalized first name is key
			Set<String> value = new TreeSet<>(Collections.reverseOrder());	//store normalized names here
			for(String compName : compNames) {					//for each name on line
				char[] compNameArr = compName.toCharArray();	//get chars in name
				StringBuilder compNameNormalized = new StringBuilder();
				for(char c : compNameArr) {						//check if char is eligible
					if(chars.contains(c)) {
						compNameNormalized.append(c);			//add char to normalized name
					}
				}
				value.add(compNameNormalized.toString());		//add normalized name to list
			}
			compMap.put(key, value);
		}
		sc.close();
		//System.out.println(compMap);
	}
	
	protected static void readArticle() {
		System.out.println("Provide the article:");
		Scanner sc = new Scanner(System.in);					//reads from console
		//Scanner sc = readFile("input.dat");					//reads from file
		StringBuilder strBld = new StringBuilder();
		String line;
		while(!(line = sc.nextLine()).equals(".")) {			//take a line
			char[] lineChars = line.toCharArray();				//get chars in line
			for(char c : lineChars) {							//check if char is eligible
				if(chars.contains(c)) {
					strBld.append(c);							//add char to normalized article
				}
			}
			strBld.append(" ");									//adds an extra space at end of line
		}
		sc.close();
		article = strBld.toString();
		//System.out.println(article);
	}
	
	private static Scanner readFile(String fileName){
		Scanner sc = null;
		try {
			sc = new Scanner(new File(fileName));			
		} catch (FileNotFoundException e) {			
			System.out.println("File Not Found");
			e.printStackTrace();
			System.exit(1);
		}
		return sc;
	}
	
	
	protected static void sysOut() {							//output to console
		//System.out.println(compCount);
		int totalHit = 0;
		double totalRel = 0;
		System.out.println("--------------------------------------");
		System.out.printf("%-15s %-10s %-1s\n", "Company", "Hit Count", "Relevance");
		System.out.println("--------------------------------------");
		for(Map.Entry<String, Integer> entry : compCount.entrySet()) {
			double relevance = ((double)entry.getValue()/totalWords)*100;
			System.out.printf("%-15s %5d %14.5f%%\n", entry.getKey(), entry.getValue(), relevance);
			totalHit = totalHit + entry.getValue();
			totalRel = totalRel + relevance;
		}
		System.out.println("--------------------------------------");
		System.out.printf("%-15s %5d %14.5f%%\n", "Total", totalHit, totalRel);
		System.out.println("--------------------------------------");
		System.out.printf("%-15s %10d", "Total Words", totalWords);
	}
}