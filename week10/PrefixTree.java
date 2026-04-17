import java.util.HashMap;
import java.util.Map;


class TrieNode {
    char value;
    Map<Character, TrieNode> children;
    boolean isEndOfWord;

    public TrieNode(char value) {
        this.value = value;
        this.children = new HashMap<>();
        this.isEndOfWord = false;
    }

    public void markAsLeaf() {
        this.isEndOfWord = true;
    }
}

public class PrefixTree {
    private TrieNode root;

    public PrefixTree() {
        root = new TrieNode('\0');
    }

    public void insert(String word) {
        TrieNode current = root;

        for (char ch : word.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode(ch));
            current = current.children.get(ch);
        }

        current.markAsLeaf();
    }

    public boolean search(String word) {
        TrieNode node = findNode(word);
        return node != null && node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        return findNode(prefix) != null;
    }

    private TrieNode findNode(String str) {
        TrieNode current = root;

        for (char ch : str.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return null;
            }
            current = current.children.get(ch);
        }

        return current;
    }

    public void traverse() {
        traverseHelper(root, "");
    }

    private void traverseHelper(TrieNode node, String indent) {
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            TrieNode child = entry.getValue();

            System.out.print(indent + "  └── " + child.value);

            if (child.isEndOfWord) {
                System.out.print(" (end)");
            }

            System.out.println();

            traverseHelper(child, indent + "  ");
        }
    }
}