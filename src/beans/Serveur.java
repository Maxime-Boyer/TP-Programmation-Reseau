package beans;

import persistence.XMLModifier;
import java.util.ArrayList;

public class Serveur {

    private XMLModifier xmlModifier = new XMLModifier();
    private ArrayList<String> listeNomsUtilisateurs = new ArrayList<>();
    private ArrayList<Conversation> listeConversations = new ArrayList<>();
    private ArrayList<String> listeUtilisateurConnectes = new ArrayList<>();

    /**
     * Constructeur de Serveur initialisé au lancement du ServerMultiThreaded
     */
    public Serveur() {
        listeConversations = xmlModifier.getAllConversation();
        listeNomsUtilisateurs = xmlModifier.getListeParticipantsServeur();
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
     *
     * Getter de la liste des utilisateurs connectés
     */
    public ArrayList<String> getListeUtilisateurConnectes() {
        return listeUtilisateurConnectes;
    }

    /**
     * Méthode permettant d'ajouter une conversation à la liste des conversations
     * @param nomConversation
     */
    public void ajouterConversations(String nomConversation, boolean isGroupe){
        Conversation conversation = new Conversation(nomConversation);
        conversation.setConversationGroupe(isGroupe);
        listeConversations.add(conversation);
        xmlModifier.stockerConversation(conversation, isGroupe);
    }

    /**
     * Méthode permettant d'ajouter un utilisateur à la liste des utilisateurs
     * @param nomUtilisateur
     */
    public void connecterUtilisateur(String nomUtilisateur){
        if (!listeNomsUtilisateurs.contains(nomUtilisateur)) {
            listeNomsUtilisateurs.add(nomUtilisateur);
            xmlModifier.stockerUtilisateurServeur(nomUtilisateur);
        }
        listeUtilisateurConnectes.add(nomUtilisateur);
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
