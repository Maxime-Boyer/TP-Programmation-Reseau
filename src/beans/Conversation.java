package beans;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Conversation {

    private String nomConversation;
    private ArrayList<Message> listeMessages;
    private ArrayList<String> listeParticipants;

    /**
     * Crée une conversation entre 1 ou plusieurs utilisateurs. L'objet contient
     * la liste des messages envoyés sur la conversation et la liste des auteurs
     * @param nomConversation: le nom de la conversation pour pouvoir l'identifier sur le serveur
     */
    public Conversation(String nomConversation) {
        this.nomConversation = nomConversation;
        this.listeMessages = new ArrayList<Message>();
        this.listeParticipants = new ArrayList<String>();
    }

    public String getNomConversation() {
        return nomConversation;
    }

    // affiche la liste des utilisateurs participant à la conversation
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
        listeParticipants.add(nomUtilisateur);
    }

    /**
     * ajoute un message à la conversation
     * @param nomUtilisateur: l'auteur du message
     * @param corpsMessage: le message écris par l'auteur
     */
    public void ajouterMessage(String nomUtilisateur, String corpsMessage){
        Message message = new Message(nomUtilisateur, corpsMessage);
        listeMessages.add(message);
    }

    /**
     * Liste l'ensemble des messages de la conversation et les affiche
     */
    public void afficherMessages(){
        System.out.println("\n---     Conversation "+nomConversation+"     ---");
        Message message;
        for(int i = 0; i < listeMessages.size(); i++){
            message = listeMessages.get(i);
            System.out.println("\n"+message.getNomAuteur()+" - "+message.getDateEnvoi());
            System.out.println(message.getCorpsMessage());
        }
    }
}
