package de.hsmannheim.pma.run;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import de.hsmannheim.pma.run.model.MyCredentials;

public class ProfileActivity extends Activity {
    protected MyCredentials myCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_profile);

        myCredentials = getIntent().getExtras().getParcelable("creds");
        Toast.makeText(this, myCredentials.getUsername().toString(),Toast.LENGTH_LONG).show();
    }
}
