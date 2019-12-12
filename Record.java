/**
 * 
 * @author Ryan Kirkpatrick
 * @version 12.09
 */
public class Record {

    private String pid; 
    private Handle name;
    private Handle essay;
    private int tablePos;
    private boolean tombstone = false;
    
    /**
     * 
     * @param pid id
     * @param name first,last
     */
    public Record(String pid, Handle name) {
        this.pid = pid;
        this.name = name;
        this.essay = new Handle();
        this.tablePos = -1;
    }
    
    /**
     * 
     * @param pid id
     * @param name first,last
     * @param essay e
     */
    public Record(String pid, Handle name, Handle essay) {
        this.pid = pid;
        this.name = name;
        this.essay = essay;
        this.tablePos = -1;
    }
    
    /**
     * 
     * @return pos
     */
    public int getTablePos() {
        return tablePos;
    }
    
    /**
     * 
     * @param pos position
     */
    public void setTablePos(int pos) {
        tablePos = pos;
    }
    
    /**
     * 
     * @return pid
     */
    public String getPid() {
        return pid;
    }
    
    /**
     * 
     * @return handle
     */
    public Handle getNameHandle() {
        return name;
    }
    
    /**
     * 
     * @return handle
     */
    public Handle getEssayHandle() {
        return essay;
    }
    
    /**
     * 
     * @return bool
     */
    public boolean hasEssayHandle() {
        return !essay.empty();
    }
    
    /**
     * 
     * @param h handle
     */
    public void setEssayHandle(Handle h) {
        essay = h;
    }
    
    /**
     * 
     * @param n name
     */
    public void setNameHandle(Handle n) {
        name = n;
    }
    /**
     * 
     * @return the tombstone
     */
    public boolean getTombstone() {
        return tombstone;
    }
    
    /**
     * 
     * @param bool is the bool
     */
    public void setTombstone(boolean bool) {
        tombstone = bool;
    }
}
