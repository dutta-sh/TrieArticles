import java.util.Map;
import java.util.Set;

public class MainTrie extends MainCommon {
	private static Trie compTrie = new Trie();
	
	public static void main(String[] args) {
		readCompanies();
		readArticle();
		
		for(Set<String> values : compMap.values())
			for(String compNameNormalized : values)
				compTrie.insert(compNameNormalized);	//add normalized name to trie
		
		String[] artWords = article.split(" ");
		int i = 0;
		//match one word with trie, if no match then move to next word
		//if match is found, try matching two words and continue until no more is matched
		while(i < artWords.length) {
			String matched = null;
			StringBuilder strBld = new StringBuilder();
			strBld.append(artWords[i]);
			while(compTrie.search(strBld.toString()) && i < artWords.length) {
				matched = strBld.toString();
				i++;
				strBld.append(" " + artWords[i]);
			}
			if(matched == null)
				i++;
			else {
				//System.out.println(matched);
				
				//matching word found, now pull up the unnormalized comp name, and increment its count
				for(Map.Entry<String, Set<String>> entry : compMap.entrySet()) {
					if(entry.getValue().contains(matched)) {
						int count = 1;
						if(compCount.get(entry.getKey()) != null)
							count = compCount.get(entry.getKey()) + 1;
						
						compCount.put(entry.getKey(), count);
					}
				}
			}
			
			//if the word is not one of the in-eligible words, then add to count
			//considers comp name as one word
			if(!strBld.toString().equals("") && !strBld.toString().equals("a") && !strBld.toString().equals("an") && !strBld.toString().equals("the")
					&& !strBld.toString().equals("and") && !strBld.toString().equals("or") && !strBld.toString().equals("but")) {
				totalWords++;
			}
		}
		sysOut();
	}
}