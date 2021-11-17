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
        conversationGroupe1.ajouterUtilisateur("Quentin");
        conversationGroupe1.ajouterUtilisateur("Maxime");
        conversationGroupe1.ajouterUtilisateur("Nathan");
        Conversation conversationGroupe2 = new Conversation("Diner à Paris");
        conversationGroupe2.ajouterUtilisateur("Quentin");
        conversationGroupe2.ajouterUtilisateur("Maxime");
        conversationGroupe2.ajouterUtilisateur("Nathan");
        Conversation conversationGroupe3 = new Conversation("Aprem Surf");
        conversationGroupe3.ajouterUtilisateur("Quentin");
        conversationGroupe3.ajouterUtilisateur("Maxime");
        conversationGroupe3.ajouterUtilisateur("Nathan");
        Conversation conversationGroupe4 = new Conversation("PLD Agile");
        conversationGroupe4.ajouterUtilisateur("Quentin");
        conversationGroupe4.ajouterUtilisateur("Maxime");
        conversationGroupe4.ajouterUtilisateur("Nathan");
        conversationGroupe4.ajouterMessage("Quentin", "Hello, ca va?");
        conversationGroupe4.ajouterMessage("Maxime", "Oui");
        conversationGroupe4.ajouterMessage("Nathan", "Non");
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

  