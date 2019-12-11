/**
 * 
 * @author ryank99
 * @version 12.05
 */
public class HashFunction implements HashTable<String, Record> {
    private Record[] hashes;


    /**
     * 
     * @param hashSize
     *            is
     */
    public HashFunction(int hashSize) {
        hashes = new Record[hashSize];
    }
    
    /**
     * 
     * @return the size of the hash.
     */
    public int getSize() {
        return hashes.length;
    }

    /**
     * 
     * @param s
     *            is the string to be hashed
     * @param m
     *            hash table size
     * @return the hash value
     */
    private long sfold(String s, int m) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (Math.abs(sum) % m);
    }


    /**
     * 
     * @param sequenceID
     * @return
     */
    /*
     * public boolean isInsertable(String sequenceID) {
     * if (!search(sequenceID).equals("") || size == hashes.length) {
     * return false;
     * }
     * return true;
     * }
     */

    /**
     * 
     * @param sequenceID
     *            is the sequenceID to look for
     * @return whether or not the sequence ID is in the hash table
     */
    public int hash(String id) {
        return (int)sfold(id, hashes.length);

    }

    /**
     * 
     * @param sequenceID
     * @return
     */
    /*
     * public int searchNum(String sequenceID) {
     * long hash = sfold(sequenceID, hashes.length);
     * int j = 0;
     * for (int i = (int)hash; hashes[i] != null && j < hashes.length; i++) {
     * if (hashes[i].getString().equals(sequenceID) && !hashes[i]
     * .isTombstone()) {
     * return i; // should be line from mem manager
     * }
     * j++;
     * }
     * return -1;
     * }
     */



    /*
     * public int searchEmpty(String sequenceID) {
     * long hash = sfold(sequenceID, hashes.length);
     * int j = 0;
     * if (size == hashes.length) {
     * return -1;
     * }
     * for (int i = (int)hash; j < hashes.length; i++) {
     * if (hashes[i] == null || hashes[i].isTombstone()) {
     * return i;
     * }
     * if (hashes[i].getString().equals(sequenceID)) {
     * return -1; // should be line from mem manager
     * }
     * j++;
     * }
     * return -1;
     * }
     */

    /**
     * @param id is the id
     * @param handle is the hash
     * @return the insert id
     */
    public int insert(String id, Record handle) {
        int hash = (int)sfold(id, hashes.length);
        if(hashes[hash] == null ) {
            hashes[hash] = handle;
            return hash;
        }
        if(hashes[hash].getTombstone()) {
            hashes[hash].setTombstone(false);
            hashes[hash] = handle;
            return hash;
        }
        
        int x = hash + 1;
        while(x != hash) {
            if(x % 32 == 0) {
                x-=32;
            }
            Record hashObj = hashes[x];
            if(hashObj == null) {
                hashes[x] = handle;
                return x;
            }
            if(hashObj.getTombstone()) {
                hashes[x] = handle;
                return x;
            }
            if(hashObj.getPid().equals(id) && !hashObj.getTombstone()) {
                System.out.println("error, already inserted");
                return -1;
            }
            
            x++;
        }
        System.out.println("overflow");
        return -1;
    }


    /**
     * Precondition: the sequenceID exists in the hash.
     * 
     * @param sequenceID
     *            is the sequenceID to remove
     * @param amountToSkip is the amt to skip
     * @return the hash
     */
    public Record remove(String id, Integer amountToSkip) {
        int hash = (int)sfold(id, hashes.length);
        if(hashes[hash] == null) {
            return null;
        }
        if(hashes[hash].getPid().equals(id)) {
            hashes[hash].setTombstone(true);
            return hashes[hash];
        }
        
        int x = hash + 1;
        while(x != hash) {
            
            if(x % 32 == 0) {
                x-=32;
            }
            
            if(hashes[x] == null) {
                return null;
            }
            if(hashes[x].getPid().equals(id)) {
                hashes[hash].setTombstone(true);
                return hashes[x];
            }
            x++;
        }
        return null;
    }


    /**
     * @return prints the hash.
     */
    public Record[] print() {
        return hashes;
    }


    /**
     * 
     * @param id is the id
     * @param amountToSkip is the amt to skip
     * @return the hash
     */
    public Record search(String id, Integer skip) {
        int hash = (int)sfold(id, hashes.length);
        if(hashes[hash] != null && hashes[hash].getPid().equals(id) && !hashes[hash].getTombstone()) {
            return hashes[hash];
        }
        int x = hash + 1;
        while(x != hash) {
            
            if(x % 32 == 0) {
                x-=32;
            }
            Record hashObj = hashes[x];
            if(hashObj == null) {
                return null;
            }
            if(hashObj.getTombstone()) {
                return null;
            }
            if(hashObj.getPid().equals(id) && !hashObj.getTombstone()) {
                return hashObj;
            }
            x++;
            
        }
        return null;
    }

}