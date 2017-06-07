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

import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;
import de.hsmannheim.pma.run.uiparts.LogAdapter;

public class LogActivity extends Activity {
    ListView lv;
    Context context;
    protected MyCredentials myCredentials;

    public static int[] prgmImages = {R.drawable.tracking, R.drawable.buschkind, R.drawable.tracking};
    public static String[] challengeNameOrTrackingArray = {"Tracking", "Quadratekid", "Tracking"};
    public static String[] dateInformation = {"07.06.17, 15:00", "07.06.17, 14:00", "07.06.17, 13:00"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_log);

        myCredentials = getIntent().getExtras().getParcelable("creds");
        Toast.makeText(this, myCredentials.getUsername().toString(),Toast.LENGTH_LONG).show();
        WebConnection webConnection = new WebConnectionImpl(myCredentials);

        /*List<Challenge> allRuns = webConnection.getAllRuns();
        List<String> runNames = new ArrayList<String>();
        List<String> runDescriptions = new ArrayList<String>();

        for(int i = 0; i<allRuns.size(); i++)
        {
            runNames.add(allRuns.get(i).getName());
            runDescriptions.add(allRuns.get(i).getDescription());
        }
        challengeNameOrTrackingArray = new String[allRuns.size()];
        dateInformation = new String[allRuns.size()];
        runNames.toArray(challengeNameOrTrackingArray);
        runDescriptions.toArray(dateInformation);*/

        context = this;
        lv = (ListView) findViewById(R.id.logList);
        lv.setAdapter(new LogAdapter(this, challengeNameOrTrackingArray, dateInformation, prgmImages, myCredentials));
    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        myIntent.putExtra("creds", myCredentials);
        startActivity(myIntent);
    }
}
