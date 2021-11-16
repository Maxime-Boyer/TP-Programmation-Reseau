package stream_multithread; /***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

import java.io.*;
import java.net.*;

public class EchoServerMultiThreaded  {

    /**
     * main method
     * @param args port
     *
     **/
    public static void main(String args[]){
        ServerSocket listenSocket;
        int idClient = 1;

        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("\n---     Connexion du client "+idClient+"     ---");
                ClientThread ct = new ClientThread(clientSocket, idClient);
                idClient++;
                ct.start();
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
}

  