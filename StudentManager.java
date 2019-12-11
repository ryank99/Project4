import java.io.File;
import java.io.IOException;

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
