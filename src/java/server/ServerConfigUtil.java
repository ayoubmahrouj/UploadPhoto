/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import bean.Produit;
import controller.util.Item;
import controller.util.JsfUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ayoubmahrouj
 */
public class ServerConfigUtil {

    private static String vmParam = "produit.files.path";//chemin dont laquelle on va creer le dosqsier globale qui aura pour bute de contenir la totalitees des dossier d un abonnee

    private static List<Item> taxePaths = new ArrayList();

    static {

        filesPath(taxePaths, "libelles_dossiers");

    }

    public static String getCheminePhoto(Produit produit, boolean write) throws IOException {
        createRepoIfNotExist(getCommuneFilePath(produit, write));
        return getCommuneFilePath(produit, write)+"/photo";
    }

    private static String getContextParameter(String paramInWeb) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String myConstantValue = ctx.getExternalContext().getInitParameter(paramInWeb);
        return myConstantValue;
    }

    private static void filesPath(List<Item> items, String nameVariable) {

        String filesName = getContextParameter(nameVariable);

        String paths[] = filesName.split(",");

        for (int i = 0; i < paths.length; i++) {

            String path = paths[i];

            String[] oneFileConfig = path.split("=");

            items.add(new Item(oneFileConfig[0], oneFileConfig[1]));

        }

    }

    private static String findFileByPath(List<Item> items, String path) {

        for (int i = 0; i < items.size(); i++) {

            Item sessionItem = items.get(i);

            if (sessionItem.getKey().equalsIgnoreCase(path)) {

                return sessionItem.getObject().toString();

            }

        }

        return null;

    }

    private static int createFile(File file) {

        if (!file.exists()) {

            if (file.mkdir()) {

                return 1; //file.getName() + " Directory is created!";

            }

            return -1;//"Failed to create " + file.getName() + " directory!";

        }

        return -2; //file.getName() + " Directory already existe!";

    }

    public static String getCommuneFilePath(Produit produit, boolean write) {

        String rootPath = "";

        if (write) {

            rootPath = System.getProperty(vmParam);

            System.out.println("ha pzth:::." + rootPath);

        } else {

            rootPath = "/photo";
            System.out.println("hana rootPath....> "+rootPath);
        }

        return rootPath ;//+ "/produit-" + produit.getRef();

    }

    private static void createCommuneFiles(Produit commune) {

        for (Item taxePath : taxePaths) {

            createFile(new File(getCommuneFilePath(commune, true) + "\\" + taxePath.getObject().toString()));

        }

    }

    private static void createRepoIfNotExist(String repo) throws IOException {

        Path path = Paths.get(repo);

        if (!Files.exists(path)) {

            Files.createDirectories(path);

        }

    }

    public static String getChangementFilePath(Produit commune, boolean write) {

        return getCommuneFilePath(commune, write) + "/Changement";

    }

    public static String getChaumagePath(Produit commune, boolean write) {

        return getCommuneFilePath(commune, write);

    }

    public static String getChangementEtAlienationPath(Produit commune, boolean write) throws IOException {

        createRepoIfNotExist(getCommuneFilePath(commune, write) + "/ChangementEtAlienation");

        return getCommuneFilePath(commune, write) + "/ChangementEtAlienation";

    }

    public static String getDeclarationExistencePath(Produit commune, boolean write) throws IOException {

        createRepoIfNotExist(getCommuneFilePath(commune, write) + "/DeclarationExistence");

        return getCommuneFilePath(commune, write) + "/DeclarationExistence";

    }

    public static String getEntrePath(Produit commune, boolean write) {

        return getCommuneFilePath(commune, write) + "/Entree";

    }

    public static String getSortiePath(Produit commune, boolean write) {

        return getCommuneFilePath(commune, write) + "/Sortie";

    }

    public static String getInfoPath(Produit commune, boolean write) {

        return getCommuneFilePath(commune, write) + "/info";

    }

    public static void createCommuneFile(Produit commune) {

        createFile(new File(getCommuneFilePath(commune, true)));

        createCommuneFiles(commune);

    }

    public static void upload(UploadedFile uploadedFile, String destinationPath, String nameOfUploadeFile) {
            System.out.println("haaaa 1 "+uploadedFile.getFileName());
            System.out.println("haaaa 2 "+uploadedFile.getContentType());
            System.out.println("haaaa 3 "+nameOfUploadeFile);
            System.out.println("destinationPath "+destinationPath);
            System.out.println("nameOfUploadeFile "+nameOfUploadeFile);
            
        try {

            String fileSavePath = destinationPath + "/" + nameOfUploadeFile;

            System.out.println("ha file save path :::" + fileSavePath);

            InputStream fileContent = null;

            fileContent = uploadedFile.getInputstream();

//            int scaledWidth = 1024;
//            int scaledHeight = 768;

//            double percent = 0.5;
            Files.copy(fileContent, new File("/Users/ayoubmahrouj/Desktop/photo/"+nameOfUploadeFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
//            ImageResizer.resize("/Users/ayoubmahrouj/Desktop/photo/"+nameOfUploadeFile, "/Users/ayoubmahrouj/Desktop/photo/"+nameOfUploadeFile, percent);
            System.out.println("32323");
//            Files.copy(fileContent, new File("F:\\photo\\"+nameOfUploadeFile).toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {

            JsfUtil.addErrorMessage(e, "Erreur Upload image");

            System.out.println("No update !!!!");

            e.printStackTrace();

        }

    }

}
