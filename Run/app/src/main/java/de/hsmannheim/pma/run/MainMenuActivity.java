package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;

import de.hsmannheim.pma.run.model.Route;
import de.hsmannheim.pma.run.model.RouteAnalyse;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;
import de.hsmannheim.pma.run.uiparts.MainMenuAdapter;

public class MainMenuActivity extends Activity {
    ListView lv;
    Context context;
    protected MyCredentials myCredentials;
    protected WebConnection webConnection;

    public final static int RESULT_ID_ROUTE_TRACKING=1;
    public static int[] prgmImages = {R.drawable.challenges, R.drawable.log, R.drawable.tracking, R.drawable.info};
    public static String[] prgmNameList = {"challenges", "log", "tracking", "info"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FIXME: Muss raus - nur zum umgehen von download problemen
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);


        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_main_menu);

        myCredentials = getIntent().getExtras().getParcelable("creds");
        webConnection = new WebConnectionImpl(myCredentials);
        Toast.makeText(this, myCredentials.getUsername().toString(),Toast.LENGTH_SHORT).show();

        context = this;
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new MainMenuAdapter(this, prgmNameList, prgmImages, myCredentials));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        myIntent.putExtra("creds", myCredentials);
        startActivity(myIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ID_ROUTE_TRACKING) {
            if(data==null){
                Log.i(this.getClass().toString(), "onActivityResult: Null object als Result bekommen!");
            }else{
                //Handle nachdem das Tracking beendet wurde
                final Route route = data.getParcelableExtra("route");
                final RouteAnalyse ra = new RouteAnalyse(route);
                ra.analyseAll();
                Log.i(this.getClass().toString(), "onActivityResult: Meder Down "+ra.getMeterDown());

                //Alles hochladen
                Thread t = new Thread() {
                    public void run() {
                        int routeId = webConnection.addRoute(route);
                        ra.setRouteId(routeId);
                        webConnection.addRouteAnalyse(ra);
                        Log.i(this.getClass().toString(), "onActivityResult: Tracking upload Fertig!");
                    }
                };
                t.start();

                Log.i(this.getClass().toString(), "onActivityResult: hier!");
            }

        }
    }
}