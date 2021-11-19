package stream_multithread; /***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

import beans.Conversation;
import beans.Serveur;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ClientThread extends Thread {

    private Socket clientSocket;
    private Serveur serveur;
    private String nomUtilisateur;
    private EtatsPossibles etat;
    private String nomConversationActuelle;
    private int tailleConversationActuelle;
    private boolean afficherMenu;

    //Nathan
    private BufferedReader socIn;
    private PrintStream socOut;

    // hash pour eviter les effets de bord (saisie utilisateur meme code par inadvertance)
    public static String FIN_AFFICHAGE = "b5d3a7a3a9290e714f2b06ec21cf5af613a7eff7e1674269b3940848afb1bdf6";
    public static String DECONNEXION = "1a6d88ecd34873cbc5a9cbc0dbc1d01ce9fe24af75f835714160b9d5735da9a5";

    private enum EtatsPossibles {
        MENU_INITIAL,
        MENU_LISTER_CONVERSATIONS,
        MENU_LISTER_UTILISATEURS,
        MENU_CONVERSATION,
        CREER_CONVERSATION_GROUPE,
        REJOINDRE_CONVERSATION_GROUPE,
        CONTACTER_UTILISATEUR,
        PARLER_DANS_CONVERSATION
    };

    /**
     *
     * @param s: le socket créé par le ServerMultiThreaded affecté à un client
     * @param serveur
     */
    ClientThread(Socket s, Serveur serveur) {
        this.clientSocket = s;
        this.serveur = serveur;
        nomConversationActuelle = "";
        tailleConversationActuelle = 0;
    }

    //Nathan
    ClientThread(Socket s, Serveur serveur, String nomUtilisateur, BufferedReader socIn, PrintStream socOut) {
        this.clientSocket = s;
        this.serveur = serveur;
        this.nomUtilisateur = nomUtilisateur;
        this.socIn = socIn;
        this.socOut = socOut;
    }

    ClientThread(Socket s, Serveur serveur, BufferedReader socIn, PrintStream socOut) {
        this.clientSocket = s;
        this.serveur = serveur;
        this.socIn = socIn;
        this.socOut = socOut;
    }

    /**
     * Lance un nouveau thread qui gèrera le cycle de vie d'une seul client au sein de l'application
     **/
    public void run() {

        try {

            // initialisation des canaux de communication
            /*
            BufferedReader socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            */
            //String line = socIn.readLine();

            //connection de l'utilisateur
            //nomUtilisateur = line;
            serveur.connecterUtilisateur(nomUtilisateur);
            System.out.println("\n"+serveur);
            etat = EtatsPossibles.MENU_INITIAL;
            afficherMenu = true;

            //message utilisateur connexion reussie
            socOut.println("Vous êtes connecté avec succès");

            String line = " ";
            while (line != null) {
                if(afficherMenu){
                    switch (etat){
                        case MENU_INITIAL:
                            afficherMenuInitial(socOut);
                            gereMenuInitial(socOut, socIn);
                            break;
                        case MENU_LISTER_CONVERSATIONS:
                            afficherMenuListerConversations(socOut);
                            retourEtatInitial();
                            break;
                        case MENU_LISTER_UTILISATEURS:
                            afficherMenuListerUtilisateurs(socOut);
                            retourEtatInitial();
                            break;
                        case MENU_CONVERSATION:
                            afficherMenuConversation(socOut);
                            gereMenuConversation(socIn);
                            break;
                        case CREER_CONVERSATION_GROUPE:
                            creerConversation(socOut, socIn);
                            break;
                        case REJOINDRE_CONVERSATION_GROUPE:
                            rejoindreConversation(socOut, socIn);
                            break;
                        case CONTACTER_UTILISATEUR:
                            rejoindreConversationUtilisateur(socOut, socIn);
                            break;
                        case PARLER_DANS_CONVERSATION:
                            parlerDansConversation(socOut, socIn);
                            break;
                    }
                }
            }
            socOut.println("Deconnexion réussie.");
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    /**
     * Méthode permettant d'afficher le menu de choix d'actions à faire
     */
    public void retourEtatInitial(){
        etat = EtatsPossibles.MENU_INITIAL;
        afficherMenu = true;
    }

    /**
     * affiche le menu initial, c'est à dire le menu que voit l'utilisateur lorsqu'il est connecté mais qu'il
     * n'a commencé aucune action
     * @param socOut: le canal de communication sortant qui permet de parler au client
     */
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

    /**
     * Méthode gérant l'affichage de la liste des conversations
     * @param socOut: permet de transmettre des informations au client
     */
    public void afficherMenuListerConversations(PrintStream socOut){
        ArrayList<Conversation> listeConversation = serveur.getListeConversations();
        socOut.println(" ");
        socOut.println("Conversations de groupe:");
        Collections.sort(listeConversation, Comparator.comparing((Conversation conversation) -> conversation.getNomConversation()));
        int j = 1;
        for(int i = 0; i < listeConversation.size(); i++){
            if(listeConversation.get(i).isConversationGroupe()){
                socOut.println(" - " + j + " - " + listeConversation.get(i).getNomConversation());
                j++;
            }
        }

        j = 1;
        String utilisateur = "";
        socOut.println(" ");
        socOut.println("Utilisateurs avec qui vous avez une conversation:");
        for(int i = 0; i < listeConversation.size(); i++){
            if(!listeConversation.get(i).isConversationGroupe()){
                if((listeConversation.get(i).getListeParticipants().get(0).equals(nomUtilisateur) || listeConversation.get(i).getListeParticipants().get(1).equals(nomUtilisateur))){
                    if(listeConversation.get(i).getListeParticipants().get(0).equals(nomUtilisateur)){
                        utilisateur = listeConversation.get(i).getListeParticipants().get(1);
                    }
                    else{
                        utilisateur = listeConversation.get(i).getListeParticipants().get(0);
                    }
                    socOut.println(" - " + j + " - " + utilisateur);
                    j++;
                }
            }
        }

        afficherMenu = false;
    }

    /**
     * Méthode gérant l'affichage de la liste des utilisateurs
     * @param socOut: permet de transmettre des informations au client
     */
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

    /**
     * Affiche le menu permettant de communiquer sur le forum
     * @param socOut: le canal de communication sortant permettant de communiquer avec le client
     */
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

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    /**
     * Récupère la saisie utilisateur suite au menu initial et redirige vers l'état demandé par l'utilisateur
     * @param socIn: le canal de communication entrant permettant de recuperer les saisies client
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void gereMenuInitial(PrintStream socOut, BufferedReader socIn) throws IOException {

        // si entree non prise en charge, affiche à nouveau le meme menu
        String line = socIn.readLine();

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
                    socOut.println(DECONNEXION);
                    break;
            }
        }
        afficherMenu = true;
    }

    /**
     * Récupère la saisie utilisateur suite au menu conversation et redirige vers l'état demandé par l'utilisateur
     * @param socIn: le canal de communication entrant permettant de recuperer les saisies client
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void gereMenuConversation(BufferedReader socIn) throws IOException{

        // si entree non prise en charge, affiche à nouveau le meme menu
        String line = socIn.readLine();

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
                    etat = EtatsPossibles.CONTACTER_UTILISATEUR;
                    break;
                case 4:
                    etat = EtatsPossibles.MENU_INITIAL;
                    break;
            }
        }
        afficherMenu = true;
    }

    /**
     * Methode permettant de creer une conversation
     * Protections:
     *      si la conversation existe deja, message avertissement et envoie utilisateur vers le menu précédent
     *      si le nom entré ne possede pas un seul caractere ou chiffre, on lui redemande de saisir le nom
     * @param socOut: le canal de communication sortant permettant de communiquer avec le client
     * @param socIn: le canal de communication entrant permettant de recuperer les saisies client
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
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
            nomConversationActuelle = line;
            tailleConversationActuelle = 0;
            etat = EtatsPossibles.PARLER_DANS_CONVERSATION;
        }

        afficherMenu = true;
    }

    /**
     * Methode permettant de rejoindre une conversation
     * Protections:
     *      si la conversation n'existe pas, message avertissement et envoie utilisateur vers le menu précédent
     *      si le nom entré ne possede pas un seul caractere ou chiffre, on lui redemande de saisir le nom
     * @param socOut: le canal de communication sortant permettant de communiquer avec le client
     * @param socIn: le canal de communication entrant permettant de recuperer les saisies client
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void rejoindreConversation(PrintStream socOut, BufferedReader socIn) throws IOException {

        String line = "";
        // tant que l'entree utilisateur ne contient pas un seul caractere lisible, on lui demande une entree
        while(!line.matches(".*[0-9a-zA-Z]+.*")){
            socOut.println(" ");
            socOut.println("Entrez le nom de la conversation que vous souhaitez rejoindre:");
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
                nomConversationActuelle = line;
                tailleConversationActuelle = conversation.getListeMessages().size();

                // ajout du client dans cette conversation
                conversation.ajouterUtilisateur(nomUtilisateur);

                socOut.println("Connexion à la conversation '"+line+"'...");
                etat = EtatsPossibles.PARLER_DANS_CONVERSATION;
                break;
            }
        }

        if(!conversationExiste){
            // si elle n'existe pas, on avertit l'utilisateur
            socOut.println("La conversation '"+line+"' n'existe pas. Créez là ou rejoignez en une autre.");
            etat = EtatsPossibles.MENU_CONVERSATION;
        }

        afficherMenu = true;
    }

    /**
     * Methode permettant de contacter un utilisateur dans une conversation privee
     * Protections:
     *      Si l'utilisateur n'existe pas, avertissement et retour au menu précédent
     *      si le nom entré ne possede pas un seul caractere ou chiffre, on lui redemande de saisir le nom
     *      si la conversation n'existe pas, elle est créée et l'utilisateur est averti
     * @param socOut: le canal de communication sortant permettant de communiquer avec le client
     * @param socIn: le canal de communication entrant permettant de recuperer les saisies client
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void rejoindreConversationUtilisateur(PrintStream socOut, BufferedReader socIn) throws IOException {

        String line = "";
        // tant que l'entree utilisateur ne contient pas un seul caractere lisible, on lui demande une entree
        while(!line.matches(".*[0-9a-zA-Z]+.*")){
            socOut.println(" ");
            socOut.println("Entrez le nom de l'utilisateur à qui vous souhaitez parler:");
            socOut.println(FIN_AFFICHAGE);
            line = socIn.readLine();
        }

        //recherche si l'utilisateur existe
        boolean utilisateurExiste = false;
        for(int i = 0; i < serveur.getListeNomsUtilisateurs().size(); i++) {

            if(serveur.getListeNomsUtilisateurs().get(i).equals(line)){
                utilisateurExiste = true;
                break;
            }
        }

        if(utilisateurExiste){
            // rechercher si la conversation existe
            Conversation conversation;
            boolean conversationExiste = false;
            String nomComversation = Conversation.determinerNomConversationAvec2NomsUtilisateurs(line, nomUtilisateur);
            for(int i = 0; i < serveur.getListeConversations().size(); i++){
                conversation = serveur.getListeConversations().get(i);

                // si une conversation existe avec ce nom, retour état d'avant
                if(conversation.getNomConversation().equals(nomComversation)){
                    conversationExiste = true;
                    nomConversationActuelle = nomComversation;
                    tailleConversationActuelle = conversation.getListeMessages().size();

                    // ajout du client dans cette conversation
                    conversation.ajouterUtilisateur(nomUtilisateur);

                    socOut.println("Connexion à la conversation '"+nomComversation+"'...");
                    etat = EtatsPossibles.PARLER_DANS_CONVERSATION;
                    break;
                }
            }

            if(!conversationExiste){
                // si elle n'existe pas, on crée la conversation
                serveur.ajouterConversations(nomComversation);
                socOut.println("Création de la conversation avec '"+line+"'. Vous pouvez désormais communiquer avec "+line+".");
                etat = EtatsPossibles.PARLER_DANS_CONVERSATION;
            }
        }
        else{
            socOut.println("L'utilisateur '"+line+"' n'existe pas.");
            etat = EtatsPossibles.MENU_CONVERSATION;
        }

        afficherMenu = true;
    }

    /**
     * Methode permettant à un utilisateur de parler sur la conversation
     * @param socOut: le canal de communication sortant permettant de communiquer avec le client
     * @param socIn: le canal de communication entrant permettant de recuperer les saisies client
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void parlerDansConversation(PrintStream socOut, BufferedReader socIn) throws IOException{

        // TODO Synchro

        socOut.println(" ");
        socOut.println("---     Conversation "+nomConversationActuelle+"     ---");

        // determiner la conversation dans laquelle se trouve l'utilisateur
        int indexConversation = 0;
        for(int i = 0; i < serveur.getListeConversations().size(); i++){
            if(serveur.getListeConversations().get(i).getNomConversation().equals(nomConversationActuelle)){
                indexConversation = i;
                break;
            }
        }

        //afficher la conversaition
        serveur.getListeConversations().get(indexConversation).afficherNMessages(socOut, 10);

        // afficher les consignes à l'utilisateur
        socOut.println(" ");
        socOut.println("--- Consignes ---------------------------------------------------");
        socOut.println("- Ecrivez votre message puis appuyez sur Entrer pour l'envoyer. -");
        socOut.println("- Ecrivez 'showAll' pour afficher toute la conversation         -");
        socOut.println("- Ecriver 'exit' pour sortir de la conversation                 -");
        socOut.println("----------------------------------------------------------------- ");

        //tant que l'utilisateur est dans la conversation
        String line = "";
        int nouvelleTailleConversationActuelle = 0;
        while(true){

            // affichage des nouveaux messages
            nouvelleTailleConversationActuelle = serveur.getListeConversations().get(indexConversation).getListeMessages().size();
            if(nouvelleTailleConversationActuelle > tailleConversationActuelle){
                serveur.getListeConversations().get(indexConversation).afficherNMessages(socOut, nouvelleTailleConversationActuelle-tailleConversationActuelle);
                tailleConversationActuelle = nouvelleTailleConversationActuelle;
            }

            // recuperation de l'entree utilisateur
            socOut.println(FIN_AFFICHAGE);
            line = socIn.readLine();

            // si entree = exit sortir de la conversation et retourner à l'état initial
            if(line.equals("exit")){
                socOut.println(" ");
                socOut.println("---     Sortie de la conversation    ---");
                etat = EtatsPossibles.MENU_INITIAL;
                afficherMenu = true;
                break;
            }

            // si entree = showAll afficher tout la conversation
            if(line.equals("showAll")){
                socOut.println(" ");
                socOut.println("---     Conversation "+nomConversationActuelle+"     ---");
                tailleConversationActuelle = serveur.getListeConversations().get(indexConversation).getListeMessages().size();
                serveur.getListeConversations().get(indexConversation).afficherNMessages(socOut, tailleConversationActuelle);
            }
            // sinon envoyer un message sur la conversation
            else{
                serveur.getListeConversations().get(indexConversation).ajouterMessage(nomUtilisateur, line);

                //Choix du ou des clients à qui on envoie le message
                for (Map.Entry<String, ClientThread> client : EchoServerMultiThreaded.clientThreads.entrySet()) {
                    for(String participant : serveur.getListeConversations().get(indexConversation).getListeParticipants()) {
                        if((client.getKey().equals(participant)) && !(client.getKey() == nomUtilisateur)) {
                            client.getValue().getSocout().println(nomUtilisateur + ":" + line);
                        }
                    }
                }
            }
        }
    }
    private PrintStream getSocout() {return this.socOut; }
    private String getnomUtilisateur() {return this.nomUtilisateur; }
}

  