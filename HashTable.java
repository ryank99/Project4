/**
 * @version 12.05
 * @author ryank99
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


    public K remove(T id);



    public int hash(T id);


    /**
     * 
     * @param id is the id 
     * @param amountToSkip amt to skip
     * @return the search
     */
    public K search(T id);
    
    /**
     * 
     * @return the arr rep
     */
    public K[] toArray();

}
