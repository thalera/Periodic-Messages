import java.util.*;
import java.io.*;

/** 
 * Aidan Thaler
 * <br>January 20, 2019
 *
 * <p>Takes in user-inputted words and phrases and compiles them into
 * the same message written using element names/symbols (AKA an elegram).
 */

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
   
   /**
    * Allows the user to input messages and encode them into elegrams.
    *
    * @param input Scanner to interact with the user.
    * @param compiler compiler to use.
    */
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
   
    /**
    * Allows the user to input elegrams and decode them into messages.
    *
    * @param input user's input.
    * @param compiler compiler to use.
    */
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
   
   /**
    * Prompts the user for a message and returns their input.
    *
    * @param input user's input.
    * @return the user's message.
    */
   public static String getMessage(Scanner input) {
      System.out.print("Please enter your message: ");
      return input.nextLine().trim();
   }
}