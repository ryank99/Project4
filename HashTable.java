/**
 * 
 * @author chrisd98, jdins22
 * @version 05.09
 * @param <T> is a comparator
 * @param <K> is a comparator
 */
public interface HashTable<T extends Comparable<T>, K> {

    /**
     * Precondition: isInsertable is true.
     * 
     * @param id is the ID to hash
     * @param handle is the handle
     * @return the insert
     */
    public int insert(T id, K handle);


    /**
     * Precondition: the sequenceID exists in the hash.
     * 
     * @param sequenceID
     *            is the sequenceID to remove
     * @param amountToSkip is the amt to skip
     * @return the remove
     */
    public K remove(T sequenceID, Integer amountToSkip);


    /**
     * 
     * @param sequenceID
     *            is the sequenceID to look for
     * @return the sfold value
     */
    public int hash(T sequenceID);


    /**
     * 
     * @param id is the id 
     * @param amountToSkip amt to skip
     * @return the search
     */
    public K search(T id, Integer amountToSkip);
    
    /**
     * 
     * @return the print
     */
    public K[] print();

}
