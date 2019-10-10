package rpc;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class CommunicationModule{
    public final int FRAGMENT_SIZE = 8192;
    private int serverPort;
    private InetAddress serverAddress;
    private DatagramSocket socket;
    private Queue<DatagramPacket> asyncQueue;

    public CommunicationModule(int serverPort) throws UnknownHostException, SocketException {
        this.serverPort = serverPort;
        serverAddress = InetAddress.getLocalHost();
        socket = new DatagramSocket();

        asyncQueue = new LinkedList<>();

        new Thread(()->{
            DatagramPacket packet;
            while (true) {
                while (!asyncQueue.isEmpty()) {
                    asyncSend();
                }
            }
        }).start();

    }

    public CommunicationModule(int serverPort, String serverAddress) throws UnknownHostException, SocketException {
        this.serverPort = serverPort;
        this.serverAddress = InetAddress.getByName(serverAddress);
        socket = new DatagramSocket();
        asyncQueue = new LinkedList<>();

    }


    public synchronized String syncSend(String s) throws IOException {
        byte[] buffer = s.getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        socket.send(send);

        buffer = new byte[FRAGMENT_SIZE];
        DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
        socket.receive(recv);

        return new String(recv.getData(), 0, recv.getLength());
    }

    public void asyncSend(String s) {
        byte[] buffer = s.getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        synchronized (asyncQueue) {
            asyncQueue.add(send);
        }
    }

    private void asyncSend() {
        DatagramPacket response;
        synchronized ( asyncQueue ){
            response = asyncQueue.poll();
        }
        try {
            socket.send( response );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}