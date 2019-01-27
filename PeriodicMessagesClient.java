// Aidan Thaler
// January 20th, 2019
// This program allows the user to input a message and it will return
// all possible ways to write the message using only periodic table
// symbols. Can print out only symbols or full element names.
// If you ever want to reset the solved dictionary (the collection of words
// the program will use to decompile a message), delete all entries
// in SolvedDictionary.txt, and uncomment line 17 with the name
// of the file to compile a new dictionary from in the quotes.

import java.util.*;
import java.io.*;

public class PeriodicMessagesClient {
   public static void main(String[] args) throws FileNotFoundException {
      PeriodicMessageCompiler compiler = new PeriodicMessageCompiler();
      //compiler.compile(new File(""));
      Scanner input = new Scanner(System.in);
      System.out.println("Type !encode to start encoding or !decode if you want to decode.");
      System.out.println();
      String message = getMessage(input);
      while (!message.equals("") && !message.equals("!encode") && !message.equals("!decode")
             && !message.equals("!printAll")) {
         System.out.println();
         System.out.println("I don't understand.");
         System.out.println();
         System.out.println("Type !encode to start encoding or !decode if you want to decode.");
         System.out.println();
         message = getMessage(input);
      }
      if (message.equals("!encode")) {
         System.out.println();
         encode(input, compiler);
      } else if (message.equals("!decode")) {
         System.out.println();
         decode(input, compiler);
      } else if (message.equals("!printAll")) {
         System.out.println();
         compiler.printAll();
         System.out.println();
         encode(input, compiler);
      }
   } 
   
   // allows the user to encode messages
   public static void encode(Scanner input, PeriodicMessageCompiler compiler) {
      System.out.println("Type !symbols if you wish to see periodic");
      System.out.println("symbols instead of full element names,");
      System.out.println("or type !decode to decode a message instead.");
      System.out.println();
      String message = getMessage(input);
      boolean symbols = false;
      while (!message.equals("") && !message.equals("!decode") && !message.equals("!printAll")) {
         if (message.equals("!symbols")) {
            symbols = !symbols;
            System.out.println();
            System.out.print("Symbols ");
            if (symbols) {
               System.out.println("activated.");
            } else {
               System.out.println("deactivated.");
            }
            System.out.println();
         } else {
            System.out.println();
            compiler.printMessage(message, symbols);
            System.out.println();
         }
         message = getMessage(input);
      }
      if (message.equals("!decode")) {
         System.out.println();
         decode(input, compiler);
      } else if (message.equals("!printAll")) {
         System.out.println();
         compiler.printAll();
         System.out.println();
         encode(input, compiler);
      }
   }
   
   // allows the user to decode messages
   public static void decode(Scanner input, PeriodicMessageCompiler compiler) {
      System.out.println("Type !encode to encode a message instead.");
      System.out.println();
      String message = getMessage(input);
      boolean symbols = false;
      while (!message.equals("") && !message.equals("!encode") && !message.equals("!printAll")) {
         System.out.println();
         compiler.decodeMessage(message);
         System.out.println();
         message = getMessage(input);
      }
      if (message.equals("!encode")) {
         System.out.println();
         encode(input, compiler);
      } else if (message.equals("!printAll")) {
         System.out.println();
         compiler.printAll();
         System.out.println();
         decode(input, compiler);
      }

   }
   
   // gets a message from the user and returns it
   public static String getMessage(Scanner input) {
      System.out.print("Please enter your message: ");
      return input.nextLine().trim();
   }
}