/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package stream;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.*;
import java.sql.SQLOutput;


public class EchoClient {


    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;

        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            echoSocket = new Socket(args[0],new Integer(args[1]).intValue());
            socIn = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            socOut= new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            //entree client

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        String line;
        System.out.println("Entrez votre identifiant puis taper entrer");
        while (true) {
            //System.out.println("Début affichage");
            line=stdIn.readLine(); //point d'attente
            if (line.equals(".")) break;
            socOut.println(line);

            //on affiche tout ce qui est renvoye par le thread
            String affichage = socIn.readLine();
            while(affichage.length() > 0){
                //System.out.println("taille: "+affichage.length());
                System.out.println(affichage);
                affichage = socIn.readLine();
            }
            //System.out.println("Fin affichage");
        }
        socOut.close();
        socIn.close();
        stdIn.close();
        echoSocket.close();
    }
}


