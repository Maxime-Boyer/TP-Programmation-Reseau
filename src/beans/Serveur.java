package beans;

import stream_multithread.ClientThread;
import stream_multithread.EchoServerMultiThreaded;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Serveur {

    private ArrayList<String> listeNomsUtilisateurs = new ArrayList<>();
    private ArrayList<Conversation> listeConversations = new ArrayList<>();

    /**
     * Constructeur de Serveur initialisé au lancement du ServerMultiThreaded
     */
    public Serveur() {
    }

    /**
     * Getter de la liste des noms d'utilisateurs
     */
    public ArrayList<String> getListeNomsUtilisateurs() {
        return listeNomsUtilisateurs;
    }

    /**
     * Getter de la liste des noms de conversations
     */
    public ArrayList<Conversation> getListeConversations() {
        return listeConversations;
    }

    /**
     * Méthode permettant d'ajouter une conversation à la liste des conversations
     * @param nomConversation
     */
    public void ajouterConversations(String nomConversation){
        Conversation conversation = new Conversation(nomConversation);
        listeConversations.add(conversation);
    }

    //TODO : ajouter la notion d'utilisateur connecté ou non pour afficher que les utilisateurs : tous et aussi que les connecter
    /**
     * Méthode permettant d'ajouter un utilisateur à la liste des utilisateurs
     * @param nomUtilisateur
     */
    public void connecterUtilisateur(String nomUtilisateur){
        System.out.println("\nConnexion de l'utilisateur: " + nomUtilisateur);
        if (!listeNomsUtilisateurs.contains(nomUtilisateur)) {
            listeNomsUtilisateurs.add(nomUtilisateur);
        }
    }

    /**
     * Méthode d'affichage de la liste des utilisateurs
     * @return
     */
    @Override
    public String toString() {
        return "Serveur{" +
                "listeNomsUtilisateurs=" + listeNomsUtilisateurs +
                '}';
    }
}
