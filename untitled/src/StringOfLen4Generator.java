/**
 * Created by simon on 2016-03-28.
 */
public class StringOfLen4Generator {
    static CharIterator[] iter = new CharIterator[4];
    char[] ch = new char[4];

    StringOfLen4Generator(){
        for(int i=0;i<4;i++){
            iter[i] = new CharIterator();
        }

        for(int i=0;i<4;i++){
            ch[i] = iter[i].next();
        }
        ch[3] = iter[3].curr();
    }

    boolean hasMoreStr(){
        if(iter[0].hasNext())
            return true;
        else
            return false;
    }

    String getNext(){
        String result = "";
        if(iter[3].hasNext()) {
            ch[3] = iter[3].next();
            for(int i=0;i<4;i++)
                result += Character.toString(ch[i]);
            return result;
        } else{
            iter[3].pos = 0;
            ch[3] = iter[3].next();
            if(iter[2].hasNext()){
                ch[2] = iter[2].next();
                for(int i=0;i<4;i++)
                    result += Character.toString(ch[i]);
                return result;
            } else{
                iter[2].pos = 0;
                ch[2] = iter[2].next();
                if(iter[1].hasNext()){
                    ch[1] = iter[1].next();
                    for(int i=0;i<4;i++)
                        result += Character.toString(ch[i]);
                    return result;
                } else{
                    iter[1].pos = 0;
                    ch[1] = iter[1].next();
                    if(iter[0].hasNext()) {
                        ch[0] = iter[0].next();
                        for(int i=0;i<4;i++)
                            result += Character.toString(ch[i]);
                        return result;
                    }
                    else
                        return null;
                }
            }
        }
    }
}
