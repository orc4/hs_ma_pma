package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.hsmannheim.pma.run.model.MyCredentials;

public class CreateChallengeActivity extends Activity {
    protected GlobalApplication state;

    MyCredentials myCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = ((GlobalApplication) getApplicationContext());
        myCredentials = getIntent().getExtras().getParcelable("creds");
        setContentView(R.layout.activity_create_challenge);
    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        myIntent.putExtra("creds", myCredentials);
        startActivity(myIntent);
    }
}
