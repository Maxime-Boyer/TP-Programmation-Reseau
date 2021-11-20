package beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private String nomAuteur;
    private String corpsMessage;
    private Date dateEnvoi;
    private SimpleDateFormat formatDate;

    /**
     * Message textuel envoyé par un utilisateur dans une conversation
     * @param nomAuteur: le nom de l'auteur du message
     * @param corpsMessage: le message écrit par l'auteur
     */
    public Message(String nomAuteur, String corpsMessage) {
        this.nomAuteur = nomAuteur;
        this.corpsMessage = corpsMessage;

        formatDate= new SimpleDateFormat("yyyy-MM-dd 'à' HH'h'mm");
        this.dateEnvoi = new Date(System.currentTimeMillis());
    }

    /**
     * Message textuel envoyé par un utilisateur dans une conversation
     * @param nomAuteur: le nom de l'auteur du message
     * @param corpsMessage: le message écrit par l'auteur
     * @param stringDateEnvoi: la date du message
     * @throws ParseException
     */
    public Message(String nomAuteur, String corpsMessage, String stringDateEnvoi) throws ParseException {
        this.nomAuteur = nomAuteur;
        this.corpsMessage = corpsMessage;
        formatDate= new SimpleDateFormat("yyyy-MM-dd 'à' HH'h'mm");
        this.dateEnvoi = formatDate.parse(stringDateEnvoi);
    }

    public String getNomAuteur() {
        return nomAuteur;
    }

    public String getCorpsMessage() {
        return corpsMessage;
    }

    public String getDateEnvoi() {
        return formatDate.format(dateEnvoi);
    }

    @Override
    public String toString() {
        return "Message{" +
                "nomAuteur='" + nomAuteur + '\'' +
                ", corpsMessage='" + corpsMessage + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", formatDate=" + formatDate +
                '}';
    }
}
