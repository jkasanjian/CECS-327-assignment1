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
    private byte[] buffer;
    private byte[] service;
    private Queue<DatagramPacket> queue;

    public CommunicationModule(int port) throws IOException{
        buffer = new byte[FRAGMENT_SIZE];
        queue  = new LinkedList<>();

        socket     = new DatagramSocket( port );
        packet     = new DatagramPacket( buffer,  buffer.length );
        dispatcher = new Dispatcher();

        new Thread( () -> {
            while(true){
                if( !queue.isEmpty() ){
                    send();
                }
            }
        }).start();
    }

    public void listen() throws IOException{
        while(true){
            socket.receive( packet );
            String received = new String( packet.getData(), 0, packet.getLength() );


            new Thread( () -> {
                String response = dispatcher.dispatch( received );
                InetAddress clientAddress = packet.getAddress();
                int port = packet.getPort();
                service  = response.getBytes();
                packet   = new DatagramPacket( service, service.length, clientAddress, port );
                synchronized ( queue ){
                    queue.add(packet);
                }
            }).start();
        }
    }

    private void send(){
        DatagramPacket response;
        try{
            synchronized ( queue ){
                response = queue.poll();
            }

            socket.send( response );
        }catch(Exception exception){

        }
    }
}