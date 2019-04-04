package main.java.Home.CustomerPane.GoogleDrive;


import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static main.java.Home.CustomerPane.GoogleDrive.GoogleDriveUtils.getDriveService;

public class GetSubFoldersByName {

    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleSubFolderByName(String googleFolderIdParent, String subFolderName)
            throws IOException {

        Drive driveService = getDriveService();

        String pageToken = null;
        List<File> list = new ArrayList<File>();

        String query = null;
        if (googleFolderIdParent == null) {
            query = " name = '" + subFolderName + "' " //
                    + " and mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " name = '" + subFolderName + "' " //
                    + " and mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleRootFoldersByName(String subFolderName) throws IOException {
        return getGoogleSubFolderByName(null,subFolderName);
    }

    public static void main(String[] args) throws IOException {

        List<File> rootGoogleFolders = getGoogleRootFoldersByName("WarrantySystem");
        for (File folder : rootGoogleFolders) {

            System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
        }
    }

}