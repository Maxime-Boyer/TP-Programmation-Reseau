package persistence;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import beans.Conversation;
import beans.Message;
import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XMLModifier {

    private String role1 = null;
    private String role2 = null;
    private String role3 = null;
    private String role4 = null;
    private ArrayList<String> rolev;

    public void saveToXML(Conversation conversation) {
        Document dom;
        Element e = null;

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
            ArrayList<Message> listeMessages = conversation.getListeMessages();
            ArrayList<String> listeParticipants = conversation.getListeParticipants();

            Element elemListeParticipants = dom.createElement("listeParticipants");

            for(int i = 0; i < listeParticipants.size(); i++) {
                // create data elements and place them under root
                e = dom.createElement("participant");
                e.setNodeValue(listeParticipants.get(i));
                //e.appendChild(dom.createTextNode(listeParticipants.get(i)));
                elemListeParticipants.appendChild(e);
            }

            elemConversation.appendChild(elemListeParticipants);

            for(int j = 0; j < listeMessages.size(); j++){
                e = dom.createElement("message");
                e.setAttribute("dateEnvoi", listeMessages.get(j).getDateEnvoi());
                e.setAttribute("expediteur", listeMessages.get(j).getNomAuteur());
                e.setNodeValue(listeMessages.get(j).getCorpsMessage());
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
                        new StreamResult(new FileOutputStream("src/FichierXML/" + nomFichier + ".xml")));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }


    }

    public boolean readXML(String xml) {
        /*rolev = new ArrayList<String>();
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            dom = db.parse(xml);

            Element doc = dom.getDocumentElement();

            role1 = getTextValue(role1, doc, "role1");
            if (role1 != null) {
                if (!role1.isEmpty())
                    rolev.add(role1);
            }
            role2 = getTextValue(role2, doc, "role2");
            if (role2 != null) {
                if (!role2.isEmpty())
                    rolev.add(role2);
            }
            role3 = getTextValue(role3, doc, "role3");
            if (role3 != null) {
                if (!role3.isEmpty())
                    rolev.add(role3);
            }
            role4 = getTextValue(role4, doc, "role4");
            if ( role4 != null) {
                if (!role4.isEmpty())
                    rolev.add(role4);
            }
            return true;

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }


         */
        return false;
    }

}
