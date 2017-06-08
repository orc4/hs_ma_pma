package de.hsmannheim.pma.run;

import android.app.Activity;
import android.os.Bundle;

public class InfoActivity extends Activity {
    protected GlobalApplication state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        state = ((GlobalApplication) getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
}
