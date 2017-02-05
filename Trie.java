import java.util.HashMap;

//node of the trie
class Node {
	HashMap<Character, Node> children = new HashMap<>();
    boolean endOfWord = false;
}

public class Trie {
    private Node root = new Node();

    //insert word to trie
    public void insert(String str) {
        Node current = root;
        for (char ch : str.toCharArray()) {
            Node node = current.children.get(ch);
            if (node == null) {
                node = new Node();
                current.children.put(ch, node);
            }
            current = node;
        }
        current.endOfWord = true; //mark current endOfWord = true
    }
    
    //search for a word in trie
    public boolean search(String str) {
        Node current = root;
        for (char ch : str.toCharArray()) {
            Node node = current.children.get(ch);
            if (node == null) {	//return false if node does not exist
                return false;
            }
            current = node;
        }
        return current.endOfWord;	//return if this is the end of the word
    }
}