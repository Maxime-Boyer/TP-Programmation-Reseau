package beans;

import stream_multithread.ClientThread;
import stream_multithread.EchoServerMultiThreaded;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Serveur {

    private ArrayList<String> listeNomsUtilisateurs = new ArrayList<>();
    private ArrayList<Conversation> listeConversations = new ArrayList<>();

    public Serveur() {
    }

    public ArrayList<String> getListeNomsUtilisateurs() {
        return listeNomsUtilisateurs;
    }

    public ArrayList<Conversation> getListeConversations() {
        return listeConversations;
    }

    public void connecterUtilisateur(String nomUtilisateur){
        System.out.println("\nConnexion de l'utilisateur: " + nomUtilisateur);
        if (!listeNomsUtilisateurs.contains(nomUtilisateur)) {
            listeNomsUtilisateurs.add(nomUtilisateur);
        }
    }

    @Override
    public String toString() {
        return "Serveur{" +
                "listeNomsUtilisateurs=" + listeNomsUtilisateurs +
                '}';
    }
}
