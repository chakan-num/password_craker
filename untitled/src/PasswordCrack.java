import java.util.Queue;
import java.util.Scanner;

/**
 * Created by simon on 2016-03-28.
 */
public class PasswordCrack {
    static volatile boolean isDone = false;
    private static final int SIZE = 1000;

    public static void main(String[] args){
        int ThreadNum;
        String hashedPW;

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the hashed 8-digit password : ");
        hashedPW = scan.nextLine();

        System.out.print("Enter thread number : ");
        ThreadNum = scan.nextInt();

        //make prefix in queue
        Queue prefixQueue = new MyBlockingQueue(SIZE);
        PrefixProducer producer = new PrefixProducer(prefixQueue);
        producer.start();

        Worker[] th = new Worker[ThreadNum];
        for(int i=0;i<ThreadNum;i++){
            th[i] = new Worker(prefixQueue, hashedPW);
            th[i].start();
        }
        try {
            for (int i = 0; i < ThreadNum; i++)
                th[i].join();
        } catch(Exception a){}

        System.out.println("end");
    }

    public static class Worker extends Thread {
        Queue shareQueue;
        String hashedPW;

        Worker(Queue _queue, String _hashedPW){
            shareQueue = _queue;
            this.hashedPW = _hashedPW;
        }

        public void run(){
            //wait producer and get
            //should check while waiting, task is done
            try {
                while(!isDone) {
                    String prefix = (String) shareQueue.remove();
                    StringGenerator task = new StringGenerator(4);

                    while (task.hasNextString()) {
                        if(isDone)
                            break;

                        String testPW = StringToMD5.getMD5(prefix + task.getString());

                        if (hashedPW.equals(testPW)) {
                            isDone = true;
                            System.out.println("solve : " + hashedPW);
                            break;
                        }
                    }
                }
            } finally{
//                lock.unlock();
            }
        }

    }

    public static class PrefixProducer extends Thread{
        Queue taskQueue;

        PrefixProducer(Queue _queue){
            taskQueue = _queue;
        }

        public void run(){
            StringGenerator taskProducer = new StringGenerator(4);

            while(taskProducer.hasNextString()){
                taskQueue.add(taskProducer.getString());
            }

        }
    }
}
