package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
import de.hsmannheim.pma.run.uiparts.ChallengeAdapter;

public class ChallengeActivity extends Activity {
    public final static int RESULT_ID_ROUTE_TRACKING_CHALLANGE=1;
    protected ListView lv;
    protected Context context;
    protected Activity challengeActivity;
    protected WebConnection webConnection;
    protected GlobalApplication state;

    public static int[] prgmImages = {R.drawable.buschkind, R.drawable.quadratekid};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            final ArrayList<Challenge> allAvailableChallenges=b.getParcelableArrayList("challenges");
            handleChallengeReceive(allAvailableChallenges);
        }
    };

    protected void handleChallengeReceive(ArrayList<Challenge> allAvailableChallenges){
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new ChallengeAdapter(challengeActivity, prgmImages,allAvailableChallenges));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = ((GlobalApplication) getApplicationContext());
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_challenge);
        ImageButton userPic = (ImageButton) findViewById(R.id.user);
        userPic.setImageBitmap(state.getProfielImageBitmap());

        webConnection = new WebConnectionImpl(state.getMyCredentials());

        Thread t = new Thread() {
            public void run() {
                final ArrayList<Challenge> allAvailableChallenges = webConnection.getAllChallanges();
                final Message msg = new Message();
                final Bundle b = new Bundle();
                b.putParcelableArrayList("challenges",allAvailableChallenges);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };
        t.start();

        context = this;
        challengeActivity=this;

    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        startActivity(myIntent);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ID_ROUTE_TRACKING_CHALLANGE) {
            if(data==null){
                Log.i(this.getClass().toString(), "onActivityResult: Null received");
            }else{
                //Handle nachdem das Tracking beendet wurde
                final Route route = data.getParcelableExtra("route");
                final Challenge challenge = data.getParcelableExtra("challenge");
                final RouteAnalyse ra = new RouteAnalyse(route);
                ra.setChallengeId(challenge.getId());

                //Alles hochladen
                Thread t = new Thread() {
                    public void run() {
                        int routeId = webConnection.addRoute(route);
                        ra.setRouteId(routeId);
                        Log.i(this.getClass().toString(), "onActivityResult: routeAnalyse "+ra.toString());
                        webConnection.addRouteAnalyse(ra);
                        webConnection.setChallengeChecked(challenge);
                        Log.i(this.getClass().toString(), "onActivityResult: Tracking + Challenge upload Fertig!");
                    }
                };
                t.start();

                Intent myIntent = new Intent(context, RouteAnalyseActivity.class);
                myIntent.putExtra("route",route);
                myIntent.putExtra("routeAnalyse",ra);
                context.startActivity(myIntent);

                Intent intent = new Intent(context, ChallengeDoneActivity.class);
                intent.putExtra("challenge", challenge);
                intent.putExtra("routeAnalyse", ra);
                context.startActivity(intent);

                Log.i(this.getClass().toString(), "onActivityResult: hier!");
            }
        }
    }
}
