
package stream;

import java.io.*;
import java.net.*;

public class EchoClient {

    static PrintStream socOut = null;
    static BufferedReader stdIn = null;
    static BufferedReader socIn = null;
    /**
     * Méthode main
     * Accepte une connection, reçoit un message du client et ensuite envoi un echo au client
     * @param args: les entrees de la config
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        Socket echoSocket = null;
        String nomUtilisateur = null;

        //Nombre d'arguments à 3, car le nom d'utilisateur est aussi passé en argument
        if (args.length != 2) {
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
            System.out.println("Entrez votre identifiant puis taper entrer");
            nomUtilisateur=stdIn.readLine();
            socOut.println(nomUtilisateur);
            while(socIn.readLine().equals("Erreur utilisateur déjà connecté")){
                System.out.println("Utilisateur déjà connecté sur un autre appareil");
                System.out.println("Veuillez le déconnecté de l'autre appareil pour vous connecter sur celui-ci");
                System.out.println("Entrez à nouveau votre identifiant puis taper entrer");
                nomUtilisateur=stdIn.readLine();
                socOut.println(nomUtilisateur);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        /****************************************************************************/
        /*      ICI COMMENCE LES THREADS POUR L'ENVOIE ET LA RECEPTION DE MESSSAGE  */
        /****************************************************************************/

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

        // maintient en vie du thread pendant 1h
        Thread.sleep(60*60*3600);

        socOut.close();
        socIn.close();
        stdIn.close();
        echoSocket.close();
    }
}


