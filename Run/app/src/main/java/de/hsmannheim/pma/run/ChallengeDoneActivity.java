package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.hsmannheim.pma.run.MainMenuActivity;
import de.hsmannheim.pma.run.R;
import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.RouteAnalyse;

public class ChallengeDoneActivity extends Activity {
protected MyCredentials myCredentials;
    protected Challenge challenge;
    protected RouteAnalyse ra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_done);
        myCredentials = getIntent().getExtras().getParcelable("creds");
        challenge = getIntent().getExtras().getParcelable("challenge");
        ra = getIntent().getExtras().getParcelable("routeAnalyse");

        TextView challengeHeadLine = (TextView) findViewById(R.id.challengeHeadline);
        TextView yourTime = (TextView) findViewById(R.id.yourTime);
        TextView finishText = (TextView) findViewById(R.id.finishText);

        challengeHeadLine.setText(challenge.getName());
        int sec = (int) (ra.getTimeInSeconds()%60);
        int min = (int) (ra.getTimeInSeconds()/60);
        yourTime.setText(String.format("%02d", min)+":"+String.format("%02d", sec));
        finishText.setText(challenge.getFinishText());
    }

    public void onMainMenuReturn(View view){
        //Intent myIntent = new Intent(this, MainMenuActivity.class);
        //startActivity(myIntent);
        finish();
    }
}
