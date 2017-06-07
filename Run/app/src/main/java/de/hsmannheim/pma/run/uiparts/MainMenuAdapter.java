package de.hsmannheim.pma.run.uiparts;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hsmannheim.pma.run.ChallengeActivity;
import de.hsmannheim.pma.run.LogActivity;
import de.hsmannheim.pma.run.R;
import de.hsmannheim.pma.run.ShowMapActivity;
import de.hsmannheim.pma.run.TrackingActivity;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Route;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;

public class MainMenuAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    MyCredentials myCredentials;
    private static LayoutInflater inflater=null;
    public MainMenuAdapter(Activity activity, String[] prgmNameList, int[] prgmImages, MyCredentials credentials) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=activity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myCredentials = credentials;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.menu_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.textView1);
        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result[position].contentEquals("challenges"))
                {
                    onChallengesClick();
                }
                if (result[position].contentEquals("log"))
                {
                    onLogClick();
                }
                if (result[position].contentEquals("tracking"))
                {
                    onTrackingClick();
                }

            }
        });
        return rowView;
    }

    public void onChallengesClick(){
        Intent myIntent = new Intent(context, ChallengeActivity.class);
        myIntent.putExtra("creds", myCredentials);
        context.startActivity(myIntent);
    }

    public void onLogClick(){
        Intent myIntent = new Intent(context, LogActivity.class);
        myIntent.putExtra("creds", myCredentials);
        context.startActivity(myIntent);
    }

    public void onTrackingClick(){
        Intent myIntent = new Intent(context, TrackingActivity.class);
        context.startActivity(myIntent);
        //Hier ein Beispiel wie das mit onActivity Result klappt
        /*
        Intent intent = new Intent(cnx,TrackingActivity.class);
        //  startActivity(intent);
        startActivityForResult(intent,RESULT_ID_ROUTE_TRACKING);

        Und hier wie man die antwort handelt:
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ID_ROUTE_TRACKING) {
            if (resultCode == RESULT_OK) {
                Route route2 = data.getParcelableExtra("route");

                RouteAnalyse routeAnal = new RouteAnalyse(route2);
                routeAnal.analyseDistance();
                routeAnal.analyseTime();
                routeAnal.analyseHigh();
                double distance = routeAnal.getDistance();
                long time = routeAnal.getTimeInSeconds();
                double up = routeAnal.getMeterUp();
                double down = routeAnal.getMeterDown();

                textInfo.setText("Distance "+ String.format( "%.4f", distance )+" Meter"
                        + time + "Secs " +
                        "up: "+up+"m " +
                        "down "+down+"m ");

                }
            }
        }
         */

    }
}