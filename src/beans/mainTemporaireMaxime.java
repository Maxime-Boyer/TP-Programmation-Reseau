package beans;

import jdk.jshell.execution.Util;

import javax.swing.text.Utilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class mainTemporaireMaxime {

    public static void main(String args[]) throws InterruptedException {

        ArrayList<String> listUtilisateurs = new ArrayList<>();
        listUtilisateurs.add("Lucas");
        listUtilisateurs.add("Morice");
        listUtilisateurs.add("Tony");
        listUtilisateurs.add("Tom");
        listUtilisateurs.add("Gérard");
        listUtilisateurs.add("Justine");
        listUtilisateurs.add("Adèle");
        listUtilisateurs.add("Babar");
        listUtilisateurs.add("Zizou");

        Collections.sort(listUtilisateurs, Comparator.comparing(String::toLowerCase));
        for(int i = 0; i < listUtilisateurs.size(); i++){
            System.out.println(" - " + i + " - " + listUtilisateurs.get(i));
        }

    }
}
