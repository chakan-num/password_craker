import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by simon on 2016-04-28.
 */
import pwcrack.*;

public class PWCrackClient {
	//public String err_flag;
	private static Lock lock = new ReentrantLock();
	private static PWCrackService.Client client;
	private static ClientInfo c_info;
	private TTransport transport;
	private TProtocol protocol;
	private static String FindingPW;
	
	public static void main(String[] args){
		PWCrackClient pwclient = new PWCrackClient();
		//pwclient.err_flag = args[1];
	}

	PWCrackClient() {
		try {
			// Connection Start
			transport = new TSocket("localhost",9090);
			protocol = new TBinaryProtocol(transport);
			transport.open();
			
			client = new PWCrackService.Client(protocol); // Connect
			c_info = client.getInfo();
			FindingPW = client.getPWtoFind();

			System.out.println("Client - Constructor - client_id : " + c_info.client_id);
			
			Runnable work = new Runnable() {
				public void run() {
					work();
				}
			};

			Runnable send_ping = new Runnable() {
				public void run() {
					send_ping();
				}
			};

			Thread work_thread = new Thread(work);
			Thread send_ping_thread = new Thread(send_ping);
			work_thread.start();
			send_ping_thread.start();
			work_thread.join(); //wait until the thread ends
			send_ping_thread.join(); 
      
			transport.close();
    } catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void work() {
		try {
			while(true){
				lock.lock();
				int cur_client_id = c_info.client_id;
				boolean check_isDone = client.isDone();
				lock.unlock();
				if(check_isDone) break;
				
				String prefix;
				lock.lock();
				if ( client.getTask(cur_client_id) != null )
					prefix = client.getTask(cur_client_id).prefix;
				else{
					lock.unlock();
					break;
				}
				lock.unlock();

				StringGenerator task = new StringGenerator(4);
				while(task.hasNextString()){
					String test_pw = prefix + task.getString();
    	    //String testHashedPW = StringToMD5.getMD5(PW); /* Delete for Testing Convenience */
					if(FindingPW.equals(test_pw)) {
						//if(err_flag != null ) while(1) ;
						lock.lock();
						client.SuccessCrack(test_pw);
						lock.unlock();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void send_ping() {
		try {
			while(true) {
				lock.lock();
				client.ping(c_info.client_id);
				if(client.isDone()) {
					//if(err_flag != null) while(1) ;
					lock.unlock();
					client.deleteNodebyID(c_info.client_id);
					break;
				}
				lock.unlock();
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
