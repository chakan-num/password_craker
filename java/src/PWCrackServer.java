/**
 * Created by simon on 2016-04-28.
 */
import pwcrack.*;

import java.util.Queue;
import java.util.Scanner;

public class PWCrackServer {
    public static PWCrackServiceHandler handler;
    public static PWCrackService.Processor processor;
    private static final int SIZE = 1000;

    public static void main(String[] args) {
        String hashedPW;

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the hashed 8-digit password : ");
        hashedPW = scan.nextLine();

				Queue PrefixQueue = new MyBlockingQueue(SIZE);
				PrefixProducer producer = new PrefixProducer(prefixQueue);
				producer.start();

        try {
            handler = new PWCrackServiceHandler();
            processor = new PWCrackService().Processor(handler);

            Runnable server = new Runnable() {
                @Override
                public void run() {
                    server(processor,hashedPW,PrefixQueue);
                }
            };

            new Thread(server).start();
        } catch (Exception e){e.printStackTrace();}
    }

    public static void server(pwcrack.PWCrackService.Processor processor,String hashedPW,Queue PrefixQueue){
        try{
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

            server.serve();
            processor.setHashedPW(hashedPW);
            
						while(!processor.isDone())
                Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
