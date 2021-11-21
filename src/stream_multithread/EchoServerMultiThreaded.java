package stream_multithread;
import beans.Serveur;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class EchoServerMultiThreaded  {

    //key = nomUtilisateur
    static HashMap<String, ClientThread> clientThreads = new HashMap<>();

    /**
     * main du serveur: permet la synchro de l'ensemble des threads utilisateur
     * @param args port
     **/
    public static void main(String args[]){

        ServerSocket listenSocket;

        if (args.length != 1) {
            System.out.println("Utilisation: démarrer une config avec le port du serveur en entrée");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            Serveur serveur = new Serveur(); //creer le serveur
            System.out.println("Serveur prêt...\n");

            // ecoute de nouvelles connexion, creation d'un nouveau thread en cas de connexion
            while (true) {
                Socket clientSocket = listenSocket.accept();
                BufferedReader socInClient = null;
                socInClient = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                //Buffer de sortie de la socket
                PrintStream socOutClient = null;
                socOutClient = new PrintStream(clientSocket.getOutputStream());

                //On recupere le username de l'utilisateur
                String nomUtilisateur = socInClient.readLine();

                System.out.println("on est la");
                ClientThread ct = new ClientThread(clientSocket, serveur, nomUtilisateur, socInClient, socOutClient);
                clientThreads.put(nomUtilisateur, ct);
                ct.start();
                /*if(serveur.getListeUtilisateurConnectes().contains(nomUtilisateur)){
                    System.out.println(nomUtilisateur+" est déjà déjà connecté sur un autre appareil, pour pouvoir le connecter il faut le déconnecter sur l'autre appareil ");
                }else {
                    System.out.println(nomUtilisateur+" est connecté au serveur.");
                    if(clientThreads.containsKey(nomUtilisateur)) {
                        clientThreads.get(nomUtilisateur).start();
                    }else {
                        System.out.println("on est la");
                        ClientThread ct = new ClientThread(clientSocket, serveur, nomUtilisateur, socInClient, socOutClient);
                        clientThreads.put(nomUtilisateur, ct);
                        ct.start();
                    }
                }*/
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
}

  