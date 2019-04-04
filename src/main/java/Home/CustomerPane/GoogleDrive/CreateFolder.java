package main.java.Home.CustomerPane.GoogleDrive;


import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static main.java.Home.CustomerPane.GoogleDrive.GoogleDriveUtils.getDriveService;

public class CreateFolder {

    public static final File createGoogleFolder(String folderIdParent, String folderName) throws IOException {

        File fileMetadata = new File();

        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        if (folderIdParent != null) {
            List<String> parents = Arrays.asList(folderIdParent);

            fileMetadata.setParents(parents);
        }
        Drive driveService = getDriveService();

        // Create a Folder.
        // Returns File object with id & name fields will be assigned values
        File file = driveService.files().create(fileMetadata).setFields("id, name").execute();

        return file;
    }

    public static void main(String[] args) throws IOException {

        // Create a Root Folder

        File folder1 = createGoogleFolder(null, "WarrantySystem");

        System.out.println("Created folder with id= "+ folder1.getId());
        System.out.println("                    name= "+ folder1.getName());

        System.out.println("Done!");
    }

}