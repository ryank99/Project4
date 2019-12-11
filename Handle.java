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
    
    /**
     * default constructor
     */
    public Handle() {
        this.offset = -1;
        this.length = -1;
    }
    
    /**
     * 
     * @return whether handle empty
     */
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
     * @param pos to set
     */
    public void setPosition(int pos) {
        this.offset = pos;
    }


    /**
     * 
     * @param length to set
     */
    public void setLength(int length) {
        this.length = length;
    }
}
