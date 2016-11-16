package computomovil.practica7cm;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

public class MainActivity extends AppCompatActivity {
   private DbxClientV2 client;
    private String email="",name="",tipe="",files="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client= DropboxClient.getClient(StringValues.ACCESS_TOKEN);
    }

    public void actualizar(View view) throws DbxException {
        String infoAccount="";
        if(client!=null){
            getUserAccount();
            infoAccount= email+"\n"+name+"\n"+tipe+"\n"+files;
            Toast.makeText(this,infoAccount,Toast.LENGTH_LONG);
            ((TextView)findViewById(R.id.dataAccount)).setText(infoAccount);

        }

    }
    public void downloadFile(View view){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Downloading");
        dialog.show();



    }

     protected void getUserAccount() {

        if (StringValues.ACCESS_TOKEN == null)return;

        new UserAccountTask(DropboxClient.getClient(StringValues.ACCESS_TOKEN), new UserAccountTask.TaskDelegate() {
            @Override
            public void onAccountReceived(FullAccount account) {
                //Print account's info
                email=account.getEmail();
                name= account.getName().getDisplayName();
                tipe= account.getAccountType().name();
                ListFolderResult result = null;

            }
            @Override
            public void onError(Exception error) {
                name="Error receiving account details.";
            }

        }

        ).execute();
    }


}
