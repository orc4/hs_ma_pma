package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.RouteAnalyse;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;

public class CreateChallengeActivity extends Activity {
    protected GlobalApplication state;

    protected RouteAnalyse routeAnalyse;
    protected EditText textChallangeName;
    protected EditText textViertel;
    protected EditText textDescription;
    protected EditText textDone;
    protected Button createButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = ((GlobalApplication) getApplicationContext());
        routeAnalyse = getIntent().getExtras().getParcelable("routeAnalyse");

        setContentView(R.layout.activity_create_challenge);
        textChallangeName = (EditText) findViewById(R.id.textChallangeName);
        textViertel = (EditText) findViewById(R.id.textViertel);
        textDescription = (EditText) findViewById(R.id.textDescription);
        textDone = (EditText) findViewById(R.id.textDone);
        createButton = (Button) findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreateChallange();
            }
        });

    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        startActivity(myIntent);
    }
    public void handleCreateChallange(){
        String name = textChallangeName.getText().toString();
        String viertel = textViertel.getText().toString();
        String description = textDescription.getText().toString();
        String done = textDone.getText().toString();

        String picUrl = "lala";

        final Challenge c = new Challenge(name,viertel,description,picUrl,(int)routeAnalyse.getTimeInSeconds(),routeAnalyse.getDistance().intValue(),routeAnalyse.getRouteId(),done);

        Thread t = new Thread() {
            public void run() {
                new WebConnectionImpl(state.getMyCredentials()).addChallange(c);
                handler.sendEmptyMessage(0);
            }
        };
        t.start();
    }

}
