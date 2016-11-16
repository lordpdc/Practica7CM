package computomovil.practica7cm;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by raoman on 14/11/2016.
 */

public class UserAccountTask extends AsyncTask<Void, Void, FullAccount> {

    private DbxClientV2 dbxClient;
    private TaskDelegate  delegate;
    private Exception error;

    public interface TaskDelegate {
        void onAccountReceived(FullAccount account);
        void onError(Exception error);

    }

    UserAccountTask(DbxClientV2 dbxClient, TaskDelegate delegate){
        this.dbxClient =dbxClient;
        this.delegate = delegate;
    }

    /***********************************************************************************
    los parametros @nameFile, @pathLowerFile, @revFile se obtienen de la siguiente liga
    https://dropbox.github.io/dropbox-api-v2-explorer/#files_alpha/get_metadata
    hay una funcion para obtenerlo, pero que hueva.
     **********************************************************************************/
    private String nameFile="prueba.docx";
    private String pathLowerFile="/prueba.docx";
    private String revFile="e12f1b93c";
    @Override
    protected FullAccount doInBackground(Void... params) {
        try {
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path, nameFile);

            // Make sure the Downloads directory exists.
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    error = new RuntimeException("Unable to create directory: " + path);
                }
            } else if (!path.isDirectory()) {
                error = new IllegalStateException("Download path is not a directory: " + path);
                return null;
            }

            // Download the file.
            try (OutputStream outputStream = new FileOutputStream(file)) {
                dbxClient.files().download(pathLowerFile,revFile)
                        .download(outputStream);
            }
            System.out.println(path.toString()+"\n"+path.getAbsolutePath());


            //get the users FullAccount
            return dbxClient.users().getCurrentAccount();
        } catch (Exception e) {
            e.printStackTrace();
            error = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(FullAccount account) {
        super.onPostExecute(account);

        if (account != null && error == null){
            //User Account received successfully
            delegate.onAccountReceived(account);
        }
        else {
            // Something went wrong
            delegate.onError(error);
        }
    }
}