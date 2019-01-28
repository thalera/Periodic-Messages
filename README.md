# Periodic-Messages
Create periodic messages like "Hi -> Hydrogen Iodine" using any message you'd like!
The program can also do this in reverse, i.e. "Hydrogen Iodine -> Hi".
The program finds all solutions for all inputs.

Compile and Run PeriodicMessagesClient.java to use the program. From there you can choose
whether you would like to encode or decode a message. To do so, type "!encode" or "!decode"
respectively. There is also a third option, "!printAll" which will print all
the words and their solutions that the program has saved in SolvedDictionary.txt.
You can use the commands at any time during the program to choose what you'd like
to do. While encoding you can toggle the program printing out the messages as just
element symbols or full element names by typing "!symbols". Any word that you enter
that has a solution and that the program does not already know will be added to
SolvedDictionary.txt.

Included are two dictionary files, BigDictionary.txt and SmallDictionary.txt.
By default, SolvedDictionary.txt is the solutions from SmallDictionary.txt. You
can change this by deleting all entries in SolvedDictionary.txt (do not delete
the file itself) and uncommenting line 17 of PeriodicMessagesClient.java and
placing the name of the dictionary you would like to compile answers for
inside of the quotation marks. It is recommended that you recomment this
line after doing this.

For the program to run, Elements.txt, SolvedDictionary.txt, PeriodicMessageCompiler.java,
and PeriodicMessagesClient.java all need to be in the same directory.


