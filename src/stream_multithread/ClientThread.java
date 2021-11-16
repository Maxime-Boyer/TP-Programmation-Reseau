package stream_multithread; /***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

import java.io.*;
import java.net.*;
import java.sql.SQLOutput;

public class ClientThread extends Thread {

    private Socket clientSocket;
    private int idClient;

    ClientThread(Socket s, int idClient) {
        this.clientSocket = s;
        this.idClient = idClient;
    }

    /**
     * receives a request from client then sends an echo to the client
     * @param clientSocket the client socket
     **/
    public void run() {
        try {
            BufferedReader socIn = null;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            String line = socIn.readLine();
            while (line != null) {
                System.out.println("\nMessage du client "+idClient+":");
                System.out.println(line);
                socOut.println(line);
                line = socIn.readLine();
            }
            System.out.println("\n---     Deconnexion du client "+idClient+"     ---");
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

}

  