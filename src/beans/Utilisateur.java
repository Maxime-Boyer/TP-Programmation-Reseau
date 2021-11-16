package beans;

import java.util.ArrayList;

public class Utilisateur {

    private String nomUtilisateur;
    private ArrayList<String> listeNomsConversations;

    public Utilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
}
