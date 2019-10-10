import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

public class CommunicationModule{
    public final int FRAGMENT_SIZE = 8192;
    private Dispatcher dispatcher;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Queue<DatagramPacket> outgoing_queue;
    private Queue<DatagramPacket> incoming_queue;
    //TODO: Create history with a hashmap.

    private class HandlePackets implements Runnable{
        private DatagramPacket dp;
        private byte[] service;

        public HandlePackets(DatagramPacket data){
            dp = data;
        }

        public void run(){
            String received = new String( dp.getData(), 0, dp.getLength() );
            String response = dispatcher.dispatch( received );

            InetAddress clientAddress = dp.getAddress();
            int port = dp.getPort();
            service  = response.getBytes();
            DatagramPacket responsePacket   = new DatagramPacket( service, service.length, clientAddress, port );
            synchronized ( outgoing_queue ){
                outgoing_queue.add(responsePacket);
            }
        }
    }

    public CommunicationModule(int port, Dispatcher dispatch) throws IOException{
        outgoing_queue = new LinkedList<>();
        incoming_queue = new LinkedList<>();

        socket     = new DatagramSocket( port );
        dispatcher = dispatch;

        new Thread( () -> {
            while(true){
                synchronized ( outgoing_queue ){
                    if( !outgoing_queue.isEmpty() ){
                        send();
                    }
                }
            }
        }).start();

        new Thread( () -> {
            while(true){
                synchronized ( incoming_queue ){
                    if( !incoming_queue.isEmpty() ){
                        receive();
                    }
                }
            }
        }).start();
    }

    public void listen() throws IOException{
        while(true){
            byte [] buffer = new byte[FRAGMENT_SIZE];
            DatagramPacket packet = new DatagramPacket( buffer, 0, buffer.length );
            socket.receive( packet );

            synchronized ( incoming_queue ){
                incoming_queue.add(packet);
            }
        }
    }

    public void receive(){
        DatagramPacket pack;
        synchronized ( incoming_queue ){
            pack = incoming_queue.remove();
        }

        Runnable run = new HandlePackets(pack);
        new Thread(run).start();
    }

    private void send(){
        DatagramPacket response;
        try{
            synchronized ( outgoing_queue ){
                response = outgoing_queue.poll();
            }

            socket.send( response );
        }catch(Exception exception){

        }
    }
}
