package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;
import de.hsmannheim.pma.run.uiparts.ChallengeAdapter;

public class ChallengeActivity extends Activity {
    ListView lv;
    Context context;
    protected MyCredentials myCredentials;

    public static int[] prgmImages = {R.drawable.buschkind, R.drawable.quadratekid};
    public static String[] challengeNameArray;
    public static String[] challengeDescriptionArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_challenge);

        myCredentials = getIntent().getExtras().getParcelable("creds");
        Toast.makeText(this, myCredentials.getUsername().toString(),Toast.LENGTH_LONG).show();
        WebConnection webConnection = new WebConnectionImpl(myCredentials);

        List<Challenge> allAvailableChallenges = webConnection.getAllChallanges();
        List<String> challengeNames = new ArrayList<String>();
        List<String> challengeDescriptions = new ArrayList<String>();

        for(int i = 0; i<allAvailableChallenges.size(); i++)
        {
            challengeNames.add(allAvailableChallenges.get(i).getName());
            challengeDescriptions.add(allAvailableChallenges.get(i).getDescription());
        }
        challengeNameArray = new String[allAvailableChallenges.size()];
        challengeDescriptionArray = new String[allAvailableChallenges.size()];
        challengeNames.toArray(challengeNameArray);
        challengeDescriptions.toArray(challengeDescriptionArray);

        context = this;
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new ChallengeAdapter(this, challengeNameArray, challengeDescriptionArray, prgmImages, myCredentials));
    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        myIntent.putExtra("creds", myCredentials);
        startActivity(myIntent);
    }
}
