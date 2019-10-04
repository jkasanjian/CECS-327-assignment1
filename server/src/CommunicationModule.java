import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CommunicationModule{
    public final int FRAGMENT_SIZE = 8192;
    private Dispatcher dispatcher;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buffer;
    private byte[] service;

    public CommunicationModule(int port) throws IOException{
        buffer = new byte[FRAGMENT_SIZE];

        socket     = new DatagramSocket( port );
        packet     = new DatagramPacket( buffer,  buffer.length );
        dispatcher = new Dispatcher();
    }

    public void listen() throws IOException{
        while(true){
            socket.receive( packet );
            String received = new String( packet.getData(), 0, packet.getLength() );
            String response = dispatcher.dispatch( received );

            InetAddress clientAdress = packet.getAddress();
            int port = packet.getPort();
            service  = response.getBytes();
            packet   = new DatagramPacket( service, service.length, clientAdress, port );
            socket.send(packet);
        }
    }
}