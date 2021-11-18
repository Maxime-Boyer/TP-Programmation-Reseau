package persistence;

import java.io.File;

/**
 * Lister le contenu d'un rÃ©pertoire
 * http://www.fobec.com/java/964/lister-fichiers-dossiers-repertoire.html
 * @author fobec 2010
 */
public class ExplorateurFichier {

    private String initialpath = "";
    private Boolean recursivePath = false;
    public int filecount = 0;
    public int dircount = 0;

    /**
     * Constructeur
     * @param path chemin du répertoire
     * @param subFolder analyse des sous dossiers
     */
    public ExplorateurFichier(String path, Boolean subFolder) {
        super();
        this.initialpath = path;
        this.recursivePath = subFolder;
    }

    public void list() {
        this.listDirectory(this.initialpath);
    }

    public File[] getNomDesFichiers() {
        File file = new File(this.initialpath);
        File[] files = file.listFiles();
        return files;
    }

    private void listDirectory(String dir) {
        File file = new File(dir);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory() == true) {
                    System.out.println("Dossier: " + files[i].getAbsolutePath());
                    this.dircount++;
                } else {
                    System.out.println("  Fichier: " + files[i].getName());
                    this.filecount++;
                }
                if (files[i].isDirectory() == true && this.recursivePath == true) {
                    this.listDirectory(files[i].getAbsolutePath());
                }
            }
        }
    }
}