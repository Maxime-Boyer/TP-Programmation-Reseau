package beans;

import java.io.*;
import java.net.*;


public class Server {

    private DatagramSocket serverSocket;

    private String ip;

    private int port;

    public Server(String ip, int port) throws SocketException, IOException{
        this.ip = ip;
        this.port = port;
        // socket used to send
        serverSocket = new DatagramSocket();
    }

    public void send(String argument) throws IOException{
        // make datagram packet
        byte[] message = ("Multicasting..."+argument).getBytes();
        DatagramPacket packet = new DatagramPacket(message, message.length,
                InetAddress.getByName(ip), port);
        // send packet
        serverSocket.send(packet);
    }

    public void close(){
        serverSocket.close();
    }

    public static void main(String[] args) {
        try {
            final String ip = args[0];
            final int port = Integer.parseInt(args[1]);
            Server server = new Server(ip, port);
            server.send(args[2]);
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
