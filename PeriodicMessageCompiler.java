// Aidan Thaler
// January 20th, 2019
// This outlines a Periodic Message Compiler, which can take in a word
// and figure out all the possible ways it can be written using
// periodic symbols. Can display solutions for phrases and words
// using periodic symbols or names. 

import java.util.*;
import java.io.*;

public class PeriodicMessageCompiler {
   // name of solved dictionary to load upon instantiation
   // to reset solved dictionary, delete all words inside of this file
   public static final String DICTIONARY = "SolvedDictionary.txt";
   // this is the map of symbols -> elements
   private Map<String, String> elements;
   // this is a similar map to elements, except it goes elements -> symbols
   private Map<String, String> reverseElements;
   // this is the map of words -> solutions
   // we store the solutions as a list so we can easily convert between symbols
   // and names of the elements depending on what the user wants
   private Map<String, Set<List<String>>> wordSolutions;
   // this is a map of solutions -> words
   private Map<String, String> decodeSolutions;
   // this is all words the compiler knows solutions to
   private Set<String> dictionary;
   
   // creates a new compiler and compiles all words in the dictionary file
   public PeriodicMessageCompiler() throws FileNotFoundException {
      // add all the element symbols to the map
      File elementFile = new File("Elements.txt");
      Scanner fileScanner = new Scanner(elementFile);
      elements = new HashMap<String, String>();
      reverseElements = new HashMap<String, String>();
      while (fileScanner.hasNext()) {
         String symbol = fileScanner.next().toLowerCase();
         String elementName = fileScanner.next().toLowerCase();
         elements.put(symbol, elementName);
         reverseElements.put(elementName, symbol);
      }
      // add all the old words to the set
      dictionary = new TreeSet<String>();
      File dictFile = new File(DICTIONARY);
      fileScanner = new Scanner(dictFile);
      while (fileScanner.hasNext()) {
         dictionary.add(fileScanner.next());
      }
      // instantiate the solutions maps
      wordSolutions = new TreeMap<String, Set<List<String>>>();
      decodeSolutions = new TreeMap<String, String>();
      // compile all the old words that were in the dictionary file
      System.out.println("Compiling previously solved words...");
      System.out.print(this.compile(dictFile));
      System.out.println(" words loaded from the dictionary.");
      System.out.println();
   }
   
   // Pre: message is longer than 1 (outputs message in response)
   // Takes in a message and prints all possible combinations of elements
   // for that message as their symbols if symbols is true, or full
   // element names if false. 
   // If no solutions are found, outputs a message saying so.
   public void printMessage(String s, boolean symbols) {
      if (s.length() <= 1) {
         System.out.println("Please enter a longer message.");
         return;
      }
      String[] words = s.toLowerCase().trim().replace("'", "").split("\\W+");
      boolean fail = false;
      for (String word : words) {
         // only compile the word if it doesn't already have a solution
         if (!wordSolutions.containsKey(word)) {
            compileWord(word, 0, 1, new ArrayList<String>());
            // if it still doesn't have a solution, say so
            if (!wordSolutions.containsKey(word)) {
               System.out.println("No solutions found for \"" + word + "\".");
               fail = true;
            }
         }
      }
      if (!fail) {
         printAllSolutions(words, 0, new ArrayList<List<String>>(), symbols);
      }
   }
   
   // prints out all possible sentences using the given array of words. index is where we
   // are starting at, and result is where the result is stored while building it.
   // symbols is if we are printing out the symbols instead of the full words
   private void printAllSolutions(String[] sentence, int index, List<List<String>> result,
                                  boolean symbols) {
      if (index < sentence.length) {
         // simple recursive backtracking loop to build all solutions
         for (List<String> solution : wordSolutions.get(sentence[index])) {
            result.add(solution);
            printAllSolutions(sentence, index + 1, result, symbols);
            result.remove(result.size() - 1);
         }
      } else {
         boolean first = true;
         String output = "";
         // for each word solution in our final sentence
         for (List<String> solution : result) {
            // for each element in our word solution
            for (String word : solution) {
               // if it's symbols, then use the symbol instead
               if (symbols) {
                  word = reverseElements.get(word);
               }
               // this is to capatalize the first word/each symbol
               if (first || symbols) {
                  word = word.substring(0 , 1).toUpperCase() + word.substring(1);
                  first = false;
               }
               output += " " + word;
            }
         }
         System.out.println(output.trim() + ".");
      }
   }
   
   // compiles a given text file
   // returns number of successful solutions
   public int compile(File inputFile) throws FileNotFoundException {
      Scanner fileScanner = new Scanner(inputFile);
      int count = 0;
      while (fileScanner.hasNext()) {
         String currentWord = fileScanner.next().toLowerCase();
         if (currentWord.length() != 1 || currentWord.equals("a") ||
             currentWord.equals("i")) {
            compileWord(currentWord, 0, 1, new ArrayList<String>());
            if (dictionary.contains(currentWord)) {
               count++;
            }
         }
      }
      return count;
   }
   
   // compiles a given collection of words
   // returns number of successful solutions
   public int compile(Collection<String> data) {
      int count = 0;
      for (String word : data) {
         compileWord(word, 0, 1, new ArrayList<String>());
         if (dictionary.contains(word)) {
            count++;
         }
      }
      return count;
   }
   
   // compiles a single given word into the periodic form and saves it to the given map
   // with the key being the given word. index is the index we are currently at, size
   // is the length of the current symbol, and current is our currently
   // built list of elements
   private void compileWord(String word, int index, int size, List<String> current) {
      // if there are still letters that need elements
      if (index < word.length()) {
         // create the key
         String key = "" + word.charAt(index);
         if (size == 2) {
            key += word.charAt(index + 1);
         }
         // if the key is valid, great! go deeper
         if (elements.containsKey(key)) {
            // add it to the list
            current.add(elements.get(key));
            // continue on at the right index and repeat until no letters left
            compileWord(word, index + size, 1, current);
            // remove this one
            current.remove(current.size() - 1);
         }
         // if the length was one, we need to try it again but with two this time
         if (size == 1 && index + 1 < word.length()) {
            compileWord(word, index, 2, current);
         }
      } else { // we have finished the word
         // save it to the map of words -> solutions
         addSolution(word, current);
      }
   }
   
   // adds the given list to corresponding set of lists in the word solutions map
   // and adds the word to the solved dictionary
   private void addSolution(String word, List<String> solution) {
      if (!wordSolutions.containsKey(word)) {
         wordSolutions.put(word, new HashSet<List<String>>());
      }
      List<String> solutionCopy = new ArrayList<String>();
      solutionCopy.addAll(solution);
      wordSolutions.get(word).add(solutionCopy);
      decodeSolutions.put(solution.toString(), word);
      addToDictionary(word);
   }
   
   // adds the given word to the solved dictionary
   private void addToDictionary(String word) {
      if (!dictionary.contains(word)) {
         dictionary.add(word);
         try {
            File dictFile = new File(DICTIONARY);
            PrintStream outputStrm = new PrintStream(new FileOutputStream(dictFile, true));
            outputStrm.println(word);
         } catch (Exception e) {
         
         }
      }
   }
   
   // prints out all words this compiler has solved and their solutions
   public void printAll() {
      for (String word :  dictionary) {
         System.out.print(word + ": ");
         for (List<String> solution : wordSolutions.get(word)) {
            System.out.print(solution + " ");
         }
         System.out.println();
      }
   }
   
   // takes a string of element names and returns it as the original word(s) if
   // it knows it. Otherwise it will return it as periodic symbols. If a 
   // non-elemental name is passed in, it will remain unchanged.
   public void decodeMessage(String s) {
      // make an array of each word put that into a list
      String[] words = s.toLowerCase().split("\\W+");
      List<String> wordsList = new ArrayList<String>();
      for (String word : words) {
         wordsList.add(word);
      }
      List<String> results = new ArrayList<String>();
      decompile(wordsList, 0, 1, new ArrayList<String>(), results);
      if (results.size() == 0) { // we didn't find a solution in the dictionary
         results.add(0, "");
         for (String word : words) {
            // if the word is an element name
            if (reverseElements.containsKey(word)) {
               String symbol = reverseElements.get(word);
               symbol = symbol.substring(0, 1).toUpperCase() + symbol.substring(1);
               results.add(0, results.remove(0) + symbol + " ");
            } else { // if it's not an elemental name, just leave it alone
               results.add(0, results.remove(0) + word + " ");
            }
         }
         System.out.println(results.get(0).trim() + ".");
      } else { // we did find a solution, yay!
         for (String result : results) {
            System.out.println(result);
         }
      }
   }
   
   // decompiles the given list of elements and outputs all solution to the
   // given solutions list. index is where we are currently at (start at 0),
   // size is the length of the current "substring" (start at 1) and
   // output if our current output sentence as a list (pass a new list)
   private void decompile(List<String> elements, int index, int size, List<String> current,
                          List<String> solutions) {
      // if there are still elements that need words
      if (index < elements.size()) {
         // create the key
         List<String> key = new ArrayList<String>();
         for (int i = index; i < index + size; i++) {
            key.add(elements.get(i));
         }
         // if this key has a match, great! go deeper
         if (decodeSolutions.containsKey(key.toString())) {
            current.add(decodeSolutions.get(key.toString()));
            decompile(elements, index + size, 1, current, solutions);
            current.remove(current.size() - 1);
         }
         // ok that key is doneso now, so lets try a slightly bigger one
         if (index + size + 1 <= elements.size()) {
            decompile(elements, index, size + 1, current, solutions);
         }
      } else { // we've made a solution
         String firstWord = current.get(0);
         String result = firstWord.substring(0, 1).toUpperCase() + firstWord.substring(1);
         for (int i = 1; i < current.size(); i++) {
            result += " " + current.get(i);
         }
         result += ".";
         solutions.add(result);
      }
   }
   
   
}