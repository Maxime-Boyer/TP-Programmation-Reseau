package stream_multithread;

import beans.Conversation;
import beans.Message;
import beans.Serveur;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;

public class ClientThread extends Thread {

    private Socket clientSocket;
    private Serveur serveur;
    private String nomUtilisateur;
    private EtatsPossibles etat;
    private String nomConversationActuelle;
    private int tailleConversationActuelle;
    private boolean afficherMenu;
    private boolean utilisateurConnecte;
    private ArrayList<Conversation> listeConversationPriveeUtilisateur = new ArrayList<>();
    private HashMap<String, Integer> messagesEnAbsenceConversationsPublic = new HashMap<>();
    private HashMap<String, Integer> messagesEnAbsenceConversationsPrivee = new HashMap<>();

    private BufferedReader socIn;
    private PrintStream socOut;

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
     * @param s: la socket init sur le serveur
     * @param serveur: le serveur sur lequel tourne le chat
     * @param nomUtilisateur: le nom de l'utilisateur auquel appartient le thread
     * @param socIn: le canal d'entree des saisies utilisateur
     * @param socOut: le canal de sortie permettznt d'afficher du texte sur le terminal de l'utilisateur
     */
    ClientThread(Socket s, Serveur serveur, String nomUtilisateur, BufferedReader socIn, PrintStream socOut) {
        this.clientSocket = s;
        this.serveur = serveur;
        this.nomUtilisateur = nomUtilisateur;
        this.socIn = socIn;
        this.socOut = socOut;
        this.nomConversationActuelle = "";
        this.tailleConversationActuelle = 0;
        this.utilisateurConnecte = true;
    }

    /**
     * Lance un nouveau thread qui gèrera le cycle de vie d'une seul client au sein de l'application
     **/
    public void run() {

        try {
            //connection de l'utilisateur
            serveur.connecterUtilisateur(nomUtilisateur);

            etat = EtatsPossibles.MENU_INITIAL;
            afficherMenu = true;

            //message utilisateur connexion reussie
            socOut.println("Vous êtes connecté avec succès");

            String line = " ";
            while (true) {

                if(utilisateurConnecte){
                    if(afficherMenu){
                        if(etat != EtatsPossibles.PARLER_DANS_CONVERSATION){
                            nomConversationActuelle = "";
                            tailleConversationActuelle = 0;
                        }
                        switch (etat){
                            case MENU_INITIAL:
                                afficherMenuInitial();
                                gereMenuInitial();
                                break;
                            case MENU_LISTER_CONVERSATIONS:
                                afficherMenuListerConversations();
                                retourEtatInitial();
                                break;
                            case MENU_LISTER_UTILISATEURS:
                                afficherMenuListerUtilisateurs();
                                retourEtatInitial();
                                break;
                            case MENU_CONVERSATION:
                                afficherMenuConversation();
                                gereMenuConversation();
                                break;
                            case CREER_CONVERSATION_GROUPE:
                                creerConversation();
                                break;
                            case REJOINDRE_CONVERSATION_GROUPE:
                                rejoindreConversation();
                                break;
                            case CONTACTER_UTILISATEUR:
                                rejoindreConversationUtilisateur();
                                break;
                            case PARLER_DANS_CONVERSATION:
                                parlerDansConversation();
                                break;
                        }
                    }
                }

                if(!utilisateurConnecte) {
                    socOut.println(" ");
                    socOut.println("Pour vous reconnecter, veuillez entrer votre identifiant svp: ");
                    line = socIn.readLine();

                    if(line.equals(nomUtilisateur)){
                        if(!serveur.getListeUtilisateurConnectes().contains(nomUtilisateur)){
                            System.out.println(nomUtilisateur+" est connecté au serveur.");
                            serveur.getListeUtilisateurConnectes().add(nomUtilisateur);
                            utilisateurConnecte = true;
                        }
                    }
                }
            }

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
     */
    public void afficherMenuInitial(){
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

    public void recupererConversationsUtilisateur(){
        listeConversationPriveeUtilisateur.clear();
        ArrayList<Conversation> listeConversationPrivee = serveur.getListeConversationsPrivee();
        for(int i = 0; i < listeConversationPrivee.size(); i++){
            if(!listeConversationPrivee.get(i).isConversationGroupe()){
                if((listeConversationPrivee.get(i).getListeParticipants().get(0).equals(nomUtilisateur) || listeConversationPrivee.get(i).getListeParticipants().get(1).equals(nomUtilisateur))){
                    listeConversationPriveeUtilisateur.add(listeConversationPrivee.get(i));
                }
            }
        }
    }

    /**
     * Méthode gérant l'affichage de la liste des conversations
     */
    public void afficherMenuListerConversations(){
        ArrayList<Conversation> listeConversation = serveur.getListeConversationsPublic();
        socOut.println(" ");
        socOut.println("Conversations de groupe:");
        for(int i = 0; i < listeConversation.size(); i++){
            socOut.println(" - " + i+1 + " - " + listeConversation.get(i).getNomConversation());
        }

        String utilisateur = "";
        socOut.println(" ");
        socOut.println("Utilisateurs avec qui vous avez une conversation:");
        for(int i = 0; i < serveur.getListeConversationsPrivee().size(); i++) {
            if((serveur.getListeConversationsPrivee().get(i).getListeParticipants().get(0).equals(nomUtilisateur) || serveur.getListeConversationsPrivee().get(i).getListeParticipants().get(1).equals(nomUtilisateur))) {
                if (serveur.getListeConversationsPrivee().get(i).getListeParticipants().get(0).equals(nomUtilisateur)) {
                    utilisateur = serveur.getListeConversationsPrivee().get(i).getListeParticipants().get(1);
                } else {
                    utilisateur = serveur.getListeConversationsPrivee().get(i).getListeParticipants().get(0);
                }
            }
            socOut.println(" - " + i+1 + " - " + utilisateur);
        }

        afficherMenu = false;
    }

    /**
     * Méthode gérant l'affichage de la liste des utilisateurs
     */
    public void afficherMenuListerUtilisateurs(){
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
     */
    public void afficherMenuConversation(){
        socOut.println(" ");
        socOut.println("Que souhaitez-vous faire ?");
        socOut.println("1 - Créer une conversation de groupe");
        socOut.println("2 - Rejoindre une conversation de groupe");
        socOut.println("3 - Parler à un utilisateur");
        socOut.println("4 - Retourner au menu initial");
        afficherMenu = false;
    }

    public String getNomConversationActuelle() {
        return nomConversationActuelle;
    }

    /**
     * Récupère la saisie utilisateur suite au menu initial et redirige vers l'état demandé par l'utilisateur
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void gereMenuInitial() throws IOException {

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
                    System.out.println(nomUtilisateur+" a quitté le serveur.");
                    socOut.println("Deconnexion réussie.");
                    serveur.getListeUtilisateurConnectes().remove(nomUtilisateur);
                    deconnectionEnregistrementMessages();
                    utilisateurConnecte = false;
                    break;
            }
        }
        afficherMenu = true;
    }



    /**
     * Récupère la saisie utilisateur suite au menu conversation et redirige vers l'état demandé par l'utilisateur
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void gereMenuConversation() throws IOException{

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
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void creerConversation() throws IOException {

        String line = "";
        // tant que l'entree utilisateur ne contient pas un seul caractere lisible, on lui demande une entree
        while(!line.matches(".*[0-9a-zA-Z]+.*")){
            socOut.println(" ");
            socOut.println("Entrez le nom de la conversation que vous souhaitez créer:");
            line = socIn.readLine();
            line = enleverCaracteresSpeciaux(line);
        }

        // rechercher si la conversation existe
        Conversation conversation;
        boolean conversationExiste = false;
        for(int i = 0; i < serveur.getListeConversationsPublic().size(); i++){
            conversation = serveur.getListeConversationsPublic().get(i);

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
            serveur.ajouterConversations(line, true);
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
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void rejoindreConversation() throws IOException {

        String line = "";
        // tant que l'entree utilisateur ne contient pas un seul caractere lisible, on lui demande une entree
        while(!line.matches(".*[0-9a-zA-Z]+.*")){
            socOut.println(" ");
            socOut.println("Entrez le nom de la conversation que vous souhaitez rejoindre:");
            line = socIn.readLine();
        }

        // rechercher si la conversation existe
        Conversation conversation;
        boolean conversationExiste = false;
        for(int i = 0; i < serveur.getListeConversationsPublic().size(); i++){
            conversation = serveur.getListeConversationsPublic().get(i);

            // si une conversation existe avec ce nom, retour état d'avant
            if(conversation.getNomConversation().equals(line)){
                conversationExiste = true;
                nomConversationActuelle = line;
                tailleConversationActuelle = conversation.getListeMessages().size();

                // ajout du client dans cette conversation si il n'existe pas
                boolean utilisateurTrouve = false;
                for(int j = 0; j < conversation.getListeParticipants().size(); j++){
                    if(conversation.getListeParticipants().get(j).equals(nomUtilisateur)){
                        utilisateurTrouve = true;
                    }
                }
                if(!utilisateurTrouve){
                    System.out.println("ajout utilisateur " + nomUtilisateur);
                    conversation.ajouterUtilisateur(nomUtilisateur);
                }

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
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void rejoindreConversationUtilisateur() throws IOException {

        String line = "";
        // tant que l'entree utilisateur ne contient pas un seul caractere lisible, on lui demande une entree
        while(!line.matches(".*[0-9a-zA-Z]+.*")){
            socOut.println(" ");
            socOut.println("Entrez le nom de l'utilisateur à qui vous souhaitez parler:");
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
            for(int i = 0; i < serveur.getListeConversationsPrivee().size(); i++){
                conversation = serveur.getListeConversationsPrivee().get(i);

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
                serveur.ajouterConversations(nomComversation, false);
                serveur.getListeConversationsPrivee().get(serveur.getListeConversationsPrivee().size()-1).ajouterUtilisateur(nomUtilisateur);
                serveur.getListeConversationsPrivee().get(serveur.getListeConversationsPrivee().size()-1).ajouterUtilisateur(line);
                nomConversationActuelle = nomComversation;
                tailleConversationActuelle = 0;
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
     * @throws IOException: jette les exceptions liees aux I/O utilisateur
     */
    public void parlerDansConversation() throws IOException {

        socOut.println(" ");
        socOut.println("---     Conversation " + nomConversationActuelle + "     ---");

        boolean isGroup = true;
        boolean conversationFound = false;
        // determiner la conversation dans laquelle se trouve l'utilisateur
        int indexConversation = 0;
        ArrayList<Conversation> listeConversation = null;
        for (int i = 0; i < serveur.getListeConversationsPrivee().size(); i++) {
            if (serveur.getListeConversationsPrivee().get(i).getNomConversation().equals(nomConversationActuelle)) {
                indexConversation = i;
                listeConversation = serveur.getListeConversationsPrivee();
                isGroup = false;
                conversationFound = true;
                break;
            }
        }
        if(!conversationFound) {
            for (int i = 0; i < serveur.getListeConversationsPublic().size(); i++) {
                if (serveur.getListeConversationsPublic().get(i).getNomConversation().equals(nomConversationActuelle)) {
                    indexConversation = i;
                    listeConversation = serveur.getListeConversationsPublic();
                    isGroup = true;
                    break;
                }
            }
        }

        tailleConversationActuelle = listeConversation.get(indexConversation).getListeMessages().size();

        //ajouter utilisateur si conv groupe
        Conversation conversation = listeConversation.get(indexConversation);
        if (conversation.isConversationGroupe()){
            boolean utilisateurTrouve = false;
            for (int j = 0; j < conversation.getListeParticipants().size(); j++) {
                if (conversation.getListeParticipants().get(j).equals(nomUtilisateur)) {
                    utilisateurTrouve = true;
                }

                if (!utilisateurTrouve) {
                    System.out.println("ajout utilisateur " + nomUtilisateur);
                    conversation.ajouterUtilisateur(nomUtilisateur);
                }
            }
        }

        if(afficherMessageEnAbsence(conversation)) {
            //afficher la conversation
            conversation.afficherNMessages(socOut, 10, nomUtilisateur);
        }

        // afficher les consignes à l'utilisateur
        socOut.println(" ");
        socOut.println("--- Consignes ---------------------------------------------------");
        socOut.println("- Ecrivez votre message puis appuyez sur Entrer pour l'envoyer. -");
        socOut.println("- Ecrivez 'showAll' pour afficher toute la conversation         -");
        socOut.println("- Ecriver 'exit' pour sortir de la conversation                 -");
        socOut.println("----------------------------------------------------------------- ");

        //tant que l'utilisateur est dans la conversation
        String line = "";
        while(true){

            // recuperation de l'entree utilisateur
            line = socIn.readLine();

            // si entree = exit sortir de la conversation et retourner à l'état initial
            if(line.equals("exit")){
                socOut.println(" ");
                socOut.println("---     Sortie de la conversation    ---");
                etat = EtatsPossibles.MENU_INITIAL;
                afficherMenu = true;
                break;
            }

            if(isGroup){
                listeConversation = serveur.getListeConversationsPublic();
            }else {
                listeConversation = serveur.getListeConversationsPrivee();
            }
            // si entree = showAll afficher tout la conversation
            if(line.equals("showAll")){
                socOut.println(" ");
                socOut.println("---     Conversation "+nomConversationActuelle+"     ---");
                tailleConversationActuelle = listeConversation.get(indexConversation).getListeMessages().size();
                listeConversation.get(indexConversation).afficherNMessages(socOut, tailleConversationActuelle, nomUtilisateur);
            }
            // sinon envoyer un message sur la conversation
            else{
                listeConversation.get(indexConversation).ajouterMessage(nomUtilisateur, line);
                refreshConversation();

                //Choix du ou des clients à qui on envoie le message
                for (Map.Entry<String, ClientThread> client : EchoServerMultiThreaded.clientThreads.entrySet()) {
                    for(String participant : listeConversation.get(indexConversation).getListeParticipants()) {
                        if((client.getKey().equals(participant)) && !(client.getKey() == nomUtilisateur) && client.getValue().getNomConversationActuelle().equals(nomConversationActuelle)) {
                            client.getValue().refreshConversation();
                        }
                    }
                }
            }
        }
    }

    /**
     * Affiche les derniers messages non lus de la conversation
     */
    public void refreshConversation(){

        ArrayList<Conversation> listeConversation = null;
        int indexConversation = 0;
        boolean isGroup = false;
        for (int i = 0; i < serveur.getListeConversationsPublic().size(); i++) {
            if (serveur.getListeConversationsPublic().get(i).getNomConversation().equals(nomConversationActuelle)) {
                indexConversation = i;
                isGroup = true;
                listeConversation = serveur.getListeConversationsPublic();
                break;
            }
        }
        for (int i = 0; i < serveur.getListeConversationsPrivee().size(); i++) {
            if (serveur.getListeConversationsPrivee().get(i).getNomConversation().equals(nomConversationActuelle)) {
                indexConversation = i;
                listeConversation = serveur.getListeConversationsPrivee();
                isGroup = false;
                break;
            }
        }

        // affichage des nouveaux messages
        int nouvelleTailleConversationActuelle = 0;
        nouvelleTailleConversationActuelle = listeConversation.get(indexConversation).getListeMessages().size();
        if(nouvelleTailleConversationActuelle > tailleConversationActuelle){
            listeConversation.get(indexConversation).afficherNMessages(socOut, nouvelleTailleConversationActuelle-tailleConversationActuelle, nomUtilisateur);
            tailleConversationActuelle = nouvelleTailleConversationActuelle;
        }
    }

    /**
     * Méthode permettant de remplacer les caractères spéciaux d'une chaine String en caractères non spéciaux
     * @param line: chaîne à transformer
     * @return: chaîne texte transformée
     */
    public static String enleverCaracteresSpeciaux(String line) {
        //On parcours chaque caractère du nom de la conversation et on le remplace par le caractère sans accent...
        StringBuffer resultat = new StringBuffer();
        if(line!=null && line.length()!=0) {
            int index = -1;
            char c = (char)0;
            String chars= "àâäéèêëîïôöùûüç";
            String replace= "aaaeeeeiioouuuc";
            for(int i=0; i<line.length(); i++) {
                c = line.charAt(i);
                if( (index=chars.indexOf(c))!=-1 )
                    resultat.append(replace.charAt(index));
                else
                    resultat.append(c);
            }
        }
        return resultat.toString();
    }

    public void deconnectionEnregistrementMessages(){
        recupererConversationsUtilisateur();
        for(int i = 0; i < listeConversationPriveeUtilisateur.size(); i++){
            Conversation conversation = listeConversationPriveeUtilisateur.get(i);
            int idMessage = conversation.getListeMessages().size() - 1;
            messagesEnAbsenceConversationsPrivee.put(conversation.getNomConversation(), idMessage);
        }
        for(int i = 0; i < serveur.getListeConversationsPublic().size(); i++){
            Conversation conversation = serveur.getListeConversationsPublic().get(i);
            int idMessage = conversation.getListeMessages().size() - 1;
            messagesEnAbsenceConversationsPublic.put(conversation.getNomConversation(), idMessage);
        }
    }

    public boolean afficherMessageEnAbsence(Conversation conversation){
        Message message;
        int debutBoucle = 0;
        boolean listeVide = true;

        if(conversation.getListeMessages().size() > 0){
            if(conversation.isConversationGroupe()) {
                if (messagesEnAbsenceConversationsPublic != null){
                    if (messagesEnAbsenceConversationsPublic.containsKey(conversation.getNomConversation())) {
                        debutBoucle = messagesEnAbsenceConversationsPublic.get(conversation.getNomConversation());
                        messagesEnAbsenceConversationsPublic.remove(conversation.getNomConversation());
                        listeVide = false;
                    }
                }
            }else {
                if (messagesEnAbsenceConversationsPrivee != null) {
                    if (messagesEnAbsenceConversationsPrivee.containsKey(conversation.getNomConversation())) {
                        debutBoucle = messagesEnAbsenceConversationsPrivee.get(conversation.getNomConversation());
                        messagesEnAbsenceConversationsPrivee.remove(conversation.getNomConversation());
                        ;
                        listeVide = false;
                    }
                }
            }
        }

        if(debutBoucle != 0 && !listeVide){

            socOut.println("------ Pendant votre absence : ------");

            for(int i = debutBoucle; i < conversation.getListeMessages().size(); i++){
                message = conversation.getListeMessages().get(i);
                socOut.println(" ");
                String nomAuteur = message.getNomAuteur();
                socOut.println(nomAuteur+" - "+message.getDateEnvoi());
                socOut.println(message.getCorpsMessage());
            }
            socOut.println("-------------------------------------");
        }
        return listeVide;
    }
}

  