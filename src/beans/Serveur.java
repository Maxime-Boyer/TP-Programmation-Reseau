package beans;

import stream_multithread.EchoServerMultiThreaded;

import java.util.ArrayList;

public class Serveur {

    private ArrayList<String> listeNomsUtilisateurs;
    private ArrayList<Conversation> listeConversations;
    private EchoServerMultiThreaded

    public Serveur(ArrayList<String> listeNomsUtilisateurs, ArrayList<Conversation> listeConversations) {
        this.listeNomsUtilisateurs = listeNomsUtilisateurs;
        this.listeConversations = listeConversations;
    }
}
