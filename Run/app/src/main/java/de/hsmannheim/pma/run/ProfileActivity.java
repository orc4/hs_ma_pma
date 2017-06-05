package de.hsmannheim.pma.run;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_profile);
    }
}
