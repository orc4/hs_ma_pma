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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageButton;
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
    protected GlobalApplication state;
    protected ArrayList<RouteAnalyse> allRouteAnalyses;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            final ArrayList<RouteAnalyse> allRouteAnalyses=b.getParcelableArrayList("routeAnalyses");
            handleRoutesReceive(allRouteAnalyses);
        }
    };

    private void handleRoutesReceive(ArrayList<RouteAnalyse> allRouteAnalyses) {
        this.allRouteAnalyses=allRouteAnalyses;
        lv = (ListView) findViewById(R.id.logList);
        Log.i(this.getClass().toString(), "handleRoutesReceive: "+allRouteAnalyses.size());
        lv.setAdapter(new LogAdapter(this, allRouteAnalyses, prgmImages));
        registerForContextMenu(lv);
    }

    public static int[] prgmImages = {R.drawable.tracking, R.drawable.buschkind, R.drawable.tracking};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = ((GlobalApplication) getApplicationContext());
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_log);
        ImageButton userPic = (ImageButton) findViewById(R.id.user);
        userPic.setImageBitmap(state.getProfielImageBitmap());

        final WebConnection webConnection = new WebConnectionImpl(state.getMyCredentials());


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
        AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
        RouteAnalyse ra = (RouteAnalyse) lv.getItemAtPosition(acmi.position);
        if (ra.getChallengeId()==-1||ra.getChallengeId()==null)
        {
            menu.add(0, v.getId(), 0, "challenge erstellen");
        }
        menu.add(0, v.getId(), 0, "details anzeigen");//groupId, itemId, order, title

    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        startActivity(myIntent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        RouteAnalyse ra =  allRouteAnalyses.get(info.position);
        switch (item.getTitle().toString()) {
            case ("challenge erstellen"):
                Intent myIntent = new Intent(context, CreateChallengeActivity.class);
                Log.i(this.getClass().toString(), "selected item pos=" + info.position +" show Details");
                myIntent.putExtra("routeAnalyse", ra);
                context.startActivity(myIntent);
                return true;

            case ("details anzeigen"):
                Intent myIntent2 = new Intent(context, RouteAnalyseActivity.class);
                Log.i(this.getClass().toString(), "selected item pos=" + info.position +" show Details");
                myIntent2.putExtra("routeAnalyse", ra);
                context.startActivity(myIntent2);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
