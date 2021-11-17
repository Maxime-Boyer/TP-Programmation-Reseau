package beans;

import java.util.ArrayList;

public class Conversation {

    private String nomConversation;
    private ArrayList<Message> listeMessages;
    private ArrayList<String> listeParticipants;
    private boolean conversationGroupe;

    /**
     * Crée une conversation de groupe entre 0 ou plusieurs utilisateurs. L'objet contient
     * la liste des messages envoyés sur la conversation et la liste des auteurs
     * @param nomConversation: le nom de la conversation pour pouvoir l'identifier sur le serveur
     */
    public Conversation(String nomConversation) {
        this.nomConversation = nomConversation;
        this.listeMessages = new ArrayList<Message>();
        this.listeParticipants = new ArrayList<String>();
        this.conversationGroupe = true;
    }

    /**
     * Crée une conversation privée entre 2 utilisateurs. L'objet contient
     * la liste des messages envoyés sur la conversation et les deux auteurs.
     * La clef est générée par "nomOrdreAlphabetique1 et nomOrdreAlphabetique2"
     * @param nomUtilisateur: le nom de l'utilisateur qui crée la conversatoion
     * @param nomDuCorrespondant: le nom de l'utilisateur avec qui il correspond
     */
    public Conversation(String nomUtilisateur, String nomDuCorrespondant) {

        this("");

        //determination de la clef de la conversation par ordre lexicographique
        this.nomConversation = determinerNomConversationAvec2NomsUtilisateurs(nomUtilisateur, nomDuCorrespondant);

        // creation de la conversation
        this.conversationGroupe = false;

        // ajout des 2 seuls utilisateurs
        ajouterUtilisateur(nomUtilisateur);
        ajouterUtilisateur(nomDuCorrespondant);
    }

    /**
     * determine la nom d'une conversation grace à 2 noms selon l'ordre lexicographique
     * @param nom1: le nom d'un utilisateur
     * @param nom2: le nom d'un autre utilisateur
     *            l'ordre de nom1 en nom2 n'importe pas
     * @return: le nom de la conversation par ordre lexicographique
     */
    public static String determinerNomConversationAvec2NomsUtilisateurs(String nom1, String nom2){
        String nomConversation = "";
        if(nom1.compareTo(nom2) < 0){
            nomConversation = nom1 + " et " + nom2;
        }
        else{
            nomConversation = nom2 + " et " + nom1;
        }
        return nomConversation;
    }

    public String getNomConversation() {
        return nomConversation;
    }

    public boolean isConversationGroupe() {
        return conversationGroupe;
    }

    /**
     * affiche la liste des utilisateurs participant à la conversation
     */
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

    public ArrayList<String> getListeParticipants() {
        return listeParticipants;
    }
}
