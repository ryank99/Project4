import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author ryank99
 * @version 12.09
 */
public class MemoryManager {
    private RandomAccessFile memoryFile;
    private Node head;
    private HashFunction hashTable;


   /**
    * 
    * @param fileName is the file name
    * @param hashSize size of hash
    * @throws IOException can throw
    */
    public MemoryManager(String fileName, int hashSize) throws IOException {
        this.memoryFile = new RandomAccessFile(fileName, "rw");
        memoryFile.setLength(0);
        this.head = null;
        this.hashTable = new HashFunction(hashSize);
    }

    /**
     * 
     * @return mem file as string
     * @throws IOException
     */
    public String getFileDump() throws IOException {
        return readFromMem(new Handle(0, (int)memoryFile.length()));
    }
    
    /**
     * A constructor to test certain functions
     * 
     * @param hashSize is the hashsize
     */
    public MemoryManager(int hashSize) {
        head = null;
        hashTable = new HashFunction(hashSize);
    }


    /**
     * 
     * @param id is the id
     * @param name is the name
     * @return  error code 0, already inserted. error code 1 overflow
     * @throws IOException
     */
    public int insertName(String id, String name)
        throws IOException {
        Record hashObject = searchHash(id);
        // returns null if seqeunce already exists. Or overflow.
        if (hashObject != null && hashObject.getPid().equals(id)) {
            return 0;
        }
        if (hashTable.isFull(id)) {
            return 1;
        }
        
        byte[] data = name.getBytes();
        
        //add to end
        Handle nameHandle;
        if (head == null) {
            nameHandle = new Handle(memoryFile.length(), data.length);
            memoryFile.seek(memoryFile.length());
            memoryFile.write(data);
        }
        else {
            Node freeID = findFree(data.length);
            if (freeID != null) {
                nameHandle = new Handle(freeID.getOffset(), data.length);
                memoryFile.seek(freeID.getOffset());
                memoryFile.write(data);
                if (freeID.getLength() == data.length) {
                    //perfect fit, remove node
                    removeNode(freeID);
                }
                else {
                  //update node bounds
                    freeID.setLength(freeID.getLength() - data.length);
                    freeID.setOffset(freeID.getOffset() + data.length); 
                }
            }
            else {
                nameHandle = new Handle(memoryFile.length(), data.length);
                memoryFile.seek(memoryFile.length());
                memoryFile.write(data);
            }
        }
        //save to handle object
        //write to file
        //update free nodes
        Record hashObjectNew = new Record(id, nameHandle);
        hashTable.insert(id, hashObjectNew); // insert
        return -1;
    }
    
    /**
     * 
     * @param id to insert to
     * @param essay to insert
     * @return new record inserted to
     * @throws Exception
     */
    public Record insertEssay(String id, String essay) throws Exception {
        Record found = searchHash(id);
        if (found == null) {
            System.out.println("error");
        }
        byte[] data = essay.getBytes();
        data = essay.getBytes();
        Handle essayHandle;
        if (head == null) {
            essayHandle = new Handle(memoryFile.length(), data.length);
            memoryFile.seek(memoryFile.length());
            memoryFile.write(data);
        }
        else {
            Node freeID = findFree(data.length);
            if (freeID != null) {
                essayHandle = new Handle(freeID.getOffset(), data.length);
                memoryFile.seek(freeID.getOffset());
                memoryFile.write(data);
                if (freeID.getLength() == data.length) {
                    //perfect fit, remove node
                    removeNode(freeID);
                }
                else {
                  //update node bounds
                    freeID.setLength(freeID.getLength() - data.length);
                    freeID.setOffset(freeID.getOffset() + data.length); 
                }
            }
            else {
                essayHandle = new Handle(memoryFile.length(), data.length);
                memoryFile.seek(memoryFile.length());
                memoryFile.write(data);
            }
        }
        found.setEssayHandle(essayHandle);
        return found;
    }
    
    /**
     * 
     * @param id to update
     * @param name updated
     * @return the updated record
     * @throws IOException
     */
    public Record update(String id, String name) throws IOException {
        Record found = searchHash(id);
        if (found == null) {
            int errorCode = insertName(id, name);
            if (errorCode == 0) {
                System.out.println(name + " insertion failed since the pid "
                    + id + "belongs to another student");
                return null;
            }
            else if (errorCode == 1) {
                System.out.println(name + " insertion failed. "
                    + "Attempt to insert in a full bucket");
                return null;
            }
            else {
                System.out.println(name + " inserted.");
            }
            return searchHash(id);
        }
        else {
            System.out.println("Student " + id + " updated to " + name);
            String oldName = readFromMem(found.getNameHandle());
            if (oldName.equals(name)) {
                return found;
            }
            Node nameNode = new Node(found.getNameHandle().getLength(),
                (int)found.getNameHandle().getPosition());
            addToFree(nameNode);
            clean();
            Handle nameHandle = new Handle();
            byte[] data = name.getBytes();

            Node freeID = findFree(data.length);
            if (freeID != null) {
                nameHandle = new Handle(freeID.getOffset(), data.length);
                memoryFile.seek(freeID.getOffset());
                memoryFile.write(data);
                if (freeID.getLength() == data.length) {
                    //perfect fit, remove node
                    removeNode(freeID);
                }
                else {
                    //update node bounds
                    freeID.setLength(freeID.getLength() - data.length);
                    freeID.setOffset(freeID.getOffset() + data.length); 
                }
            }
            else {
                nameHandle = new Handle(memoryFile.length(), data.length);
                memoryFile.seek(memoryFile.length());
                memoryFile.write(data);
            }
            found.setNameHandle(nameHandle);
            return found;
        }
    }
    
    /**
     * 
     * @param n to remove
     */
    private void removeNode(Node n) {
        int offset = n.getOffset();
        Node temp = head;
        if (temp.getOffset() == offset) {
            head = head.getNext();
        }
        while (temp.getNext() != null) {
            if (temp.getNext().getOffset() == offset) {
                temp.setNext(temp.getNext().getNext());
                return;
            }
            temp = temp.getNext();
        }
    }


    /**
     * 
     * @param length is the length of free block
     * @return the node
     */
    private Node findFree(int length) {
        Node temp = head;
        while (temp != null) {
            if (temp.getLength() >= length) {
                return temp;
            }
            temp = temp.getNext();
        }
        return null;
    }


    /**
     * @param id is the id to remove
     * @return the string removed
     * @throws IOException can throw
     */
    public String remove(String id) throws IOException {
        
        String ret = "";
        Record hash = searchHash(id);
        if (hash == null) {
            ret = id + " is not found in the database."; 
        }
        else {
            memoryFile.seek(hash.getNameHandle().getPosition());
            byte[] data = new byte[hash.getNameHandle().getLength()];
            memoryFile.read(data);
            String removed = new String(data);
            ret = id + " with full name " + removed + 
                " is removed from the database.";
            
            Node nameNode = new Node(hash.getNameHandle().getLength(),
                (int)hash.getNameHandle().getPosition());
            //add namespace to freeblocks
            addToFree(nameNode);
            clean();
            if (hash.hasEssayHandle()) {
                Node essayNode = new Node(hash.getEssayHandle().getLength(),
                    (int)hash.getEssayHandle().getPosition());
                //if not empty, add essatspace to freeblocks
                addToFree(essayNode);
                clean();
            }
            Record check = hashTable.remove(id); // check
            if (check == null) {
                System.out.println("remove error");
            }
        }
        return ret;
    }

    
    /**
     * 
     * @param id to clear
     * @return ret message
     * @throws IOException
     */
    public String clear(String id) throws IOException {
        String ret = "";
        Record hash = searchHash(id);
        if (hash == null) {
            ret = id + " is not found in the database.";
        }
        else {
            memoryFile.seek(hash.getNameHandle().getPosition());
            byte[] data = new byte[hash.getNameHandle().getLength()];
            memoryFile.read(data);
            String removed = new String(data);
            ret = "record with pid " + id + " with full name "
                + removed + " is cleared.";
            if (hash.hasEssayHandle()) {
                Node essayNode = new Node(hash.getEssayHandle().getLength(),
                    (int)hash.getEssayHandle().getPosition());
                //if not empty, add essatspace to freeblocks
                addToFree(essayNode);
                clean();
            }
            Handle clearedEssayHandle = new Handle();
            hash.setEssayHandle(clearedEssayHandle);
        }
        return ret;
    }

    /**
     * 
     * @param id to search
     * @return found record, null
     * @throws IOException
     */
    public Record searchHash(String id) throws IOException {
        Record hash = hashTable.search(id); // check
        if (hash == null) {
            return null;
        }
        else {
            int pos = (int)hash.getNameHandle().getPosition();
            int len = hash.getNameHandle().getLength();
            byte[] data = new byte[len]; 
            memoryFile.seek(pos);
            memoryFile.read(data);
        }
        if (!id.equals(hash.getPid())) {
            System.out.println(id + " " + hash.getPid());
        }
        return hash;
    }
    
    /**
     * removes uneeded free blocks at end of file
     * @throws IOException
     */
    public void clean() throws IOException {
        Node temp = head;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }
        if (temp.getLength() + temp.getOffset() == memoryFile.length()) {
            removeNode(temp);
            memoryFile.setLength(memoryFile.length() - temp.getLength());
        }
    }
    
    /**
     * 
     * @param node to add to free block list
     * @throws IOException 
     */
    private void addToFree(Node node) throws IOException {
        if (head == null) {
            head = node;
            return;
        }
        if (head.left() > node.left()) {
            if (node.getLength() + node.getOffset() == head.getOffset()) {
                head.setOffset(node.getOffset());
                head.setLength(head.getLength() + node.getLength());
                return;
            }
            Node temp = head;
            node.setNext(temp);
            head = node;
            return;
        }
        Node temp = head;
        while (temp != null) {
            if (temp.getNext() == null) {
                if (temp.right() == node.left()) {
                    temp.setLength(temp.getLength() + node.getLength());
                    return;
                }
                temp.setNext(node);
                return;
            }
            if (temp.getNext().left() > node.left()) {
                
                if (node.right() == temp.getNext().left()) {
                    temp.getNext().setOffset(node.getOffset());
                    temp.getNext().setLength(
                        temp.getNext().getLength() + node.getLength());
                    
                    if (temp.right() == temp.getNext().getOffset()) {
                        temp.setLength(temp.getLength() +
                            temp.getNext().getLength());
                        Node temp2 = temp.getNext();
                        temp.setNext(null);
                        temp.setNext(temp2.getNext());
                        temp2 = null;
                        return;
                    }
                    return;
                }
                else if (temp.right() == node.left()) {
                    temp.setLength(temp.getLength() + node.getLength());
                    return;
                }
                else {
                    node.setNext(temp.getNext());
                    temp.setNext(node);
                    return;
                }
            }
            temp = temp.getNext();
        }
    }
        
        
    



    /**
     * 
     * @return the has table
     */
    public HashFunction getHashTable() {
        return this.hashTable;
    }


    /**
     * 
     * @throws IOException
     */
    public void print() throws IOException {
        Record[] table = hashTable.toArray();
        System.out.println("Students in the database:");
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null || table[i].getTombstone()) {
                continue;
            }
            String name = readFromMem(table[i].getNameHandle());
            System.out.println(name + " at slot " + i);
        }
        if (head == null) {
            System.out.println("Free Block List:");
        }
        else {
            System.out.println("Free Block List:");
            Node temp = head;
            int count = 1;
            while (temp != null) {
                System.out.println("Free Block " + count
                    + " starts from Byte " + temp.getOffset()
                    + " with length " + temp.getLength());
                temp = temp.getNext();
                count++;
            }
        }
    }

    /**
     * 
     * @param h handle
     * @return string rep
     * @throws IOException
     */
    public String readFromMem(Handle h) throws IOException {
        if (h.empty()) {
            return "";
        }
        int len = h.getLength();
        int pos = (int)h.getPosition();
        byte[] data = new byte[len];
        memoryFile.seek(pos);
        memoryFile.read(data);
        String ret = new String(data);
        return ret;
    }

    /**
     * determines whether or not anything been deleted
     * 
     * @return whether or not there is free space
     */
    public boolean isFreeSpace() {
        return (head != null);
    }

}