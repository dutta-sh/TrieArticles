import java.util.Map;
import java.util.Set;

public class MainSearch extends MainCommon {
	private static boolean[] isMatched;
	
	public static void main(String[] args) {
		readCompanies();
		readArticle();
		
		isMatched = new boolean[article.length()];						//store flag for which characters are matched
		for(Map.Entry<String, Set<String>> entry : compMap.entrySet()) {
			int count = 0;
			for(String compName : entry.getValue()) {
				count = count + countName(compName);
			}
			if(count > 0)
				compCount.put(entry.getKey(), count);
		}
		countWords();
		sysOut();
	}
	
	//counts the number of times normalized company name occurs in the normalized article
	private static int countName(String company) {
		char[] art = article.toCharArray();
		char[] comp = company.toCharArray();
		int occurrence = 0;
		int i = 0;				//char position in the article
		while(i < art.length) {
			//pull that many chars from the article as size of comp name length, and preceded and succeeded by space
			//and not matched already
			if((!isMatched[i] && i == 0 && (i + comp.length < art.length) && art[i + comp.length] ==  ' ') // for first word
			|| (!isMatched[i] && i > 0 && (i + comp.length < art.length) && art[i-1] == ' ' && art[i + comp.length] ==  ' ') //for all other words
			|| (!isMatched[i] && i > 0 && (i + comp.length == art.length) && art[i-1] == ' ')) { //for last word
				char[] extract = new char[comp.length];
				for(int j = 0; j < comp.length; j++) {			//create extract of that many chars from article as comp name
					extract[j] = art[i + j];
				}
				if(company.equals(new String(extract))) {
					occurrence++;
					for(int j = i; j < i + comp.length; j++) {	//flag those positions as matched
						isMatched[j] = true;
					}
					i = i + comp.length;						//jump to the next word
				} else
					i++;										//jump to next char
			} else {
				i++;											//jump to next char
			}
		}
		return occurrence;
	}
	
	//counts total words from normalized article
	private static void countWords() {
		char[] art = article.toCharArray();
		StringBuilder artWithoutCompNames = new StringBuilder();
		for(int i = 0; i < art.length; i++) {					//only take those positions that are not matched
			if(!isMatched[i]) {
				artWithoutCompNames.append(art[i]);
			}
		}
		String[] artWithoutComp = artWithoutCompNames.toString().split(" ");	//take those words that are not matched
		for(String str : artWithoutComp) {	//get word count
			if(!str.trim().equals("") && !str.trim().equals("a") && !str.trim().equals("an") && !str.trim().equals("the")
					&& !str.trim().equals("and") && !str.trim().equals("or") && !str.trim().equals("but"))
				totalWords++;
		}
		for(int compCount : compCount.values()) {	//add to word count, the comp name counts, to get total count
			totalWords = totalWords + compCount;	//considers comp name as one word
		}
	}
}