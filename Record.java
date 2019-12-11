
public class Record {

    String pid; 
    Handle name;
    Handle essay;
    private int tablePos;
    private boolean tombstone = false;
    
    public Record(String pid, Handle name) {
        this.pid = pid;
        this.name = name;
        this.essay = new Handle();
        this.tablePos = -1;
    }
    
    public Record(String pid, Handle name, Handle essay) {
        this.pid = pid;
        this.name = name;
        this.essay = essay;
        this.tablePos = -1;
    }
    
    public int getTablePos() {
        return tablePos;
    }
    
    public void setTablePos(int pos) {
        tablePos = pos;
    }
    public String getPid() {
        return pid;
    }
    
    public Handle getNameHandle() {
        return name;
    }
    
    public Handle getEssayHandle() {
        return essay;
    }
    
    public boolean hasEssayHandle() {
        return !essay.empty();
    }
    
    public void setEssayHandle(Handle h) {
        essay = h;
    }
    
    
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
