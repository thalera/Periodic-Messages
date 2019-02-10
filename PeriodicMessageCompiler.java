import java.util.*;
import java.io.*;

/** 
 * Aidan Thaler
 * <br>January 20, 2019
 *
 * <p>Generates all elegrams for given words or phrases. Can decode elegrams
 * back into words or phrases. Stores words with solutions in a file and
 * can read {@code Collections} or {@code Files} and compile elegrams
 * for words in them.
 */

public class PeriodicMessageCompiler {

   /**
    * Name of previously solved dictionary to load.
    */ 
   public static final String DICTIONARY_NAME = "SolvedDictionary.txt";
   
   /**
    * Name of file containing element symbols and names. Must be in format
    * "[Symbol] [Name]", such as "He Helium". Case-insensitive.
    */
   public static final String ELEMENTS_NAME = "Elements.txt";
   
   /**
    * Map from element symbols to element names.
    */
   private Map<String, String> elements;
   
   /**
    * Map from element names to element symbols.
    */
   private Map<String, String> reverseElements;
   
   /**
    * Map from words to their elegrams.
    */
   private Map<String, Set<List<String>>> wordSolutions;
   
   /**
    * Map from elegrams to words. Elegrams are stored as ArrayList.toString().
    */
   private Map<String, String> decodeSolutions;
   
   /**
    * All words with real elegrams.
    */
   private Set<String> dictionary;
   
   /**
    * Constructs a new PeriodicMessageCompiler and compiles all previously
    * solved words.
    *
    * @throws FileNotFoundException if {@link ELEMENTS_NAME} or {@link DICTIONARY_NAME}
    *         did not exist.
    */
   public PeriodicMessageCompiler() throws FileNotFoundException {
      // add all the element symbols to the map
      File elementFile = new File(ELEMENTS_NAME);
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
      File dictFile = new File(DICTIONARY_NAME);
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
   
   /**
    * Finds and prints all possible elegrams for the s. If symbols, then it
    * prints the elegrams using the element symbols, otherwise it prints them
    * using the element names. If there are no possible solutions, it will
    * print a message saying so. If s is an empty {@code String} or is a
    * {@code String} of length 1, it will print a message saying to input
    * a longer message.
    *
    * @param s the input to make an elegram for.
    * @param symbols {@code true} true if printing element symbols instead of
    *        element names.
    */
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
   
   /**
    * Prints all possible elegrams for the {@code String[]} sentence. If there
    * are no elegrams, it does nothing.
    *
    * @param sentence the sentence to convert to elegrams.
    * @param index the current word we're printing solutions for.
    * @param result where we are saving the elegram while we're building it.
    * @param symbols {@code true} if printing element symbols instead of
    *        element names.
    */
   private void printAllSolutions(String[] sentence, int index, List<List<String>> result,
                                  boolean symbols) {
      if (index < sentence.length) {
         // recursive backtracking loop to build all solutions
         for (List<String> solution : wordSolutions.get(sentence[index])) {
            result.add(solution);
            printAllSolutions(sentence, index + 1, result, symbols);
            result.remove(result.size() - 1);
         }
      } else if (index == sentence.length) {
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
   
   /**
    * Finds elegrams for all words in the {@code File} inputFile. Returns
    * the total number of words with an elegram as an {@code int}.
    *
    * @param inputFile file to find elegrams for.
    * @return total number of words with an elegram.
    * @throws FileNotFoundException if inputFile doesn't exist.
    */
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
   
   /**
    * Finds elegrams for all words in the {@code Collection<String>} data.
    * Returns the total number of words with an elegram as an {@code int}.
    *
    * @param data the words to find elegrams for.
    * @return total number of words with an elegram.
    */
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
   
   /**
    * Finds all elegrams for the {@code String} word and saves them to the
    * solutions {@code Map}s. If the word has an elegram and was not previously in
    * the {@link DICTIONARY_NAME}, then it will be added.
    *
    * @param word the word to find elegrams for.
    * @param index the index we are at.
    * @param size the size of the substring we are solving.
    * @param current a data structure to hold the elegram as we build it.
    */
   private void compileWord(String word, int index, int size, List<String> current) {
      // if there are still letters that need elements
      if (index < word.length()) {
         String key = "" + word.charAt(index);
         if (size == 2) {
            key += word.charAt(index + 1);
         }
         if (elements.containsKey(key)) {
            current.add(elements.get(key));
            compileWord(word, index + size, 1, current);
            current.remove(current.size() - 1);
         }
         // if the length was one, do it again but with length two
         if (size == 1 && index + 1 < word.length()) {
            compileWord(word, index, 2, current);
         }
      } else { // we have finished the word
         // save it to the map of words -> solutions
         addSolution(word, current);
      }
   }
   
   /**
    * Adds the {@code List<String>} solution and {@code String} word to the
    * {@link wordSolutions} and {@link decodeSolutions}.
    *
    * @param word the word corresponding to the solution.
    * @param solution the elegram corresponding to the word.
    */
   private void addSolution(String word, List<String> solution) {
      if (!wordSolutions.containsKey(word)) {
         wordSolutions.put(word, new HashSet<List<String>>());
      }
      List<String> solutionCopy = new ArrayList<String>(solution);
      wordSolutions.get(word).add(solutionCopy);
      decodeSolutions.put(solution.toString(), word);
      addToDictionary(word);
   }
   
   /**
    * Adds the {@code String} word to the {@link DICTIONARY_NAME} and {@link dictionary}.
    *
    * @param word the word to add to the dictionary.
    */
   private void addToDictionary(String word) {
      if (!dictionary.contains(word)) {
         dictionary.add(word);
         try {
         File dictFile = new File(DICTIONARY_NAME);
            PrintStream outputStrm = new PrintStream(new FileOutputStream(dictFile, true));
            outputStrm.println(word);
         } catch (FileNotFoundException e) {

         }
      }
   }
   
   /**
    * Prints all words and elegrams that have been solved.
    */
   public void printAll() {
      for (String word :  dictionary) {
         System.out.print(word + ": ");
         for (List<String> solution : wordSolutions.get(word)) {
            System.out.print(solution + " ");
         }
         System.out.println();
      }
   }
   
   /**
    * Takes the {@code String} s elegram of element names and prints it as
    * all possible English sentences. If it can't find a sentence with words
    * that align with the elements, it instead replaces the elements in the
    * elegram with their periodic symbols and prints that. If the s contains
    * a word that is not the name of an element, that word will remain
    * unchanged.
    *
    * @param s the elegram made of element names to decode.
    */
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
   
   /**
    * Decompiles the {@code List<String>} elements back into English and stores
    * all possible solutions in the {@code List<String>} solutions.
    *
    * @param elements the elegram to decode.
    * @param index where we are currently at in the elegram.
    * @param size how many elements we are considering in our current word.
    * @param current where we are building the elegrams.
    * @param solutions where we are storing the elegrams. 
    */
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