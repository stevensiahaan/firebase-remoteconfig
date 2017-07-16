package com.stevensiahaan.firebaseforceupdate;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(true)
        .build());

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("is_force_update",false);
        remoteConfig.setDefaults(defaults);

        final Task<Void> fetch = remoteConfig.fetch(0);
        fetch.addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    remoteConfig.activateFetched();
                    if(remoteConfig.getBoolean("is_force_update")) {
                        showDialogUpdate();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Fetch Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void showDialogUpdate() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, Update your app to the newest version.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Update", Toast.LENGTH_SHORT).show();;
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Close", Toast.LENGTH_SHORT).show();
                            }
                        }).create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
