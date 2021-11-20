package persistence;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import beans.Conversation;
import beans.Message;
import beans.Utilisateur;
import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class XMLModifier {

    //TODO Maxime: Javadoc des methodes de cette classe

    public void stockerUtilisateurServeur(String nomUtilisateur){
        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/donneesServeur/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;


        for(int i = 0; i < listFichier.length; i++) {
            String nomFichier = "ListeDesParticipants.xml";
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (listFichier[i].getName().equals(nomFichier)) {

                fichierTrouve = true;

                String file = "src/donneesServeur/" + nomFichier;
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(file);
                    // create instance of DOM
                    Document dom = db.newDocument();

                    Node elemListeParticipant = doc.getElementsByTagName("listeParticipants").item(0);

                    Element e = doc.createElement("participant");
                    e.setTextContent(nomUtilisateur);
                    elemListeParticipant.appendChild(e);

                    try {
                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        tr.setOutputProperty(OutputKeys.INDENT, "yes");
                        tr.setOutputProperty(OutputKeys.METHOD, "xml");
                        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "listeParticipants.dtd");
                        tr.setOutputProperty("{http://xml.Apache.org/xslt}indent-amount", "4");

                        // send DOM to file
                        tr.transform(new DOMSource(doc),
                                new StreamResult(new FileOutputStream(file)));
                    } catch (TransformerConfigurationException transformerConfigurationException) {
                        transformerConfigurationException.printStackTrace();
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    } catch (TransformerException transformerException) {
                        transformerException.printStackTrace();
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!fichierTrouve) {
            ArrayList<String> nouveauParticipant = new ArrayList<>();
            nouveauParticipant.add(nomUtilisateur);
            creerDocListeParticipants(nouveauParticipant);
        }
    }

    public void creerDocListeParticipants(ArrayList<String> listeParticipants) {
        Document dom;
        Element e = null;

        //Sinon on créer un nouveau document
        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element elemListeParticipants = dom.createElement("listeParticipants");

            for (int i = 0; i < listeParticipants.size(); i++) {
                // create data elements and place them under root
                e = dom.createElement("participant");
                e.setTextContent(listeParticipants.get(i));
                //e.appendChild(dom.createTextNode(listeParticipants.get(i)));
                elemListeParticipants.appendChild(e);
            }

            dom.appendChild(elemListeParticipants);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "listeParticipants.dtd");
                tr.setOutputProperty("{http://xml.Apache.org/xslt}indent-amount", "4");

                String nomFichier = "ListeDesParticipants.xml";

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream("src/donneesServeur/" + nomFichier)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    public void stockerMessage(Conversation conversation, Message message){
        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/fichierXML/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;


        for(int i = 0; i < listFichier.length; i++) {
            String nomFichier = conversation.getNomConversation().replaceAll("\\s", "") + ".xml";
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (listFichier[i].getName().equals(nomFichier)) {
                fichierTrouve = true;
                try {

                    String file = "src/fichierXML/" + nomFichier;
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(file);
                    // create instance of DOM
                    Document dom = db.newDocument();

                    // Récupérer l'élément racine
                    Node elemConversation = doc.getDocumentElement();;

                    Element e = doc.createElement("message");
                    e.setAttribute("dateEnvoi", message.getDateEnvoi());
                    e.setAttribute("expediteur", message.getNomAuteur());
                    e.setTextContent(message.getCorpsMessage());
                    elemConversation.appendChild(e);

                    try {
                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        tr.setOutputProperty(OutputKeys.INDENT, "yes");
                        tr.setOutputProperty(OutputKeys.METHOD, "xml");
                        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "conversation.dtd");
                        tr.setOutputProperty("{http://xml.Apache.org/xslt}indent-amount", "4");

                        // send DOM to file
                        tr.transform(new DOMSource(doc),
                                new StreamResult(new FileOutputStream(file)));

                    } catch (TransformerException te) {
                        System.out.println(te.getMessage());
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (!fichierTrouve) {
            stockerConversation(conversation);
        }
    }

    public void stockerNouveauParticipant(Conversation conversation, String nomUtilisateur){
        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/fichierXML/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;


        for(int i = 0; i < listFichier.length; i++) {
            String nomFichier = conversation.getNomConversation().replaceAll("\\s", "") + ".xml";
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (listFichier[i].getName().equals(nomFichier)) {
                fichierTrouve = true;
                try {

                    String file = "src/fichierXML/" + nomFichier;
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(file);
                    // create instance of DOM
                    Document dom = db.newDocument();

                    Node elemListeParticipant = doc.getElementsByTagName("listeParticipants").item(0);

                    Element e = doc.createElement("participant");
                    e.setTextContent(nomUtilisateur);
                    elemListeParticipant.appendChild(e);

                    try {
                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        tr.setOutputProperty(OutputKeys.INDENT, "yes");
                        tr.setOutputProperty(OutputKeys.METHOD, "xml");
                        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "conversation.dtd");
                        tr.setOutputProperty("{http://xml.Apache.org/xslt}indent-amount", "4");

                        // send DOM to file
                        tr.transform(new DOMSource(doc),
                                new StreamResult(new FileOutputStream(file)));

                    } catch (TransformerException te) {
                        System.out.println(te.getMessage());
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (!fichierTrouve) {
            stockerConversation(conversation);
        }
    }

    public void stockerConversation(Conversation conversation) {
        Document dom;
        Element e = null;

        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/fichierXML/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;

        for(int i = 0; i < listFichier.length; i++){

            //Si le fichier existe déjà on ajoute des informations à la conversation : messages, particpants
            if(listFichier[i].getName().equals(conversation.getNomConversation())){
                fichierTrouve = true;
            }
            break;
        }
        if(!fichierTrouve) {

            //Sinon on créer un nouveau document
            // instance of a DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                // use factory to get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();
                // create instance of DOM
                dom = db.newDocument();

                // create the root element
                Element elemConversation = dom.createElement("conversation");

                String nomConversation = conversation.getNomConversation();
                elemConversation.setAttribute("nomConversation", nomConversation);

                String typeConversation = "privee";
                if(conversation.isConversationGroupe())
                    typeConversation = "groupe";
                elemConversation.setAttribute("typeConversation", typeConversation);

                ArrayList<Message> listeMessages = conversation.getListeMessages();
                ArrayList<String> listeParticipants = conversation.getListeParticipants();

                Element elemListeParticipants = dom.createElement("listeParticipants");

                for (int i = 0; i < listeParticipants.size(); i++) {
                    // create data elements and place them under root
                    e = dom.createElement("participant");
                    e.setTextContent(listeParticipants.get(i));
                    //e.appendChild(dom.createTextNode(listeParticipants.get(i)));
                    elemListeParticipants.appendChild(e);
                }

                elemConversation.appendChild(elemListeParticipants);

                for (int j = 0; j < listeMessages.size(); j++) {
                    e = dom.createElement("message");
                    e.setAttribute("dateEnvoi", listeMessages.get(j).getDateEnvoi());
                    e.setAttribute("expediteur", listeMessages.get(j).getNomAuteur());
                    e.setTextContent(listeMessages.get(j).getCorpsMessage());
                    elemConversation.appendChild(e);
                }

                dom.appendChild(elemConversation);

                try {
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.setOutputProperty(OutputKeys.METHOD, "xml");
                    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "conversation.dtd");
                    tr.setOutputProperty("{http://xml.Apache.org/xslt}indent-amount", "4");

                    String nomFichier = nomConversation.replaceAll("\\s", "");

                    // send DOM to file
                    tr.transform(new DOMSource(dom),
                            new StreamResult(new FileOutputStream("src/fichierXML/" + nomFichier + ".xml")));

                } catch (TransformerException te) {
                    System.out.println(te.getMessage());
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            } catch (ParserConfigurationException pce) {
                System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
            }

        }
    }

    public void afficherConversation(String nomConversation) {

        String nomFichier = nomConversation.replaceAll("\\s", "") + ".xml";

        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/fichierXML/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;


        for(int i = 0; i < listFichier.length; i++) {
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (listFichier[i].getName().equals(nomFichier)) {
                Document dom;
                // Make an  instance of the DocumentBuilderFactory
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // use the factory to take an instance of the document builder
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    // parse using the builder to get the DOM mapping of the
                    // XML file
                    dom = db.parse("src/fichierXML/" + nomFichier);

                    Element doc = dom.getDocumentElement();

                    NodeList listeParticipant = doc.getElementsByTagName("participant");

                    for(int j = 0; j < listeParticipant.getLength(); j++){
                        System.out.println("Participant : " + listeParticipant.item(j).getTextContent());
                    }

                    NodeList listeMessage = doc.getElementsByTagName("message");

                    for(int j = 0; j < listeMessage.getLength(); j++){
                        NamedNodeMap listeAttributs = listeMessage.item(j).getAttributes();
                        System.out.println("Message de " + listeAttributs.item(1).getTextContent() + " envoyé à " + listeAttributs.item(0).getTextContent() + " : " + listeMessage.item(j).getTextContent());
                    }
                } catch (ParserConfigurationException pce) {
                    System.out.println(pce.getMessage());
                } catch (SAXException se) {
                    System.out.println(se.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }

    }

    public Conversation getConversation(String nomConversation) {

        String nomFichier = nomConversation.replaceAll("\\s", "") + ".xml";

        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/fichierXML/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;

        Conversation conversation = null;

        for(int i = 0; i < listFichier.length; i++) {
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (listFichier[i].getName().equals(nomFichier)) {
                Document dom;
                // Make an  instance of the DocumentBuilderFactory
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // use the factory to take an instance of the document builder
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    // parse using the builder to get the DOM mapping of the
                    // XML file
                    dom = db.parse("src/fichierXML/" + nomFichier);

                    Element doc = dom.getDocumentElement();

                    conversation = new Conversation(nomConversation);

                    NodeList listeParticipant = doc.getElementsByTagName("participant");

                    for(int j = 0; j < listeParticipant.getLength(); j++){
                        conversation.ajouterUtilisateur(listeParticipant.item(j).getTextContent());
                    }

                    NodeList listeMessage = doc.getElementsByTagName("message");

                    for(int j = 0; j < listeMessage.getLength(); j++){
                        NamedNodeMap listeAttributs = listeMessage.item(j).getAttributes();
                        conversation.ajouterMessage(listeAttributs.item(1).getTextContent() , listeMessage.item(j).getTextContent(), listeAttributs.item(0).getTextContent());
                    }
                } catch (ParserConfigurationException pce) {
                    System.out.println(pce.getMessage());
                } catch (SAXException se) {
                    System.out.println(se.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }
        return conversation;
    }

    public ArrayList<Conversation> getAllConversation() {

        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/fichierXML/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;
        ArrayList<Conversation> listeConversation = new ArrayList<>();

        for(int i = 0; i < listFichier.length; i++) {
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (!listFichier[i].getName().equals("conversation.dtd")) {
                Document dom;
                // Make an  instance of the DocumentBuilderFactory
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // use the factory to take an instance of the document builder
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    // parse using the builder to get the DOM mapping of the
                    // XML file
                    dom = db.parse("src/fichierXML/" + listFichier[i].getName());

                    Element doc = dom.getDocumentElement();

                    Conversation conversation = new Conversation(doc.getAttributes().item(0).getTextContent());

                    String typeConversation = doc.getAttributes().item(1).getTextContent();
                    if(typeConversation.equals("groupe")){
                        conversation.setConversationGroupe(true);
                    }
                    else{
                        conversation.setConversationGroupe(false);
                    }

                    NodeList listeParticipant = doc.getElementsByTagName("participant");

                    for(int j = 0; j < listeParticipant.getLength(); j++){
                        conversation.ajouterUtilisateur(listeParticipant.item(j).getTextContent());
                    }

                    NodeList listeMessage = doc.getElementsByTagName("message");

                    for(int j = 0; j < listeMessage.getLength(); j++){
                        NamedNodeMap listeAttributs = listeMessage.item(j).getAttributes();
                        conversation.ajouterMessage(listeAttributs.item(1).getTextContent() , listeMessage.item(j).getTextContent(), listeAttributs.item(0).getTextContent());
                    }
                    listeConversation.add(conversation);
                } catch (ParserConfigurationException pce) {
                    System.out.println(pce.getMessage());
                } catch (SAXException se) {
                    System.out.println(se.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }
        return listeConversation;
    }

    public ArrayList<String> getListeParticipantsConversation(String nomConversation) {

        String nomFichier = nomConversation.replaceAll("\\s", "") + ".xml";

        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/fichierXML/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;

        ArrayList<String> listeNomsParticipants = new ArrayList<>();

        for(int i = 0; i < listFichier.length; i++) {
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (listFichier[i].getName().equals(nomFichier)) {
                Document dom;
                // Make an  instance of the DocumentBuilderFactory
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // use the factory to take an instance of the document builder
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    // parse using the builder to get the DOM mapping of the
                    // XML file
                    dom = db.parse("src/fichierXML/" + nomFichier);

                    Element doc = dom.getDocumentElement();

                    NodeList listeParticipant = doc.getElementsByTagName("participant");

                    for(int j = 0; j < listeParticipant.getLength(); j++){
                        listeNomsParticipants.add(listeParticipant.item(j).getTextContent());
                    }

                } catch (ParserConfigurationException pce) {
                    System.out.println(pce.getMessage());
                } catch (SAXException se) {
                    System.out.println(se.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }

        return listeNomsParticipants;
    }

    public ArrayList<String> getListeParticipantsServeur() {

        String nomFichier = "ListeDesParticipants.xml";

        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/donneesServeur/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;

        ArrayList<String> listeNomsParticipants = new ArrayList<>();

        for(int i = 0; i < listFichier.length; i++) {
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (listFichier[i].getName().equals(nomFichier)) {
                Document dom;
                // Make an  instance of the DocumentBuilderFactory
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // use the factory to take an instance of the document builder
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    // parse using the builder to get the DOM mapping of the
                    // XML file
                    dom = db.parse("src/donneesServeur/" + nomFichier);

                    Element doc = dom.getDocumentElement();

                    NodeList listeParticipant = doc.getElementsByTagName("participant");

                    for(int j = 0; j < listeParticipant.getLength(); j++){
                        listeNomsParticipants.add(listeParticipant.item(j).getTextContent());
                    }

                } catch (ParserConfigurationException pce) {
                    System.out.println(pce.getMessage());
                } catch (SAXException se) {
                    System.out.println(se.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }
        return listeNomsParticipants;
    }

    public ArrayList<Message> getListeMessagesConversation(String nomConversation) {

        String nomFichier = nomConversation.replaceAll("\\s", "") + ".xml";

        ExplorateurFichier explorateurFichier = new ExplorateurFichier("src/fichierXML/", false);
        File[] listFichier = explorateurFichier.getNomDesFichiers();
        boolean fichierTrouve = false;

        ArrayList<Message> listeMessages = new ArrayList<>();


        for(int i = 0; i < listFichier.length; i++) {
            //Si le fichier existe déjà on ajoute des informations à la conversation : messages
            if (listFichier[i].getName().equals(nomFichier)) {
                Document dom;
                // Make an  instance of the DocumentBuilderFactory
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // use the factory to take an instance of the document builder
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    // parse using the builder to get the DOM mapping of the
                    // XML file
                    dom = db.parse("src/fichierXML/" + nomFichier);

                    Element doc = dom.getDocumentElement();

                    NodeList listeMessage = doc.getElementsByTagName("message");

                    for(int j = 0; j < listeMessage.getLength(); j++){
                        NamedNodeMap listeAttributs = listeMessage.item(j).getAttributes();
                        listeMessages.add(new Message(listeAttributs.item(1).getTextContent(), listeMessage.item(j).getTextContent(), listeAttributs.item(0).getTextContent()));
                    }
                } catch (ParserConfigurationException pce) {
                    System.out.println(pce.getMessage());
                } catch (SAXException se) {
                    System.out.println(se.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return listeMessages;
    }

}

