/**
 * Created by simon on 2016-04-28.
 */
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import pwcrack.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Queue;
import java.util.Scanner;

public class PWCrackServer {  
	public static PWCrackServiceHandler handler;
	public static PWCrackService.Processor processor;
	private static final int SIZE = 100000;

	public static void main(String[] args) {
		String PWtofind;
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the hashed 8-digit password : ");
		PWtofind = scan.nextLine();
    
		try {
			handler = new PWCrackServiceHandler();
			processor = new PWCrackService.Processor(handler);
			
			Runnable server = new Runnable() {
				public void run() {
					server(PWtofind);
				}
			};
			
			Runnable check_ping = new Runnable() {
				public void run() {
					check_ping();
				}
			};

			new Thread(server).start();
			new Thread(check_ping).start();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void server(String PWtofind){
		try{
			TServerTransport serverTransport = new TServerSocket(9090);
			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
			
			handler.hashedPw = PWtofind;
			for ( int i = 0; i < 100000; i++)
				handler.MyQueue.add(handler.taskProducer.getString());

			handler.setServer(server);      
			server.serve(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void check_ping() { 
		try {
			while(true) {
				Thread.sleep(2000);
				long curTime = System.currentTimeMillis();
				int clientNum = handler.ClientList.size();

				for ( int i = 0; i < clientNum; i++) {
					Client_Node targetClient = handler.ClientList.remove();
					if ( (curTime - targetClient.ping_timestamp) < 1500 )
						handler.ClientList.add(targetClient);
					else{
						String lostPrefix = targetClient.last_prefix;
						handler.lostPrefixQueue.add(lostPrefix);
					}
				}
/*
				if(handler.isDone()) {
					while(!handler.ClientQueue.isEmpty())
						handler.ClientQueue.remove();
				}
*/
				if(handler.id_generation > 0 && handler.ClientList.isEmpty()) {
					handler.stopServer();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
