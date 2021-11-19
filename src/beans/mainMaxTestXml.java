package beans;

import persistence.XMLModifier;

import java.util.ArrayList;

public class mainMaxTestXml {

    public static void main(String args[]){
        Conversation conversation = new Conversation("RESTO en club");
        conversation.ajouterUtilisateur(new Utilisateur("Franck").getNomUtilisateur());
        conversation.ajouterUtilisateur(new Utilisateur("Lola").getNomUtilisateur());
        conversation.ajouterUtilisateur(new Utilisateur("Patrice").getNomUtilisateur());
        conversation.ajouterUtilisateur(new Utilisateur("Tom").getNomUtilisateur());
        conversation.ajouterMessage("Franck", "Quelle heure demain ?");
        conversation.ajouterMessage("Lola", "8h");
        conversation.ajouterMessage("Franck", "Ok");
        conversation.ajouterMessage("Patrice", "J'peux pas j'ai tir à l'arc");
        conversation.ajouterMessage("Tom", "Je viens pour minuit");

        Conversation conversation2 = new Conversation("Galere au parc");
        conversation2.ajouterUtilisateur(new Utilisateur("Solène").getNomUtilisateur());
        conversation2.ajouterUtilisateur(new Utilisateur("Cécé").getNomUtilisateur());
        conversation2.ajouterUtilisateur(new Utilisateur("Margo").getNomUtilisateur());
        conversation2.ajouterUtilisateur(new Utilisateur("Alex").getNomUtilisateur());
        conversation2.ajouterMessage("Solène", "Y'a la hendeck");
        conversation2.ajouterMessage("Cécé", "Courez");
        conversation2.ajouterMessage("Alex", "Faut fuire la");
        conversation2.ajouterMessage("Alex", "J'ai oublié des trucs");
        conversation2.ajouterMessage("Margo", "Il est quelle heure ?");

        XMLModifier xmlModifier = new XMLModifier();

        xmlModifier.stockerConversation(conversation);
        xmlModifier.stockerConversation(conversation2);

        xmlModifier.stockerMessage(conversation, new Message("Lola", "J'suis trop une folle"));

        xmlModifier.stockerNouveauParticipant(conversation, "Lucas");

        xmlModifier.stockerNouveauParticipant(conversation2, "Max");

        xmlModifier.stockerMessage(conversation2, new Message("Max", "J'suis trop un fou"));

        //xmlModifier.afficherConversation(conversation2.getNomConversation());

        ArrayList<Message> listeStringMessage = xmlModifier.getListeMessagesConversation(conversation2.getNomConversation());
        ArrayList<String> listeStringParticipant = xmlModifier.getListeParticipantsConversation(conversation2.getNomConversation());
        System.out.println("********************************");
        for(int i = 0; i < listeStringMessage.size(); i++){
            System.out.println(listeStringMessage.get(i).toString());
        }
        System.out.println("********************************");
        for(int i = 0; i < listeStringParticipant.size(); i++){
            System.out.println(listeStringParticipant.get(i));
        }
        System.out.println("********************************");

        xmlModifier.stockerUtilisateurServeur("HelloAsso");
        xmlModifier.stockerUtilisateurServeur("Martin");
        xmlModifier.stockerUtilisateurServeur("Loris");
        xmlModifier.stockerUtilisateurServeur("Mat");

        System.out.println("Fin");
    }
}
