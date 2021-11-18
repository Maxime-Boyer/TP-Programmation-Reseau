/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package stream;

import stream_multithread.ClientThread;
import toDelete.Client;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.*;
import java.sql.SQLOutput;


public class EchoClient {

    static PrintStream socOut = null;
    static BufferedReader stdIn = null;
    static BufferedReader socIn = null;
    /**
     * Méthode main
     * Accepte une connection, reçoit un message du client et ensuite envoi un echo au client
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;

        String nomUtilisateur = null;

        //Nombre d'arguments à 3, car le nom d'utilisateur est aussi passé en argument
        if (args.length != 3) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            echoSocket = new Socket(args[0],new Integer(args[1]).intValue());
            socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            socOut= new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            //recupe le nom d'utilisateur
            nomUtilisateur = args[2];
            socOut.println(nomUtilisateur);
            System.out.println(nomUtilisateur);

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        //ICI COMMENCE LES THREADS POUR L'ENVOIE ET LA RECEPTION DE MESSSAGE

        //Thread d'envoie de messages à un ou plusieurs clients

        Thread sendMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        String message = stdIn.readLine();
                        socOut.println(message);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        //Thread de lecture de messages reçuent

        Thread readMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {

                        String message = socIn.readLine();
                        System.out.println(message);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //On demarre les threads
        sendMessage.start();
        readMessage.start();

        String line;
        //System.out.println("Entrez votre identifiant puis taper entrer");
        while (true) {
            //on affiche tout ce qui est renvoye par le thread
            String affichage = "";

            //line=stdIn.readLine(); //point d'attente
            //socOut.println(line);

            while(true){
                affichage = socIn.readLine();

                if(affichage.equals(ClientThread.FIN_AFFICHAGE) || affichage.equals(ClientThread.DECONNEXION))
                    break;

                System.out.println(affichage);
            }

            if(affichage.equals(ClientThread.DECONNEXION))
                break;
        }
        socOut.close();
        socIn.close();
        stdIn.close();
        echoSocket.close();
    }
}


