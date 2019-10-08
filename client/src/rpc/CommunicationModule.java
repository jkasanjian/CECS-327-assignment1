package rpc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CommunicationModule{
    public final int FRAGMENT_SIZE = 8192;
    private int serverPort = 8000;
    private InetAddress serverAddress;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buffer;
    private byte[] service;
    private Queue<DatagramPacket> queue;
    private Proxy proxy;

    public CommunicationModule(int serverPort) throws IOException{
        buffer = new byte[FRAGMENT_SIZE];
        queue  = new LinkedList<>();
        this.serverPort = serverPort;
        serverAddress = InetAddress.getLocalHost();
        socket     = new DatagramSocket(  );
        packet     = new DatagramPacket( buffer,  buffer.length );
        proxy      = new Proxy();

        while (true) {
            
        }
    }

    public void listen() throws IOException{
        while(true){
            socket.receive( packet );
            String received = new String( packet.getData(), 0, packet.getLength() );

        }
    }

    private void send(byte[] data){
        new Thread( () -> {
            packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
            try{
                synchronized ( queue ){
                    packet = queue.poll();
                }

                socket.send( packet );
            }catch(Exception exception){

            }
        }).start();
    }
}