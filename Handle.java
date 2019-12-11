/**
 * 
 * @author ryank99
 * @version 12.09
 */
public class Handle {
    private long offset;
    private int length;


    /**
     * 
     * @param strPosition is the position
     * @param strLength the String length
     */
    public Handle(long strPosition, int strLength) {
        this.offset = strPosition;
        this.length = strLength;
    }
    
    public Handle() {
        this.offset = -1;
        this.length = -1;
    }
    
    public boolean empty() {
        return this.length == -1;
    }


    /**
     * 
     * @return the position of the string
     */
    public long getPosition() {
        return offset;
    }


    /**
     * 
     * @return the length of the string
     */
    public int getLength() {
        return length;
    }


    /**
     * 
     * @param positionToSet
     *            new position of string
     */
    public void setPosition(int positionToSet) {
        this.offset = positionToSet;
    }


    /**
     * 
     * @param lengthToSet
     *            new length of string
     */
    public void setLength(int lengthToSet) {
        this.length = lengthToSet;
    }
}
