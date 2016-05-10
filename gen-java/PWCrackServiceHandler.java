import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;  
import java.util.concurrent.BlockingQueue;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pwcrack.*;
/**
 * Created by simon on 2016-04-28.
 */

public class PWCrackServiceHandler implements PWCrackService.Iface{
	String Pw = null;
	String hashedPw;
	int id_generation = 0;

	BlockingQueue<Client_Node> ClientQueue = new ArrayBlockingQueue<Client_Node>(1000);

	MyBlockingQueue MyQueue = new MyBlockingQueue(1000);
	StringGenerator taskProducer = new StringGenerator(4);
	
	private TServer server;
	
	public Client_Node removeNodebyID(int c_id) {
		Client_Node temp = ClientQueue.remove();
		while ( temp.client_ID != c_id ) {
			ClientQueue.add(temp);
			temp = ClientQueue.remove();
		}
		return temp;
	}

	public void deleteNodebyID(int c_id) {
		removeNodebyID(c_id);
	}

	public void setServer(TServer server) {
		this.server = server;
	}
	
	public void stopServer() {
		server.stop();
	}

	public ClientInfo getInfo() {
		ClientInfo info = new ClientInfo();
		info.client_id = id_generation++;
		
		Client_Node temp = new Client_Node();
		temp.client_ID = info.client_id;
		temp.last_prefix = null;
		temp.ping_timestamp = 0;
		ClientQueue.add(temp);
		
		return info;
	}

	public boolean isDone(){
		if(Pw == null) 
			return false;
		else
			return true;
  }

	public String getPWtoFind() {
		return hashedPw;
	}

  public Task getTask(int c_id){
		Task givetask = new Task();
		if ( taskProducer.hasNextString() ) {
			givetask.prefix = MyQueue.remove();
			MyQueue.add(taskProducer.getString());
		}
		else {
			if ( MyQueue.isEmpty() ) {
				System.out.println("Password is not found");
				givetask = null;
			}
			else
				givetask.prefix = MyQueue.remove();
		}
		Client_Node temp = removeNodebyID(c_id);
		temp.last_prefix = givetask.prefix;
		ClientQueue.add(temp);
		return givetask;
	}
  //give prefix to client from queue

	public void SuccessCrack(String ans){
		System.out.println("Server: Password is -> " + ans);
		Pw = ans;
  }
  //find PW from client

	public void ping(int c_id) {
		Client_Node temp = removeNodebyID(c_id);
		long time = System.currentTimeMillis();
		temp.ping_timestamp = time;
		ClientQueue.add(temp);
	}
}
