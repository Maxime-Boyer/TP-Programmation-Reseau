package stream_multithread; /***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

import beans.Serveur;

import java.io.*;
import java.net.*;
import java.sql.SQLOutput;

public class ClientThread extends Thread {

    private Socket clientSocket;
    private Serveur serveur;
    private String nomUtilisateur;

    /**
     * TODO javadoc Clienthread
     * @param s
     * @param serveur
     */
    ClientThread(Socket s, Serveur serveur) {
        this.clientSocket = s;
        this.serveur = serveur;
    }

    /**
     * TODO traduction
     * receives a request from client then sends an echo to the client
     * @param clientSocket the client socket
     **/
    public void run() {

        try {

            // initialisation des canaux de communication
            BufferedReader socIn = null;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            String line = socIn.readLine();

            // connection de l'utilisateur
            nomUtilisateur = line;
            serveur.connecterUtilisateur(nomUtilisateur);
            System.out.println("\n"+serveur);

            while (line != null) {
                socOut.println("Vous êtes connecté avec succès");
            }
            socOut.println("Deconnexion réussie.");
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
}

  