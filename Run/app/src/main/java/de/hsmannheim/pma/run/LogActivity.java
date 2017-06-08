package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Route;
import de.hsmannheim.pma.run.model.RouteAnalyse;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;
import de.hsmannheim.pma.run.uiparts.LogAdapter;

public class LogActivity extends Activity {
    ListView lv;
    Context context;
    Activity logActivity;
    protected MyCredentials myCredentials;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            final ArrayList<RouteAnalyse> allRouteAnalyses=b.getParcelableArrayList("routeAnalyses");
            handleRoutesReceive(allRouteAnalyses);
        }
    };

    private void handleRoutesReceive(ArrayList<RouteAnalyse> allRouteAnalyses) {
        lv = (ListView) findViewById(R.id.logList);
        Log.i(this.getClass().toString(), "handleRoutesReceive: "+allRouteAnalyses.size());
        lv.setAdapter(new LogAdapter(this, allRouteAnalyses, challengeNameOrTrackingArray, dateInformation, prgmImages, myCredentials));
        registerForContextMenu(lv);
    }

    public static int[] prgmImages = {R.drawable.tracking, R.drawable.buschkind, R.drawable.tracking};
    public static String[] challengeNameOrTrackingArray = {"Tracking", "Quadratekid", "Tracking"};
    public static String[] dateInformation = {"07.06.17, 15:00", "07.06.17, 14:00", "07.06.17, 13:00"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_log);

        myCredentials = getIntent().getExtras().getParcelable("creds");
        Toast.makeText(this, myCredentials.getUsername().toString(),Toast.LENGTH_SHORT).show();
        final WebConnection webConnection = new WebConnectionImpl(myCredentials);


        Thread t = new Thread() {
            public void run() {
                final ArrayList<RouteAnalyse> myRouteAnalyses = webConnection.getMyRouteAnalyses();
                final Message msg = new Message();
                final Bundle b = new Bundle();
                b.putParcelableArrayList("routeAnalyses",myRouteAnalyses);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };
        t.start();
        context = this;
        logActivity=this;


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "details anzeigen");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "challenge erstellen");
    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        myIntent.putExtra("creds", myCredentials);
        startActivity(myIntent);
    }
}
