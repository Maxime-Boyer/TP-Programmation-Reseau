package stream_multithread; /***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

import beans.Serveur;
import beans.Utilisateur;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClientThread extends Thread {

    private Socket clientSocket;
    private Serveur serveur;
    private String nomUtilisateur;
    private EtatsPossibles etat;
    private boolean afficherMenu;

    public static String FIN_AFFICHAGE = "fin affichage";

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
                            line = socIn.readLine();
                            etat = gereMenuInitial(line);
                            System.out.println("etat : " + etat);
                            break;
                        case MENU_LISTER_CONVERSATIONS:
                            afficherMenuListerConversations();
                            break;
                        case MENU_LISTER_UTILISATEURS:
                            afficherMenuListerUtilisateurs(socOut);
                            break;
                        case MENU_CONVERSATION:
                            afficherMenuConversation();
                            break;
                    }
                }

                // attente d'une entree


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
        socOut.println(FIN_AFFICHAGE);
        afficherMenu = false;
    }

    public void afficherMenuListerConversations(){

        System.out.println("test1");
        afficherMenu = false;
    }

    public void afficherMenuListerUtilisateurs(PrintStream socOut){
        ArrayList<String> listeUtilisateur = serveur.getListeNomsUtilisateurs();
        ArrayList<String> listeUtilisateur2 = listeUtilisateur;
        socOut.println(" ");
        socOut.println("Ordre alphabétique des utilisateurs");
        Collections.sort(listeUtilisateur, Comparator.comparing(String::toLowerCase));
        for(int i = 0; i < listeUtilisateur.size(); i++){
            socOut.println(" - " + i + " - " + listeUtilisateur.get(i));
        }
        socOut.println(FIN_AFFICHAGE);
        afficherMenu = false;
    }

    public void afficherMenuConversation(){
        System.out.println("test3");
        afficherMenu = false;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public EtatsPossibles gereMenuInitial(String line){
        EtatsPossibles etat = EtatsPossibles.MENU_INITIAL;
        if(!(line.length() > 1 || line.charAt(0) <= '0' || line.charAt(0) > '4')) {
            int choix = Integer.parseInt(line);
            switch (choix) {
                case 1:
                    etat = EtatsPossibles.MENU_LISTER_CONVERSATIONS;
                    break;
                case 2:
                    etat = EtatsPossibles.MENU_LISTER_UTILISATEURS;
                    break;
                case 3:
                    etat = EtatsPossibles.MENU_CONVERSATION;
                    break;
                case 4:
                    //TODO : à faire
                    break;
            }
        }
        afficherMenu = true;
        return etat;
    }
}

  