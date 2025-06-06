/*
 * Rowen Stiles and Charles M., May 2025, Huffman Coding
 */
package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.BitSet;
import heap.Heap;

public class Huffman {
    Node root; HashMap<Character,Code> codeMap;

    /* Constructor: initializes codeMap. */
    public Huffman() {
        codeMap = new HashMap<Character,Code>();
    }

    @SuppressWarnings("unchecked")
    /* Builds the Huffman tree from a mapping of characters to frequencies.
     * The char-freq map is used to build a min-heap of characters.
     * The Huffman tree is built from this min-heap.
     * Finally, codes are built from traversing the Huffman tree
     * and are then mapped to the relevant character.
     * precondition: file must exist */
    public Huffman(String fileName) throws FileNotFoundException {
        this(); HashMap<Character,Integer> values = parse(fileName);

        Heap<Node,Integer> heap = new Heap<Node,Integer>();
        values.forEach((v,f) -> heap.add(new Node(v, f), f));
        while (heap.size() > 1) {
            Node left = heap.poll(), right = heap.poll();
            Node huff = new Node('\0', left, right, left.freq + right.freq); // null character is used to denote an empty node
            heap.add(huff, huff.freq);
        } root = heap.poll();
        values.forEach((v,f) -> codeMap(root, v, new BitSet(), 0));
    }

    @SuppressWarnings("unchecked")
    /* Parses a text file into a hash map, mapping characters to frequencies.
     * precondition: file must exist */
    private HashMap parse(String fileName) throws FileNotFoundException {
        Scanner io = new Scanner(new File(fileName));
        HashMap<Character,Integer> map = new HashMap<Character,Integer>();
        while (io.hasNext()) {
            String line = io.next();
            for (char c : line.toCharArray())
                map.merge(c, 1, (a, b) -> a + b); // if new addition, value is one; otherwise, value is previous plus one
        } io.close();
        return map;
    }

    /* Maps a character to a code by traversing the tree until
     * a value matching the specified character is found.
     * A zero is placed when going left, and a one is placed when going right */
    private void codeMap(Node node, char c, BitSet encoding, int length) {
        if (node == null || codeMap.get(c) != null) return;
        else if (node.isLeaf() && c == node.value) {
            codeMap.put(c, new Code(encoding, length)); return;
        } BitSet left = (BitSet) encoding.clone(), right = (BitSet) encoding.clone();
        left.set(length, false); codeMap(node.left, c, left, length+1);
        right.set(length, true); codeMap(node.right, c, right, length+1);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Huffman huff = new Huffman(args[0]);
    }

    /* Node class used to represent Huffman trees.
     * attributes: character value and integer frequency;
     * additionally, left and right nodes */
    public class Node {
        char value; int freq;
        Node left, right;

        /* Constructor: sets value and frequency. */
        public Node(char value, int freq) {
            this.value = value;
            this.freq = freq;
        }
        /* Constructor: sets left and right nodes. */
        public Node(char value, Node left, Node right, int freq) {
            this(value, freq);
            this.left = left; this.right = right;
        }

        /* Check if current node is a leaf. */
        public boolean isLeaf() {
            return (left == null && right == null);
        }
    }

    /* The BitSet class only tracks length up to the last bit set to one,
     * thus another class was required to keep track of zeroes.
     * attributes: BitSet value (ones), and integer length (total) */
    public class Code {
        BitSet data; int length;

        /* Constructor: sets value and length. */
        Code(BitSet data, int length) {
            this.data = data;
            this.length = length;
        }

        public boolean get(int idx) {
            return data.get(idx);
        }

        /* Makes a string comprised of (length) zeroes and replaces any set index with a one. */
        public String toString() {
            if (data.cardinality() == length) return "1".repeat(length); // if number of set bits equals length, simply return repeating ones
            StringBuilder output = new StringBuilder(length); output.repeat('0', length);
            for (int i = data.nextSetBit(0); i >= 0; i = data.nextSetBit(i+1))
                output.replace(i, i+1, "1");
            return output.toString();
        }
    }
}
