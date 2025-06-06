/*
 * Rowen Stiles and Charles M., May 2025, Huffman Coding
 */
package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import heap.Heap;

public class Huffman {
    Node root;

    public Huffman(Node root) {
        this.root = root;
    }

    @SuppressWarnings("unchecked")
    public Huffman(String fileName) throws FileNotFoundException {
        HashMap<Character,Integer> values = parse(fileName);

        Heap<Node,Integer> heap = new Heap<Node,Integer>();
        values.forEach((v,f) -> heap.add(new Node(v, f), f));
        while (heap.size() > 1) {
            Node left = heap.poll(), right = heap.poll();
            Node huff = new Node('\0', left, right, left.freq + right.freq); // null character is used to denote an empty node
            heap.add(huff, huff.freq);
        }
        root = heap.poll();
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
}
