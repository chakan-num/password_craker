/**
 * Created by simon on 2016-03-31.
 */
public class StringGenerator {
    int len;
    CharIterator[] iter;
    char[] character;

    StringGenerator(int _length){
        len = _length;
        iter = new CharIterator[len];
        character = new char[len];
        for(int i = 0; i<len; i++) {
            iter[i] = new CharIterator();
        }
        for(int i=0; i<len; i++)
            character[i] = iter[i].curr();

        iter[len-1].pos = -1;       //because initial value like 0000...
    }

    public boolean hasNextString(){
        for(int i=0; i<len; i++){
            if(iter[i].hasNext())
                return true;
        }
        return false;
    }

    public String getString(){
        if(this.hasNextString()){
            for(int i=len-1; i>=0; i--){
                if(iter[i].hasNext()) {
                    character[i] = iter[i].next();
                    break;
                } else{
                    iter[i].pos = 0;
                    character[i] = iter[i].curr();
                }
            }
        }

        String str = "";
        for(int i=0;i<len;i++)
            str += Character.toString(character[i]);

        return str;
    }
}
