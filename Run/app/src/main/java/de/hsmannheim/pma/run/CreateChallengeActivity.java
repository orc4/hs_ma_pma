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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    protected RadioButton r1, r2, r3;

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
        ImageButton userPic = (ImageButton) findViewById(R.id.user);
        userPic.setImageBitmap(state.getProfielImageBitmap());
        textChallangeName = (EditText) findViewById(R.id.textChallangeName);
        textViertel = (EditText) findViewById(R.id.textViertel);
        textDescription = (EditText) findViewById(R.id.textDescription);
        textDone = (EditText) findViewById(R.id.textDone);
        createButton = (Button) findViewById(R.id.createButton);

        r1 = (RadioButton) findViewById(R.id.firstImageButton);
        r2 = (RadioButton) findViewById(R.id.secondImageButton);
        r3 = (RadioButton) findViewById(R.id.thirdImageButton);

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
        String picUrl = "shoes";
        if(r1.isChecked())  picUrl = "shoes";
        if(r2.isChecked())  picUrl = "fernsehturm";
        if(r3.isChecked())  picUrl = "street";

        final Challenge c = new Challenge(name,viertel,picUrl,(int)routeAnalyse.getTimeInSeconds(),routeAnalyse.getDistance().intValue(),routeAnalyse.getRouteId(),done);

        Thread t = new Thread() {
            public void run() {
                new WebConnectionImpl(state.getMyCredentials()).addChallange(c);
                handler.sendEmptyMessage(0);
            }
        };
        t.start();
    }

    public void clickImage1(View view){
        RadioGroup rg = (RadioGroup) findViewById(R.id.imageGroup);
        rg.check(R.id.firstImageButton);
    }
    public void clickImage2(View view){
        RadioGroup rg = (RadioGroup) findViewById(R.id.imageGroup);
        rg.check(R.id.secondImageButton);
    }
    public void clickImage3(View view){
        RadioGroup rg = (RadioGroup) findViewById(R.id.imageGroup);
        rg.check(R.id.thirdImageButton);
    }

}
