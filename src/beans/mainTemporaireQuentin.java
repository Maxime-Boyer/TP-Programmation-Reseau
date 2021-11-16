package beans;

public class mainTemporaireQuentin {

    public static void main(String args[]) throws InterruptedException {

        // création d'une conversation
        Conversation conversationGroupe = new Conversation("TP prog réseaux");

        // affichage du nom de la conversation
        System.out.println("\nNom de la conversation:");
        System.out.println(conversationGroupe.getNomConversation());

        // ajout d'utilisateurs à la conversation
        conversationGroupe.ajouterUtilisateur("Quentin");
        conversationGroupe.ajouterUtilisateur("Maxime");
        conversationGroupe.ajouterUtilisateur("Nathan");

        // affichage des utilisateurs participant à la conversation
        conversationGroupe.afficherListeParticipants();

        // Ajout de 2 messages à la conversation
        conversationGroupe.ajouterMessage("Quentin", "Hello les gars, petite pinte ce soir?");
        conversationGroupe.ajouterMessage("Maxime", "Grave chaud!");
        conversationGroupe.ajouterMessage("Nathan", "Je peux pas je suis à Hawaï");

        // Affichage des messages de la conversation
        conversationGroupe.afficherMessages();

    }
}
