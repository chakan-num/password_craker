import org.apache.thrift.TException;

import java.util.Queue;
import pwcrack.*;
/**
 * Created by simon on 2016-04-28.
 */
public class PWCrackServiceHandler implements PWCrackService.Iface{
    String Pw = "";
		String hashedPw;

    public boolean isDone(){
        if(Pw == "")
            return false;
        else
            return true;
    }

    public String getTask(){
    }
    //give prefix to client from queue

    public void findPW(String PW){
        Pw = PW;
    }
    //find PW from client

    public String returnPW(){ 
			return Pw;
		}


    public String getHashedPW(){
			return hashedPw;
    }
    //hashedPW give to client

    public void setHashedPW(String hashedPW){
			hashedPw = hashedPW;
    }
    //hashedPW set from server

}
