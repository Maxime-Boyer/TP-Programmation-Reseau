package stream_multithread; /***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

import beans.Conversation;
import beans.Serveur;
import persistence.XMLModifier;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class EchoServerMultiThreaded  {

    //TODO : ajouter état connecté ou non à un client


    /**
     * main method
     * @param args port
     *
     **/
    public static void main(String args[]){

        ServerSocket listenSocket;

        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            Serveur serveur = new Serveur(); //creer le serveur
            System.out.println("Server ready...");

            // ecoute de nouvelles connexion, creation d'un nouveau thread en cas de connexion
            while (true) {
                Socket clientSocket = listenSocket.accept();
                ClientThread ct = new ClientThread(clientSocket,serveur);
                ct.start();
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

}

  