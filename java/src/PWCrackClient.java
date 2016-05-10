import sun.rmi.transport.tcp.TCPTransport;

/**
 * Created by simon on 2016-04-28.
 */
public class PWCrackClient {
    public static void main(String[] args){
        try{
            TTransport transport;
            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            pwcrack.PWCrackService.Client = new PWCrackService.Client(protocol);

            work(client);

            transport.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void work(pwcrack.PWCrackService.Client client) throws Exception{
        String hashedPW = client.getHahedPW();

        while(!client.isDone()){
            String prefix = client.getTask();
            StringGenerator task = new StringGenerator(4);

            while(task.hasNextString()){
                if(client.isFindPW())
                    break;

                String PW = prefix + task.getString();
                String testHashedPW = StringToMD5.getMD5(PW);

                if(hashedPW.equals(testHashedPW)){
                    client.findPW(PW);
                    break;
                }
            }
        }
    }
}
