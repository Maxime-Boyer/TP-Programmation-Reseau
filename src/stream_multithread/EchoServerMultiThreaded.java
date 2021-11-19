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
import java.util.HashMap;
import java.util.Map;

public class EchoServerMultiThreaded  {

    //TODO : ajouter état connecté ou non à un client

    //key = nomUtilisateur
    static HashMap<String, ClientThread> clientThreads = new HashMap<>();
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
                System.out.println("Connexion from:" + clientSocket.getInetAddress());
                //Buffer de lecture de la socket
                BufferedReader socInClient = null;
                socInClient = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                //Buffer de sortie de la socket
                PrintStream socOutClient = null;
                socOutClient = new PrintStream(clientSocket.getOutputStream());

                //On recupere le username de l'utilisateur
                //provoque decalage et bug de l'affichage
                String nomUtilisateur = socInClient.readLine();

                ClientThread ct = new ClientThread(clientSocket, serveur, nomUtilisateur, socInClient, socOutClient);
                clientThreads.put(nomUtilisateur, ct);
                ct.start();
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

}

  