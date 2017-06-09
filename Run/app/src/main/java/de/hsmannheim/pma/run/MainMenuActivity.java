package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Route;
import de.hsmannheim.pma.run.model.RouteAnalyse;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;
import de.hsmannheim.pma.run.uiparts.MainMenuAdapter;

public class MainMenuActivity extends Activity {
    public final static int RESULT_ID_ROUTE_TRACKING = 1;
    public static int[] prgmImages = {R.drawable.challenges, R.drawable.log, R.drawable.tracking, R.drawable.info};
    public static String[] prgmNameList = {"challenges", "log", "tracking", "info"};
    protected WebConnection webConnection;
    protected ListView lv;
    protected Context context;
    protected GlobalApplication state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        state = ((GlobalApplication) getApplicationContext());

        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_main_menu);

        ImageButton userPic = (ImageButton) findViewById(R.id.user);
        userPic.setImageBitmap(state.getProfielImageBitmap());


        webConnection = new WebConnectionImpl(state.getMyCredentials());
        Toast.makeText(this, state.getMyCredentials().getUsername(), Toast.LENGTH_SHORT).show();

        context = this;
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new MainMenuAdapter(this, prgmNameList, prgmImages));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public void onProfileButtonClick(View view) {
        Intent myIntent = new Intent(this, ProfileActivity.class);
        startActivity(myIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ID_ROUTE_TRACKING) {
            if (data == null) {
                Log.i(this.getClass().toString(), "onActivityResult: Null object als Result bekommen!");
            } else {
                //Handle nachdem das Tracking beendet wurde
                final Route route = data.getParcelableExtra("route");
                final RouteAnalyse routeAnalyse = new RouteAnalyse(route);
                Log.i(this.getClass().toString(), "onActivityResult: Meder Down " + routeAnalyse.getMeterDown());

                //Alles hochladen
                Thread t = new Thread() {
                    public void run() {
                        int routeId = webConnection.addRoute(route);
                        routeAnalyse.setRouteId(routeId);
                        webConnection.addRouteAnalyse(routeAnalyse);
                        Log.i(this.getClass().toString(), "onActivityResult: Tracking upload Fertig!");
                    }
                };
                t.start();

                Intent myIntent = new Intent(context, RouteAnalyseActivity.class);
                myIntent.putExtra("route", route);
                myIntent.putExtra("routeAnalyse", routeAnalyse);
                context.startActivity(myIntent);

                Log.i(this.getClass().toString(), "onActivityResult: hier!");
            }

        }
    }
}