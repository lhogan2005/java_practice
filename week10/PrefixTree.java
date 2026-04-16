import java.util.ArrayList;
import java.util.List;

class TrieNode {
    char value;
    List <Character> child;
    boolean isEnd;

    public TrieNode(char value, TrieNode child) {
        this.value = value;
        this.isEnd = false;
        this.child = new ArrayList<>();
    }

    public void markAsLeaf() {
        this.isEnd = true;
    }
}

class PrefixTree {
    List<Character> root;
    char[] letters;

    public PrefixTree(String word) {
        this.letters = word.toCharArray();
        this.root = new ArrayList<>();
    }

    public void insert(String word) {
        // Inserts word into tree
        // Takes in a string that is word.
        // Makes it a character array.
        // Checks for our characters one by one seeing if there in our tree currently.
        // If they are continue with that branch.
        // If they aren't create a new branch.
    }

    public boolean search(String word) {
        // Checks for word in tree
        // Takes word, parses it.
        // Looks letter by letter seeing if its in the tree
        // As soon as the letter is not present return false
        boolean found = false;
        for (letter : word) {
            if (letter)
        }
    }

    public boolean startsWith(String prefix) {
        // Checks if any word in the tree starts with the given prefix
        // Same as search looking only until we find all of our prefix letter or are missing one.
    }

    public void traverse() {
        // Recursivly prints the tree structure
        // Use DFS
    }
}

// Operation should split word into its each individual letters