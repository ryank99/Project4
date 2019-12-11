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
     * @param length is the length
     * @return  the hash object
     * @throws IOException
     */
    public Record insertName(String id, String name)
        throws IOException {
        Record hashObject = searchHash(id);
        // returns null if seqeunce already exists. Or overflow.
        if (hashObject != null && hashObject.getPid().equals(id)) {
            return null;
        }
        byte[] data = name.getBytes();
        
        //add to end
        Handle nameHandle;
        if(head == null) {
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
                if(freeID.getLength() == data.length) {
                    //perfect fit, remove node
                    removeNode(freeID);
                }
                else {
                  //update node bounds
                    freeID.setLength(freeID.getLength()-data.length);
                    freeID.setOffset(freeID.getOffset()+data.length); 
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
        int spot = hashTable.insert(id, hashObjectNew); // insert to the hash table
        return hashObjectNew;
    }
    
    public Record insertEssay(String id, String essay) throws Exception {
        essay += "\n";
        Record found = searchHash(id);
        if(found == null) {
            System.out.println("error");
        }
        byte[] data = essay.getBytes();
        
        Handle essayHandle;
        if(head == null) {
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
                if(freeID.getLength() == data.length) {
                    //perfect fit, remove node
                    removeNode(freeID);
                }
                else {
                  //update node bounds
                    freeID.setLength(freeID.getLength()-data.length);
                    freeID.setOffset(freeID.getOffset()+data.length); 
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

    public Record update(String id, String name) throws IOException {
        Record found = searchHash(id);
        if (found == null) {
           System.out.println(name + " inserted.");
           return insertName(id, name);
        }
        else {
            System.out.println("Student " + id + " updated to " + name);
            Node nameNode = new Node(found.getNameHandle().getLength(),
                (int)found.getNameHandle().getPosition());
            addToFree(nameNode);
            
            Handle nameHandle = new Handle();
            byte[] data = name.getBytes();

            Node freeID = findFree(data.length);
            if (freeID != null) {
                nameHandle = new Handle(freeID.getOffset(), data.length);
                memoryFile.seek(freeID.getOffset());
                memoryFile.write(data);
                if(freeID.getLength() == data.length) {
                    //perfect fit, remove node
                    removeNode(freeID);
                }
                else {
                    //update node bounds
                    freeID.setLength(freeID.getLength()-data.length);
                    freeID.setOffset(freeID.getOffset()+data.length); 
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
     * @param node is the node
     * @return the prev node
     */
    private Node findPrev(Node node) {
        
        Node temp = head;
        while (temp != null) {
            if (temp.getNext() == node) {
                return temp;
            }
            temp = temp.getNext();
        }
        return null;
    }
    
    private void removeNode(Node n) {
        int offset = n.getOffset();
        Node temp = head;
        if(temp.getOffset() == offset) {
            head = head.getNext();
        }
        while (temp.getNext() != null) {
            if(temp.getNext().getOffset() == offset) {
                temp.setNext(temp.getNext().getNext());
                return;
            }
            temp = temp.getNext();
        }
    }

    /**
     * 
     * @return the last node
     */
    private Node getLast() {
        if (head == null) {
            return null;
        }
        Node temp = head;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }
        return temp;
    }


    /**
     * 
     */
    private void setLast() {
        if (head.getNext() == null) {
            head = null;
            return;
        }
        Node temp = head;
        while (temp.getNext().getNext() != null) {
            temp = temp.getNext();
        }
        temp.setNext(null);

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
        String name;
        if (hash == null) {
            ret = id + " is not found in database."; 
        }
        else {
            memoryFile.seek(hash.getNameHandle().getPosition());
            byte[] data = new byte[hash.getNameHandle().getLength()];
            memoryFile.read(data);
            String removed = new String(data);
            ret = id + " with full name " + removed + " is removed from the database.";
            
            Node nameNode = new Node(hash.getNameHandle().getLength(),
                (int)hash.getNameHandle().getPosition());
            //add namespace to freeblocks
            addToFree(nameNode);
            if(hash.hasEssayHandle()) {
                Node essayNode = new Node(hash.getEssayHandle().getLength(),
                    (int)hash.getEssayHandle().getPosition());
                //if not empty, add essatspace to freeblocks
                addToFree(essayNode);
            }
            Record check = hashTable.remove(id); // check
            if (check == null) {
                System.out.println("remove error");
            }
        }
        return ret;
    }

    public String clear(String id) throws IOException {
        String ret = "";
        Record hash = searchHash(id);
        if (hash == null) {
            ret = id + " is not found in the databse.";
        }
        else {
            memoryFile.seek(hash.getNameHandle().getPosition());
            byte[] data = new byte[hash.getNameHandle().getLength()];
            memoryFile.read(data);
            String removed = new String(data);
            ret = "record with pid " + id + " with full name " + removed + " is cleared.";
            if(hash.hasEssayHandle()) {
                Node essayNode = new Node(hash.getEssayHandle().getLength(),
                    (int)hash.getEssayHandle().getPosition());
                //if not empty, add essatspace to freeblocks
                addToFree(essayNode);
            }
            Handle clearedEssayHandle = new Handle();
            hash.setEssayHandle(clearedEssayHandle);
        }
        return ret;
    }

    /**
     * 
     * @param str is the string
     * @return the obj
     * @throws IOException can throw
     */
    public Record searchHash(String id) throws IOException {
        int counter = 0;
        boolean isnameFound = false;
        Record hash = hashTable.search(id); // check
        if(hash == null) {
            return null;
        }
        else {
            int pos = (int)hash.getNameHandle().getPosition();
            int len = hash.getNameHandle().getLength();
            byte[] data = new byte[len]; 
            memoryFile.seek(pos);
            memoryFile.read(data);
            String name = new String(data);
        }
        if(!id.equals(hash.getPid())) {
            System.out.println(id + " " + hash.getPid());
        }
        return hash;
    }
    
    /**
     * 
     * @param node to add to free block list
     */
    private void addToFree(Node node) {
        if (head == null) {
            head = node;
            return;
        }
        if (head.getOffset() > node.getOffset()) {
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
                if (temp.getOffset() + temp.getLength() == node.getOffset()) {
                    temp.setLength(temp.getLength() + node.getLength());
                    return;
                }
                temp.setNext(node);
                return;
            }
            if (temp.getNext().getOffset() > node.getOffset()) {
                if (node.getLength() + node.getOffset() == temp.getNext()
                    .getOffset()) {
                    temp.getNext().setOffset(node.getOffset());
                    temp.getNext().setLength(temp.getNext().getLength() + node
                        .getLength());
                    if (temp.getOffset() + temp.getLength() == temp.getNext()
                        .getOffset()) {
                        temp.setLength(temp.getLength() + temp.getNext()
                            .getLength());
                        Node temp2 = temp.getNext();
                        temp.setNext(null);
                        temp.setNext(temp2.getNext());
                        temp2 = null;
                        return;
                    }
                    return;
                }
                else if (temp.getLength() + temp.getOffset() == node
                    .getOffset()) {
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
            Handle testing = table[i].getNameHandle();
            String name = readFromMem(table[i].getNameHandle());
            System.out.println(name + " at slot " + i);
        }
        if (head == null) {
            System.out.println("Free Block List: none");
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

    public String readFromMem(Handle h) throws IOException {
        if(h.empty()) {
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