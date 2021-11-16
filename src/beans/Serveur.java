package beans;

import stream_multithread.EchoServerMultiThreaded;

import java.util.ArrayList;

public class Serveur {

    private ArrayList<String> listeNomsUtilisateurs;
    private ArrayList<Conversation> listeConversations;
    private EchoServerMultiThreaded echoServerMultiThreaded;

    public Serveur(EchoServerMultiThreaded echoServerMultiThreaded) {
        this.echoServerMultiThreaded = new EchoServerMultiThreaded();
    }

    public ArrayList<String> getListeNomsUtilisateurs() {
        return listeNomsUtilisateurs;
    }

    public ArrayList<Conversation> getListeConversations() {
        return listeConversations;
    }

    public EchoServerMultiThreaded getEchoServerMultiThreaded() {
        return echoServerMultiThreaded;
    }
}
