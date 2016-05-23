import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;  
import java.util.concurrent.BlockingQueue;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

import pwcrack.*;
/**
 * Created by simon on 2016-04-28.
 */

public class PWCrackServiceHandler implements PWCrackService.Iface{
	String Pw = null;
	String hashedPw;
	int id_generation = 0;

	LinkedList<Client_Node> ClientList = new LinkedList<Client_Node>();

	MyBlockingQueue MyQueue = new MyBlockingQueue(100000);
	MyBlockingQueue lostPrefixQueue = new MyBlockingQueue(100000);
	StringGenerator taskProducer = new StringGenerator(4);
	
	private TServer server;
	
	public Client_Node removeNodebyID(int c_id) {
/*
		Client_Node temp = ClientLIST.remove();
		while ( temp.client_ID != c_id ) {
			ClientList.add(temp);
			temp = ClientList.remove();
		}
		return temp;
*/
		if(ClientList.size() == 0)
			return null;

		Iterator<Client_Node> iter = ClientList.iterator();
		Client_Node temp = iter.next();
		while(temp != ClientList.getLast()){
			if(temp.client_ID == c_id)
				return temp;
			else
				temp = iter.next();
		}
		return null;
	}//find list by id

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
		ClientList.add(temp);
		
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
			if ( MyQueue.isEmpty() && lostPrefixQueue.isEmpty() ) {
				System.out.println("Password is not found");
				givetask = null;
			}
			else if( MyQueue.isEmpty() ){
				while(!lostPrefixQueue.isEmpty()){
					String temp = lostPrefixQueue.remove();
					MyQueue.add(temp);
				}
				givetask.prefix = MyQueue.remove();
			}
			else
				givetask.prefix = MyQueue.remove();
				
		}
		Client_Node temp = removeNodebyID(c_id);
		if(temp == null){
			temp = new Client_Node();
			temp.client_ID = c_id;
			temp.ping_timestamp = 0;
		}
		temp.last_prefix = givetask.prefix;
		ClientList.add(temp);
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
		if(temp == null){
			temp = new Client_Node();
			temp.client_ID = c_id;
			temp.last_prefix = null;
		}
		long time = System.currentTimeMillis();
		temp.ping_timestamp = time;
		ClientList.add(temp);
	}
}
