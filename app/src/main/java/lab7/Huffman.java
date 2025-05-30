/*
 * Rowen Stiles and Charles M., May 2025, Huffman Coding
 */
package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;


public class Huffman {
    HashMap<Character, Integer> charMap;
    Heap<Character,Integer> heap;

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
            for (char c : line.toCharArray())
                charMap.merge(c, 1, (a, b) -> a + b); // if new addition, value is one; otherwise, value is previous plus one
        } scanner.close();
    }

    public int getCharacterCount() {
        return charMap.size();
    }

    public String toString() {
        return charMap.toString();
    }

    public static void main(String[] args) {
        Huffman huff = new Huffman(args[0]);
    }
}
