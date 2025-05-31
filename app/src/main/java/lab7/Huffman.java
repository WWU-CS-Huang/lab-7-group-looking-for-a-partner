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
    Heap<Character,Integer> heap;
    StringBuilder inputString = new StringBuilder("");

    public Huffman() {
        charMap = new HashMap<Character, Integer>();
        heap = new Heap<Character,Integer>();
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

        while (scanner.hasNext()) {
            String line = scanner.next();
            inputString.append(line);
            for (char c : line.toCharArray())
                charMap.merge(c, 1, (a, b) -> a + b); // if new addition, value is one; otherwise, value is previous plus one
        } scanner.close();

        // add to heap for sorting, then can use poll() to get lowest values for building huffman.
        charMap.forEach((key, value) -> heap.add(key, value));
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
    }
}
