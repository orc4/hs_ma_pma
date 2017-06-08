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

import java.util.ArrayList;

import de.hsmannheim.pma.run.ChallengeMapsActivity;
import de.hsmannheim.pma.run.R;
import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.RouteAnalyse;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;

public class LogAdapter extends BaseAdapter{
    ArrayList<RouteAnalyse> routeAnalyses;
    String [] result;
    String [] descriptions;
    Context context;
    int [] imageId;
    MyCredentials myCredentials;
    WebConnection webConnection;

    private static LayoutInflater inflater=null;
    public LogAdapter(Activity activity,  ArrayList<RouteAnalyse> routeAnalyses, String[] challengeOrTrackingList, String[] dateList, int[] prgmImages, MyCredentials cred) {
        this.routeAnalyses = routeAnalyses;
        result=challengeOrTrackingList;
        descriptions = dateList;
        context=activity;
        imageId=prgmImages;
        myCredentials = cred;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        webConnection= new WebConnectionImpl(myCredentials);
    }
    @Override
    public int getCount() {
        return routeAnalyses.size();
    }

    @Override
    public Object getItem(int position) {
        return routeAnalyses.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView tv2;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder = new Holder();
        final RouteAnalyse ra = routeAnalyses.get(position);
        View rowView;
        rowView = inflater.inflate(R.layout.log_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.challengeOrTrackingName);
        holder.tv2 = (TextView) rowView.findViewById(R.id.challengeOrTrackingDetails);
        holder.img = (ImageView) rowView.findViewById(R.id.challengeOrTrackingImage);
        holder.tv.setText(ra.getStartDate().toString());
        holder.tv2.setText(ra.getDistance()/1000+"km");
        if (ra.getChallengeId()==null || ra.getChallengeId()==-1){
            holder.img.setImageResource(R.drawable.tracking);
        }else{
            //TODO - hier eigentlich challange laden und alles dazu hinschreiben!
            holder.img.setImageResource(R.drawable.buschkind);
        }

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onChallengesClick(result[position]);
            }
        });
        return rowView;
    }

    public void onChallengesClick(String chosenChallenge){
        //addChallengeToPool()
    }
}