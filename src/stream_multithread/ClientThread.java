package stream_multithread; /***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

import beans.Serveur;

import java.io.*;
import java.net.*;
import java.sql.SQLOutput;

public class ClientThread extends Thread {

    private Socket clientSocket;
    private Serveur serveur;
    private String nomUtilisateur;
    private EtatsPossibles etat;
    private boolean afficherMenu;

    private enum EtatsPossibles {
        MENU_INITIAL,
        MENU_LISTER_CONVERSATIONS,
        MENU_LISTER_UTILISATEURS,
        MENU_CONVERSATION
    };

    /**
     * TODO javadoc Clienthread
     * @param s
     * @param serveur
     */
    ClientThread(Socket s, Serveur serveur) {
        this.clientSocket = s;
        this.serveur = serveur;
    }

    /**
     * TODO traduction
     * receives a request from client then sends an echo to the client
     * @param clientSocket the client socket
     **/
    public void run() {

        try {

            // initialisation des canaux de communication
            BufferedReader socIn = null;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            String line = socIn.readLine();

            // connection de l'utilisateur
            nomUtilisateur = line;
            serveur.connecterUtilisateur(nomUtilisateur);
            System.out.println("\n"+serveur);
            etat = EtatsPossibles.MENU_INITIAL;
            afficherMenu = true;

            //message utilisateur connexion reussie
            socOut.println("Vous êtes connecté avec succès");

            while (line != null) {
                if(afficherMenu){
                    switch (etat){
                        case MENU_INITIAL:
                            afficherMenuInitial(socOut);
                            break;
                        case MENU_LISTER_CONVERSATIONS:
                            afficherMenuListerConversations();
                            break;
                        case MENU_LISTER_UTILISATEURS:
                            afficherMenuListerUtilisateurs();
                            break;
                        case MENU_CONVERSATION:
                            afficherMenuConversation();
                            break;
                    }
                }

                // attente d'une entree
                System.out.println(line);

                // traitement de l'entree
                /*if(line == "1") {
                    etat = EtatsPossibles.MENU_LISTER_CONVERSATIONS;
                }*/

            }
            socOut.println("Deconnexion réussie.");
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    public void afficherMenuInitial(PrintStream socOut){
        socOut.println(" ");
        socOut.println("Menu initial:");
        socOut.println("1 - lister les conversations");
        socOut.println("2 - lister les contacts");
        socOut.println("3 - entrer dans une conversation");
        socOut.println("4 - deconnexion");
        socOut.println(" ");
        socOut.println("Entrez le numéro correspondant à l'action que vous souhaiter réaliser");

        afficherMenu = false;
    }

    public void afficherMenuListerConversations(){
        System.out.println("test1");
    }

    public void afficherMenuListerUtilisateurs(){
        System.out.println("test2");
    }

    public void afficherMenuConversation(){
        System.out.println("test3");
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
}

  