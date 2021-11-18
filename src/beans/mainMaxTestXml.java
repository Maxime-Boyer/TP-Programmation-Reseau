package beans;

import persistence.XMLModifier;

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

        XMLModifier xmlModifier = new XMLModifier();

        xmlModifier.saveToXML(conversation);

    }
}
