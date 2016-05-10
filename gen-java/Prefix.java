import java.util.Queue;

public class Prefix extends Thread{
    Queue taskQueue;

    Prefix(Queue _queue){
	taskQueue = _queue;
    }

    public void run(){
        StringGenerator taskProducer = new StringGenerator(4);

        while(taskProducer.hasNextString()){
            taskQueue.add(taskProducer.getString());
        }

    }
}

