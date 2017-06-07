package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.hsmannheim.pma.run.MainMenuActivity;
import de.hsmannheim.pma.run.R;

public class ChallengeDoneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_done);

        Bundle b = getIntent().getExtras();
        String chosenChallenge = "";
        if (b != null)
            chosenChallenge = b.getString("whichChallenge");

        TextView tv = (TextView) findViewById(R.id.challengeHeadline);
        tv.setText(chosenChallenge);
    }

    public void onMainMenuReturn(View view){
        Intent myIntent = new Intent(this, MainMenuActivity.class);
        startActivity(myIntent);
    }
}
