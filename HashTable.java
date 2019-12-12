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


    /**
     * 
     * @param id to remove
     * @return removed record
     */
    public K remove(T id);


    /**
     * 
     * @param id to hash
     * @return hashvalue
     */
    public int hash(T id);


    /**
     * 
     * @param id is the id 
     * @return the search
     */
    public K search(T id);
    
    /**
     * 
     * @return the arr rep
     */
    public K[] toArray();

}
