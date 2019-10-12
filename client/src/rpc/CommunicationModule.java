package rpc;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class CommunicationModule{
    public final int FRAGMENT_SIZE = 65000;
    private int serverPort;
    private InetAddress serverAddress;
    private DatagramSocket socket;
    private Queue<DatagramPacket> asyncQueue;

    private static CommunicationModule communicationModule = null;

    public static CommunicationModule GetInstance() throws SocketException, UnknownHostException {
        if (communicationModule == null) {
            communicationModule =  new CommunicationModule();
        }
        return communicationModule;
    }

    public void init(int port) {
        serverPort = port;
    }

    public void init(int port, String address) throws UnknownHostException {
        serverPort = port;
        serverAddress = InetAddress.getByName(address);
    }

    private CommunicationModule() throws SocketException, UnknownHostException {
        serverAddress = InetAddress.getLocalHost();
        socket = new DatagramSocket();
        asyncQueue = new LinkedList<>();
        serverPort = 9000;
        socket.setSoTimeout(100);
    }

    private CommunicationModule(int serverPort) throws UnknownHostException, SocketException {
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

    private CommunicationModule(int serverPort, String serverAddress) throws UnknownHostException, SocketException {
        this.serverPort = serverPort;
        this.serverAddress = InetAddress.getByName(serverAddress);
        socket = new DatagramSocket();
        asyncQueue = new LinkedList<>();

    }


    public synchronized String syncSend(String s) throws IOException {
        byte[] buffer = s.getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        DatagramPacket recv;
        while(true) {
            try {
                socket.send(send);
                buffer = new byte[FRAGMENT_SIZE];
                recv = new DatagramPacket(buffer, buffer.length);
                socket.receive(recv);
                break;
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
        }

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

    /**
     * Getters and setters
     */
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }
}