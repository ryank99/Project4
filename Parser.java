import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Parser {

    private File com;
    private MemoryManager memory;
    
    public Parser(File c, String m, int size) throws IOException {
        com = c;
        memory = new MemoryManager(m, size);
    }
    
    public boolean parse() throws Exception {
        boolean allowEssay = false;
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
                Record hashed = memory.insertName(
                        pid, name);
                
                if (hashed == null) {
                    currRecord = null;
                    System.out.println(name + " insertion failed since the pid " 
                        + pid + " belongs to another student.");
                }
                else {
                    currRecord = hashed;
                    System.out.println(name + " inserted.");
                }
            }
            if (command.equals("remove")) {
                String removed = memory.remove(strangs[1]);
                System.out.println(removed);
            }
            if (command.equals("update")) {
                String name = strangs[2] + " " + strangs[3];
                Record found = memory.update(strangs[1], name);
                if(found != null) {
                    currRecord = found;
                }
                else {
                    System.out.println("update error");
                }
            }
            if (command.equals("print")) {
                memory.print();
            }
            if (command.equals("search")) {
                Record temp = memory.searchHash(strangs[1]);
                if (temp == null) {
                    System.out.println("Search Failed. Couldn't find any student with ID " + 
                            strangs[1]);
                }
                else {
                    String name = memory.readFromMem(temp.getNameHandle());
                    String essay = memory.readFromMem(temp.getEssayHandle());
                    System.out.println(strangs[1] + " " + name + ":");
                    if (essay.length() > 0) {
                        System.out.println(essay);
                    }
                }
            }
            if (command.equals("clear")) {
                System.out.println(memory.clear(strangs[1]));
            }
            if (command.equals("essay") && strangs[1].equals("on")) {
                if(currRecord != null) {
                    System.out.println("essay saved for " + memory.readFromMem(currRecord.getNameHandle()));
                    if (currRecord.hasEssayHandle()) {
                        memory.clear(currRecord.getPid());
                    }
                    String temp = scanner.nextLine();
                    String essay = "";
                    while(!temp.equals("essay off")) {
                        essay += temp;
                        temp = scanner.nextLine();
                    }
                    memory.insertEssay(currRecord.getPid(), essay);
                }
                else {
                    System.out.println("essay commands can only follow "
                        + "successful insert or update commands");
                }
            }
            if (command.equals("loadstudentdata")){
                currRecord = null;
                System.out.println(strangs[1] + " successfully loaded");
                String row;
                BufferedReader csvReader =
                    new BufferedReader(new FileReader(strangs[1]));
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",");
                    String pid = data[0];
                    String currName;
                    if(data[2].equals("")) {
                        currName = data[1] + " " + data[3];
                    }
                    else {
                        currName = data[1] + " " + data[3];
                    }
                    Record saved = memory.insertName( pid, currName);
                    if(saved == null) {
                        System.out.println("Warning: Student " + 
                            pid + " " + currName + 
                            " is not loaded since a student with the same pid exists.");
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
