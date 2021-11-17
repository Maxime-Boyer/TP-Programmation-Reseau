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
     * TODO Maxime: javadoc
     */
    public Serveur() {
    }

    /**
     * TODO Maxime: javadoc
     */
    public ArrayList<String> getListeNomsUtilisateurs() {
        return listeNomsUtilisateurs;
    }

    /**
     * TODO Maxime: javadoc
     */
    public ArrayList<Conversation> getListeConversations() {
        return listeConversations;
    }

    /**
     * TODO Maxime: javadoc
     * @param nomConversation
     */
    public void ajouterConversations(String nomConversation){
        Conversation conversation = new Conversation(nomConversation);
        listeConversations.add(conversation);
    }

    /**
     * TODO Maxime: javadoc
     * @param nomUtilisateur
     */
    public void connecterUtilisateur(String nomUtilisateur){
        System.out.println("\nConnexion de l'utilisateur: " + nomUtilisateur);
        if (!listeNomsUtilisateurs.contains(nomUtilisateur)) {
            listeNomsUtilisateurs.add(nomUtilisateur);
        }
    }

    /**
     * TODO Maxime: javadoc
     * @return
     */
    @Override
    public String toString() {
        return "Serveur{" +
                "listeNomsUtilisateurs=" + listeNomsUtilisateurs +
                '}';
    }
}
