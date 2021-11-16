package stream_multithread; /***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

import beans.Conversation;
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
        MENU_CONVERSATION,
        CREER_CONVERSATION_GROUPE,
        REJOINDRE_CONVERSATION_GROUPE,
        PARLER_A_UTILISATEUR
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
                    System.out.println(etat);
                    switch (etat){
                        case MENU_INITIAL:
                            afficherMenuInitial(socOut);
                            line = socIn.readLine();
                            etat = gereMenuInitial(line);
                            break;
                        case MENU_LISTER_CONVERSATIONS:
                            afficherMenuListerConversations(socOut);
                            System.out.println("back in switch");
                            retourEtatInitial();
                            break;
                        case MENU_LISTER_UTILISATEURS:
                            afficherMenuListerUtilisateurs(socOut);
                            retourEtatInitial();
                            break;
                        case MENU_CONVERSATION:
                            afficherMenuConversation(socOut);
                            line = socIn.readLine();
                            etat = gereMenuConversation(line);
                            break;
                        case CREER_CONVERSATION_GROUPE:
                            creerConversation(socOut, socIn);
                            break;
                        case REJOINDRE_CONVERSATION_GROUPE:
                            rejoindreConversation(socOut, socIn);
                            break;
                        case PARLER_A_UTILISATEUR:
                            parlerAUtilisateur(socOut, socIn);
                            break;
                    }
                }
            }
            socOut.println("Deconnexion réussie.");
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    public void retourEtatInitial(){
        etat = EtatsPossibles.MENU_INITIAL;
        afficherMenu = true;
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

    public void afficherMenuListerConversations(PrintStream socOut){
        ArrayList<Conversation> listeConversation = serveur.getListeConversations();
        socOut.println(" ");
        socOut.println("Ordre alphabétique des conversations");
        Collections.sort(listeConversation, Comparator.comparing((Conversation conversation) -> conversation.getNomConversation()));
        int j = 1;
        String utilisateur = "";
        boolean peutAfficherConversation = true;
        for(int i = 0; i < listeConversation.size(); i++){
            peutAfficherConversation = true;
            if(listeConversation.get(i).getListeParticipants().size() == 2){
                if((listeConversation.get(i).getListeParticipants().get(0).equals(nomUtilisateur) || listeConversation.get(i).getListeParticipants().get(1).equals(nomUtilisateur))){
                    if(listeConversation.get(i).getListeParticipants().get(0).equals(nomUtilisateur)){
                        utilisateur = listeConversation.get(i).getListeParticipants().get(1);
                    }
                    else{
                        utilisateur = listeConversation.get(i).getListeParticipants().get(0);
                    }
                    socOut.println(" - " + j + " - Messagerie avec " + utilisateur);
                    j++;
                }
            }else{
                socOut.println(" - " + j + " - " + listeConversation.get(i).getNomConversation());
                j++;
            }
        }
        //socOut.println(FIN_AFFICHAGE);
        afficherMenu = false;
    }

    public void afficherMenuListerUtilisateurs(PrintStream socOut){
        ArrayList<String> listeUtilisateur = serveur.getListeNomsUtilisateurs();
        socOut.println(" ");
        socOut.println("Ordre alphabétique des utilisateurs");
        Collections.sort(listeUtilisateur, Comparator.comparing(String::toLowerCase));
        int j = 1;
        for(int i = 0; i < listeUtilisateur.size(); i++){
            socOut.println(" - " + j + " - " + listeUtilisateur.get(i));
            j++;
        }
        //socOut.println(FIN_AFFICHAGE);
        afficherMenu = false;
    }

    public void afficherMenuConversation(PrintStream socOut){
        socOut.println(" ");
        socOut.println("Que souhaitez-vous faire ?");
        socOut.println("1 - Créer une conversation de groupe");
        socOut.println("2 - Rejoindre une conversation de groupe");
        socOut.println("3 - Parler à un utilisateur");
        socOut.println("4 - Retourner au menu initial");
        socOut.println(FIN_AFFICHAGE);
        afficherMenu = false;
    }

    public void creerConversation(PrintStream socOut, BufferedReader socIn) throws IOException {

        String line = "";
        // tant que l'entree utilisateur ne contient pas un seul caractere lisible, on lui demande une entree
        while(!line.matches(".*[0-9a-zA-Z]+.*")){
            socOut.println(" ");
            socOut.println("Entrez le nom de la conversation que vous souhaitez créer:");
            socOut.println(FIN_AFFICHAGE);
            line = socIn.readLine();
        }

        // rechercher si la conversation existe
        Conversation conversation;
        boolean conversationExiste = false;
        for(int i = 0; i < serveur.getListeConversations().size(); i++){
            conversation = serveur.getListeConversations().get(i);

            // si une conversation existe avec ce nom, retour état d'avant
            if(conversation.getNomConversation().equals(line)){
                conversationExiste = true;
                socOut.println("La conversation '"+line+"' existe deja. Rejoignez là ou créez en une autre.");
                etat = EtatsPossibles.MENU_CONVERSATION;
                break;
            }
        }

        if(!conversationExiste){
            // si elle n'existe pas, on la crée
            serveur.ajouterConversations(line);
            socOut.println("Conversation '"+line+"' créée.");
            etat = EtatsPossibles.MENU_INITIAL;     //TODO: raccorder à l'envoie d'un message
        }

        afficherMenu = true;
    }

    public void rejoindreConversation(PrintStream socOut, BufferedReader socIn) throws IOException {
        socOut.println("Entrez le nom de la conversation que vous souhaitez rejoindre:");
        socOut.println(FIN_AFFICHAGE);
        String line = socIn.readLine();
        System.out.println("Rejoindre conversation: "+line);
        etat = EtatsPossibles.MENU_INITIAL;
        afficherMenu = true;
    }

    public void parlerAUtilisateur(PrintStream socOut, BufferedReader socIn) throws IOException {
        socOut.println("Entrez le nom de l'utilisateur à qui vous souhaitez parler");
        socOut.println(FIN_AFFICHAGE);
        String line = socIn.readLine();
        String utilsateurCherche = line;
        System.out.println("Contacter utilisateur: "+utilsateurCherche);
        socOut.println("Entrez votre message à envoyer à " + utilsateurCherche);
        socOut.println(FIN_AFFICHAGE);
        line = socIn.readLine();
        String message = line;
        System.out.println("Message envoyé");
        //socOut.println(FIN_AFFICHAGE);

        boolean conversationTrouve = false;
        Conversation conversation = null;
        //TODO faire remonter le message au server
        for(int i = 0; i < serveur.getListeConversations().size(); i++){
            conversation = serveur.getListeConversations().get(i);
            if(conversation.getListeParticipants().size() == 2){
                if((conversation.getListeParticipants().get(0).equals(utilsateurCherche) && conversation.getListeParticipants().get(1).equals(nomUtilisateur))
                || (conversation.getListeParticipants().get(1).equals(utilsateurCherche) && conversation.getListeParticipants().get(0).equals(nomUtilisateur))
                ){
                    conversation.ajouterMessage(nomUtilisateur, message);
                    System.out.println("*** " + message + " *** envoyé par " + nomUtilisateur + " à " + utilsateurCherche);
                    conversationTrouve = true;
                }
            }
        }
        if(!conversationTrouve){
            conversation = new Conversation(nomUtilisateur, utilsateurCherche);
            conversation.ajouterMessage(nomUtilisateur, message);
            serveur.getListeConversations().add(conversation);
            System.out.println("*** " + message + " *** envoyé par " + nomUtilisateur + " à " + utilsateurCherche);
        }
        conversation.afficherMessages();
        etat = EtatsPossibles.MENU_INITIAL;
        afficherMenu = true;
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

    public EtatsPossibles gereMenuConversation(String line){
        EtatsPossibles etat = EtatsPossibles.MENU_INITIAL;
        if(!(line.length() > 1 || line.charAt(0) <= '0' || line.charAt(0) > '4')) {
            int choix = Integer.parseInt(line);
            switch (choix) {
                case 1:
                    etat = EtatsPossibles.CREER_CONVERSATION_GROUPE;
                    break;
                case 2:
                    etat = EtatsPossibles.REJOINDRE_CONVERSATION_GROUPE;
                    break;
                case 3:
                    etat = EtatsPossibles.PARLER_A_UTILISATEUR;
                    break;
                case 4:
                    etat = EtatsPossibles.MENU_INITIAL;
                    break;
            }
        }
        afficherMenu = true;
        return etat;
    }
}

  