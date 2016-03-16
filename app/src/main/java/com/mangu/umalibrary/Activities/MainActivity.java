package com.mangu.umalibrary.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.mangu.umalibrary.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Firebase mFirebaseRef;
    private Button button_general, button_ciencias, button_inf, button_economicas;
    private Map<String, String> library_association = new HashMap<>();
    private Map<String, String> coordinates_association = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fillLibraries();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://sweltering-inferno-4301.firebaseio.com/images");
        button_general = (Button) this.findViewById(R.id.button_general);
        button_ciencias = (Button) this.findViewById(R.id.button_ciencias);
        button_inf = (Button) this.findViewById(R.id.button_inf);
        button_economicas = (Button) this.findViewById(R.id.button_economicas);
        View.OnLongClickListener long_listener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                String lat_long = coordinates_association.get(((Button) v).getText().toString());
                Uri uri = Uri.parse("google.streetview:cbll=" +lat_long);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if(mapIntent.resolveActivity(getPackageManager()) != null) {
                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(mapIntent);
                }else {
                    return false;
                }
                return true;
            }
        };
        button_general.setOnLongClickListener(long_listener);
        button_ciencias.setOnLongClickListener(long_listener);
        button_inf.setOnLongClickListener(long_listener);
        button_economicas.setOnLongClickListener(long_listener);

    }

    private void fillLibraries() {
        library_association.put(this.getString(R.string.General),"0");
        library_association.put(this.getString(R.string.InfTel),"1");
        library_association.put(this.getString(R.string.Ciencias),"2");
        library_association.put(this.getString(R.string.Economicas), "3");
        coordinates_association.put(this.getString(R.string.General), "36.7175527,-4.4715621");
        coordinates_association.put(this.getString(R.string.InfTel), "36.7148423,-4.4732686");
        coordinates_association.put(this.getString(R.string.Ciencias), "36.7151232,-4.4757036");
        coordinates_association.put(this.getString(R.string.Economicas), "36.7287683,-4.4201392");

    }


    public void onClickLibrary(View view) {
        String button_text = ((Button)view).getText().toString();
        Intent intent = new Intent(this, LibraryInfo.class);
        if(button_text.equalsIgnoreCase(this.getString(R.string.General))) {
            intent.putExtra("Library",
                    getLibraryId(this.getString(R.string.General)));
        }else if(button_text.equalsIgnoreCase(this.getString(R.string.InfTel))) {
            intent.putExtra("Library",
                    getLibraryId(this.getString(R.string.InfTel)));
        }else if(button_text.equalsIgnoreCase(this.getString(R.string.Ciencias))) {
            intent.putExtra("Library",
                    getLibraryId(this.getString(R.string.Ciencias)));
        }else if(button_text.equalsIgnoreCase(this.getString(R.string.Economicas))) {
            intent.putExtra("Library",
                    getLibraryId(this.getString(R.string.Economicas)));
        }
        startActivity(intent);
    }

    public String getLibraryId(String str) {
        return library_association.get(str);
    }
}
