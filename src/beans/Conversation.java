package beans;

import persistence.XMLModifier;

import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;

public class Conversation {

    private String nomConversation;
    private ArrayList<Message> listeMessages;
    private ArrayList<String> listeParticipants;
    private XMLModifier xmlModifier = new XMLModifier();
    private boolean conversationGroupe;

    /**
     * Crée une conversation de groupe entre 0 ou plusieurs utilisateurs. L'objet contient
     * la liste des messages envoyés sur la conversation et la liste des auteurs
     * @param nomConversation: le nom de la conversation pour pouvoir l'identifier sur le serveur
     */
    public Conversation(String nomConversation) {
        this.nomConversation = nomConversation;
        this.listeMessages = new ArrayList<Message>();
        this.listeParticipants = new ArrayList<String>();
        this.conversationGroupe = true;
    }

    /**
     * determine la nom d'une conversation grace à 2 noms selon l'ordre lexicographique
     * @param nom1: le nom d'un utilisateur
     * @param nom2: le nom d'un autre utilisateur
     *            l'ordre de nom1 en nom2 n'importe pas
     * @return: le nom de la conversation par ordre lexicographique
     */
    public static String determinerNomConversationAvec2NomsUtilisateurs(String nom1, String nom2){
        String nomConversation = "";
        if(nom1.compareTo(nom2) < 0){
            nomConversation = nom1 + "_" + nom2;
        }
        else{
            nomConversation = nom2 + "_" + nom1;
        }
        return nomConversation;
    }

    public String getNomConversation() {
        return nomConversation;
    }

    public ArrayList<String> getListeParticipants() {
        return listeParticipants;
    }

    public ArrayList<Message> getListeMessages() {
        return listeMessages;
    }

    public boolean isConversationGroupe() {
        return conversationGroupe;
    }

    public void setConversationGroupe(boolean conversationGroupe) {
        this.conversationGroupe = conversationGroupe;
    }

    /**
     * affiche la liste des utilisateurs participant à la conversation
     */
    public void afficherListeParticipants() {
        System.out.println("\nUtilisateurs participant à la conversation: ");
        for(int i = 0; i < listeParticipants.size(); i++){
            System.out.println( (i+1) + " - " + listeParticipants.get(i) );
        }
    }

    /**
     * Ajoute un utilisateur en tant que participant à la conversation
     * @param nomUtilisateur: le nom de l'utilisateur à ajouter comme participant à la conversation
     */
    public void ajouterUtilisateur(String nomUtilisateur){
        boolean utilisateurTrouve = false;
        for(int i = 0; i < listeParticipants.size(); i++){
            if(listeParticipants.get(i).equals(nomUtilisateur)){
                utilisateurTrouve = true;
                break;
            }
        }
        if(!utilisateurTrouve) {
            listeParticipants.add(nomUtilisateur);
            xmlModifier.stockerNouveauParticipant(this, nomUtilisateur, conversationGroupe);
        }
    }

    /**
     * ajoute un message à la conversation
     * @param nomUtilisateur: l'auteur du message
     * @param corpsMessage: le message écris par l'auteur
     */
    public void ajouterMessage(String nomUtilisateur, String corpsMessage){
        Message message = new Message(nomUtilisateur, corpsMessage);
        listeMessages.add(message);
        xmlModifier.stockerMessage(this, message, conversationGroupe);
    }

    /**
     * ajoute un message à la conversation
     * @param nomUtilisateur: l'auteur du message
     * @param corpsMessage: le message écris par l'auteur
     * @param dateEnvoi: date à laquelle a été envoyé le message
     */
    public void ajouterMessage(String nomUtilisateur, String corpsMessage, String dateEnvoi){
        Message message = null;
        try {
            message = new Message(nomUtilisateur, corpsMessage, dateEnvoi);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        listeMessages.add(message);
        xmlModifier.stockerMessage(this, message, conversationGroupe);
    }

    /**
     * Liste les N derniers messages de la conversation et les affiche
     */
    public void afficherNMessages(PrintStream socOut, int n, String nomUtilisateur){

        Message message;
        int debutBoucle = 0;

        if(listeMessages.size() - n > 0){
            debutBoucle = listeMessages.size() - n;
        }

        for(int i = debutBoucle; i < listeMessages.size(); i++){
            message = listeMessages.get(i);
            socOut.println(" ");
            String nomAuteur = "";
            if(message.getNomAuteur().equals(nomUtilisateur)){
                nomAuteur = "Vous";
            }else {
                nomAuteur = message.getNomAuteur();
            }
            socOut.println(nomAuteur+" - "+message.getDateEnvoi());
            socOut.println(message.getCorpsMessage());
        }
    }
}
