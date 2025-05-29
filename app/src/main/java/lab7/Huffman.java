/*
 * Rowen Stiles, May 2025, Huffman Coding
 */
package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;


public class Huffman {
    public static void main(String[] args) {
        //creates heap to start with
        HashMap<String, Integer> charMap = new HashMap<>();
        Heap<String,Integer> heap = new Heap<String,Integer>();
        int charaCount = 0;

        //Takes the file given in args, returns error if not found.
        String fileName = args[0];
        File file = new File(fileName);
        Scanner scanner;
        try{
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        while(scanner.hasNext()){
            String line = scanner.next();
            for(int i = 0; i < line.length(); i++){
                String character = line.substring(i,i+1);
                System.out.println(character);
                System.out.println(charMap.containsKey(character));
                if(!charMap.containsKey(character)){
                    charMap.put(character, 1);
                }
                else{
                    charMap.put(character,(charMap.get(character)));
                    System.out.println(charMap.get(character) + " and plus " + charMap.get(character) + 1);
                }
                charaCount++;
            }
        }
        System.out.println(charaCount);
        //charMap.dump();

        scanner.close();
    }
}
