package beans;

import persistence.XMLModifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Serveur {

    private XMLModifier xmlModifier = new XMLModifier();
    private ArrayList<String> listeNomsUtilisateurs = new ArrayList<>();
    private ArrayList<Conversation> listeConversationsPublic = new ArrayList<>();
    private ArrayList<Conversation> listeConversationsPrivee = new ArrayList<>();

    private ArrayList<String> listeUtilisateurConnectes = new ArrayList<>();

    /**
     * Constructeur de Serveur initialisé au lancement du ServerMultiThreaded
     */
    public Serveur() {
        listeConversationsPublic = xmlModifier.getAllConversationPublic();
        listeConversationsPrivee = xmlModifier.getAllConversationPrivee();
        Collections.sort(listeConversationsPublic, Comparator.comparing((Conversation conversation) -> conversation.getNomConversation()));
        Collections.sort(listeConversationsPrivee, Comparator.comparing((Conversation conversation) -> conversation.getNomConversation()));
        listeNomsUtilisateurs = xmlModifier.getListeParticipantsServeur();
    }

    /**
     * Getter de la liste des noms d'utilisateurs
     */
    public ArrayList<String> getListeNomsUtilisateurs() {
        return listeNomsUtilisateurs;
    }

    public ArrayList<Conversation> getListeConversationsPublic() {
        return listeConversationsPublic;
    }

    public ArrayList<Conversation> getListeConversationsPrivee() {
        return listeConversationsPrivee;
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
        if(isGroupe){
            listeConversationsPublic.add(conversation);
        }else {
            listeConversationsPrivee.add(conversation);
        }
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
