import java.util.Iterator;

/**
 * Created by simon on 2016-03-28.
 */
public class CharIterator implements Iterator<Character>{
    static String chrs = "0123456789"+"abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    int pos = 0;

    public Character next() {
        return chrs.charAt(++pos);
    }

    public boolean hasNext() {
        if (this.pos == chrs.length()-1){
            return false;
        } else {
            return true;
        }
    }

    public void remove() {
    }

    public char curr() {
        return chrs.charAt(pos);
    }

}
