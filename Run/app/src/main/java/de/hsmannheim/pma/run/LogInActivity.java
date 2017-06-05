package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LogInActivity extends Activity {
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_log_in);
    }

    public void onButtonNextActivityClick(final View view){
        Intent myIntent = new Intent(this, MainMenuActivity.class);
        startActivity(myIntent);
    }
}
