package beans;

import java.util.ArrayList;

public class Utilisateur {

    private String nomUtilisateur;
    private ArrayList<String> listeNomsConversations;

    /**
     * Getter de l'attribut nomUtilisateur
     * @param nomUtilisateur
     */
    public Utilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

}
