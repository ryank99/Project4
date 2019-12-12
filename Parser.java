import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
/**
 * 
 * @author Ryan Kirkpatrick
 * @version 12.12
 */
public class Parser {

    private File com;
    private MemoryManager memory;
    
    /**
     * 
     * @param c com file
     * @param m mem file
     * @param size of table
     * @throws IOException
     */
    public Parser(File c, String m, int size) throws IOException {
        com = c;
        memory = new MemoryManager(m, size);
    }
    
    /**
     * 
     * @return success
     * @throws Exception
     */
    public boolean parse() throws Exception {
        Record currRecord = null;
        Scanner scanner = new Scanner(com, "UTF-8");
        while (scanner.hasNextLine()) {
            String strang = scanner.nextLine().trim();
            String[] strangs = strang.split("\\s+");
            if (strang.equals("")) {
                continue;
            }
            String command = strangs[0];
            //start of switch
            if (command.equals("insert")) {
                String pid = strangs[1];
                String name = strangs[2] + " " + strangs[3];
                int errorCode = memory.insertName(pid, name);
                
                
                if (errorCode == 0) {
                    currRecord = null;
                    System.out.println(name + 
                        " insertion failed since the pid " 
                        + pid + " belongs to another student.");
                }
                else if (errorCode == 1) {
                    currRecord = null;
                    System.out.println(name + " insertion failed."
                        + " Attempt to insert in a full bucket.");
                }
                else {
                    currRecord = memory.searchHash(pid);
                    System.out.println(name + " inserted.");
                }
            }
            
            if (command.equals("remove")) {
                currRecord = null;
                String removed = memory.remove(strangs[1]);
                System.out.println(removed);
            }
            
            if (command.equals("update")) {
                String name = strangs[2] + " " + strangs[3];
                Record found = memory.update(strangs[1], name);
                if (found != null) {
                    currRecord = found;
                }
                else {
                    currRecord = null;
                    //System.out.println("update error");
                }
            }
            if (command.equals("print")) {
                currRecord = null;
                memory.print();
            }
            if (command.equals("search")) {
                currRecord = null;
                Record temp = memory.searchHash(strangs[1]);
                if (temp == null) {
                    System.out.println("Search Failed. Couldn't "
                        + "find any student with ID " + 
                            strangs[1]);
                }
                else {
                    String name = memory.readFromMem(temp.getNameHandle());
                    String essay = memory.readFromMem(temp.getEssayHandle());
                    System.out.println(strangs[1] + " " + name + ":");
                    if (essay.length() > 0) {
                        System.out.print(essay);
                    }
                }
            }
            if (command.equals("clear")) {
                currRecord = null;
                System.out.println(memory.clear(strangs[1]));
            }
            if (command.equals("essay") && strangs[1].equals("on")) {
                if (currRecord != null) {
                    System.out.println("essay saved for " +
                        memory.readFromMem(currRecord.getNameHandle()));
                    if (currRecord.hasEssayHandle()) {
                        memory.clear(currRecord.getPid());
                    }
                    
                    String temp = scanner.nextLine();
                    String essay = "";
                    while (!temp.equals("essay off")) {
                        essay += temp;
                        essay += "\n";
                        temp = scanner.nextLine();
                    }
                    memory.insertEssay(currRecord.getPid(), essay);
                    
                }
                else {
                    System.out.println("essay commands can only follow "
                        + "successful insert or update commands");
                }
            }
            if (command.equals("loadstudentdata")) {
                currRecord = null;
                System.out.println(strangs[1] + " successfully loaded.");
                String row;
                BufferedReader csvReader =
                    new BufferedReader(new FileReader(strangs[1]));
                while ((row = csvReader.readLine()) != null) {
                    row = row.replaceAll("\\s", "");
                    String[] data = row.split(",");
                    String pid = data[0];
                    String currName;
                    if (data[2].equals("")) {
                        currName = data[1] + " " + data[3];
                    }
                    else {
                        currName = data[1] + " " + data[3];
                    }
                    int errorCode = memory.insertName( pid, currName);
                    if (errorCode == 0) {
                        System.out.println("Warning: Student " + 
                            pid + " " + currName + 
                            " is not loaded since a student"
                            + " with the same pid exists.");
                    }
                    if (errorCode == 1) {
                        System.out.println("Warning: There is no free"
                            + " place in the bucket to load student " 
                            + pid + " " + currName + ".");
                    }
                    //adding to database
                }
                csvReader.close();
            }  
            //if(command != "update" && command != "insert") {
               // currRecord = null;
            //}
            //System.out.println("hello");
        }
        //System.out.println("--------------------------");
        //System.out.println(memory.getFileDump());
        scanner.close();
        return true;
    }
}
