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

    ClientThread(Socket s, Serveur serveur) {
        this.clientSocket = s;
        this.serveur = serveur;
    }

    /**
     * receives a request from client then sends an echo to the client
     * @param clientSocket the client socket
     **/
    public void run() {
        try {

            BufferedReader socIn = null;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            String line = socIn.readLine();
            nomUtilisateur = line;
            System.out.println("nomUtilisateur " + nomUtilisateur);
            serveur.connecterUtilisateur(nomUtilisateur);
            System.out.println(serveur);
            boolean nouveauClient = true;
            while (line != null) {
                if(!nouveauClient) {
                    line = socIn.readLine();
                    System.out.println("\nMessage du client " + nomUtilisateur + " :");
                    System.out.println(line);
                    socOut.println(line);
                }else{
                    socOut.println("Vous êtes connecté avec succès");
                    nouveauClient = false;
                }
            }
            System.out.println("\n---     Deconnexion du client "+nomUtilisateur+"     ---");
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
}

  