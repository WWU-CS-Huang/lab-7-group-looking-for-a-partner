/*
 * Rowen Stiles and Charles M., May 2025, Huffman Coding
 */

package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import heap.Heap;
import java.lang.StringBuilder;
//import avl.AVL;

public class Huffman {
    HashMap<Character, Integer> charMap;
    Heap<Node,Integer> heap;
    StringBuilder inputString = new StringBuilder("");
    public String traversal;

    class Node {
        Character key;
        Integer value;
        Node left;
        Node right;

        /** For the initial values, PRE combining. */
        public Node(Character k, Integer v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }
        /** For nodes that come during combining with null keys. */
        public Node(Integer v, Node l, Node r) {
            key = null;
            value = v;
            left = l;
            right = r;
        }
    }

    public Huffman() {
        charMap = new HashMap<Character, Integer>();
        heap = new Heap<Node,Integer>();
    }

    public Huffman(String fileName) {
        this();
        File file = new File(fileName);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.printf("The file specified was not found at %s%s%s\n", System.getProperty("user.dir"), File.separator, fileName);
            return;
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            inputString.append(line);
            for (char c : line.toCharArray())
                charMap.merge(c, 1, (a, b) -> a + b); // if new addition, value is one; otherwise, value is previous plus one
        } scanner.close();

        // turns the keyvalue pairs into nodes, then adds the node and value to the heap
        charMap.forEach(
            (key, value) -> {
            Node n = new Node(key,value);
            heap.add(n, value);
        });

        // Base case is that two are removed and only one is added back, so eventually the heap will only have the head node in it
        while(heap.size() > 1){
            Node firstPolledNode = heap.poll();
            Node secondPolledNode = heap.poll();

            Node connectingNode = new Node(firstPolledNode.value + secondPolledNode.value, firstPolledNode, secondPolledNode);
            heap.add(connectingNode,connectingNode.value);
        }
    }

    /** appends the values in the tree to String traversal using an in-order traversal.
     * Borrowed from lab 3, edited slightly to work with this and be more readable.
     * Just to prove that the tree is real and hopefully correct in printing. */
    public void inOrder() {
        traversal = "";
        inOrder(heap.peek());
    }
    private void inOrder(Node n) {
        if(n == null){
            return;
        }
        inOrder(n.left);
        traversal += n.key + ":" + n.value + ", ";
        inOrder(n.right);
    }

    public int getCharacterCount() {
        return charMap.size();
    }

    public String toString() {
        return charMap.toString();
    }

    

    public static void main(String[] args) {
        Huffman huff = new Huffman(args[0]);

        if(huff.inputString.length() < 100){
            System.out.println("Input string: " + huff.inputString +
            "\nEncoded string: " + "huff.encode()" +
            "\nDecoded string: " + "huff.decode()");
        }
        System.out.println("\nDecoded equals input: " + "huff.equalCoding()" +
        "\nCompression ratio: " + "length(encoded bitstring) / huff.charaCount / 8.0");

        /**Rowen Note: I'm personally unable to continue working due to many other assignments,
         * so here's proof that unique characters are counted correctly and in-order traversal
         * of what I had of the tree, at least! */
        huff.inOrder();
        System.out.println("\nTo prove unique characters: " + huff.charMap.toString());
        System.out.println("To prove huffman tree is correct, in-order traversal: " + huff.traversal);
    }
}
