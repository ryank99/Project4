import java.io.File;
//On my honor:
//
//- I have not used source code obtained from another student,
//or any other unauthorized source, either modified or
//unmodified.
//
//- All source code and documentation used in my program is
//either my original work, or was derived by me from the
//source code published in the textbook for this course.
//
//- I have not discussed coding details about this project with
//anyone other than my partner (in the case of a joint
//submission), instructor, ACM/UPE tutors or the TAs assigned
//to this course. I understand that I may discuss the concepts
//of this program with other students, and that another student
//may help me debug my program so long as neither of us writes
//anything during the discussion or modifies any computer file
//during the discussion. I have violated neither the spirit nor
//letter of this restriction
/**
 * 
 * @author Ryan Kirkpatrick
 * @version 12/12
 *
 */
public class StudentManager {

    private static int hashSize;
    private static File command;
    private static String memory;
    private static Parser parse;

    /**
     * 
     * @param args is the input
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 4) {
            command = new File(args[0]);
            hashSize = Integer.parseInt(args[2]);
            memory = args[3];
            System.out.println("Created Hash Set with " + args[2] + " slots.");
            parse = new Parser(command, memory, hashSize);
            parse.parse();
        }
        else {
            System.out.println("Invalid program arguments");
        }
    }
    
    /**
     * 
     * @return the parser
     */
    public Parser getParser() {
        return parse;
    }
}
