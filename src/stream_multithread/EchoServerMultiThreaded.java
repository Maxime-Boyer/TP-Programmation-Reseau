package stream_multithread; /***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

import beans.Conversation;
import beans.Serveur;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class EchoServerMultiThreaded  {

    /**
     * main method
     * @param args port
     *
     **/
    /**
     * TODO Maxime: javadoc (traduction ci-dessus)
     * @param clientSocket
     */
    public static void main(String args[]){

        ServerSocket listenSocket;

        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            Serveur serveur = new Serveur(); //creer le serveur
            initialiserTest(serveur);
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

    public static void initialiserTest(Serveur serveur){
        Conversation conversationGroupe = new Conversation("TP prog réseaux");
        conversationGroupe.ajouterUtilisateur("Quentin");
        conversationGroupe.ajouterUtilisateur("Maxime");
        conversationGroupe.ajouterUtilisateur("Nathan");
        Conversation conversationGroupe1 = new Conversation("Soirée raclette");
        conversationGroupe.ajouterUtilisateur("Quentin");
        conversationGroupe.ajouterUtilisateur("Maxime");
        conversationGroupe.ajouterUtilisateur("Nathan");
        Conversation conversationGroupe2 = new Conversation("Diner à Paris");
        conversationGroupe.ajouterUtilisateur("Quentin");
        conversationGroupe.ajouterUtilisateur("Maxime");
        conversationGroupe.ajouterUtilisateur("Nathan");
        Conversation conversationGroupe3 = new Conversation("Aprem Surf");
        conversationGroupe.ajouterUtilisateur("Quentin");
        conversationGroupe.ajouterUtilisateur("Maxime");
        conversationGroupe.ajouterUtilisateur("Nathan");
        Conversation conversationGroupe4 = new Conversation("PLD Agile");
        conversationGroupe.ajouterUtilisateur("Quentin");
        conversationGroupe.ajouterUtilisateur("Maxime");
        conversationGroupe.ajouterUtilisateur("Nathan");
        Conversation conversationPrive = new Conversation("Quentin", "Maxime");
        conversationPrive.ajouterMessage("Quentin", "Hello max, on taffe ce matin?");
        conversationPrive.ajouterMessage("Maxime", "Flemme...");
        conversationPrive.ajouterMessage("Quentin", "Pas le choix");

        serveur.getListeConversations().add(conversationGroupe);
        serveur.getListeConversations().add(conversationGroupe1);
        serveur.getListeConversations().add(conversationGroupe2);
        serveur.getListeConversations().add(conversationGroupe3);
        serveur.getListeConversations().add(conversationGroupe4);
        serveur.getListeConversations().add(conversationPrive);

        serveur.getListeNomsUtilisateurs().add("Lucas");
        serveur.getListeNomsUtilisateurs().add("Morice");
        serveur.getListeNomsUtilisateurs().add("Tony");
        serveur.getListeNomsUtilisateurs().add("Tom");
        serveur.getListeNomsUtilisateurs().add("Gérard");
        serveur.getListeNomsUtilisateurs().add("Justine");
        serveur.getListeNomsUtilisateurs().add("Adèle");
        serveur.getListeNomsUtilisateurs().add("Babar");
        serveur.getListeNomsUtilisateurs().add("Zizou");
        serveur.getListeNomsUtilisateurs().add("Maxime");
        serveur.getListeNomsUtilisateurs().add("Nathan");
        serveur.getListeNomsUtilisateurs().add("Quentin");
    }

}

  