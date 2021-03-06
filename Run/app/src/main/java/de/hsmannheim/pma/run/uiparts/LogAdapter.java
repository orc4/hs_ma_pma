package de.hsmannheim.pma.run.uiparts;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hsmannheim.pma.run.ChallengeMapsActivity;
import de.hsmannheim.pma.run.R;
import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.RouteAnalyse;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;

public class LogAdapter extends BaseAdapter{
    ArrayList<RouteAnalyse> routeAnalyses;
    Context context;
    int [] imageId;

    private static LayoutInflater inflater=null;
    public LogAdapter(Activity activity,  ArrayList<RouteAnalyse> routeAnalyses, int[] prgmImages) {
        this.routeAnalyses = routeAnalyses;
        Log.i(this.getClass().toString(), "LogAdapter: Elemente in Liste: "+routeAnalyses.size());
        context=activity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        Log.i(this.getClass().toString(), "getView: An der Position: "+position);
        final Holder holder = new Holder();
        final RouteAnalyse ra = routeAnalyses.get(position);
        View rowView;
        rowView = inflater.inflate(R.layout.log_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.challengeOrTrackingName);
        holder.tv2 = (TextView) rowView.findViewById(R.id.challengeOrTrackingDetails);
        holder.img = (ImageView) rowView.findViewById(R.id.challengeOrTrackingImage);
        SimpleDateFormat sdfmt = new SimpleDateFormat();
        sdfmt.applyPattern( "E', 'dd. MMM yyyy HH:mm" );
        holder.tv.setText(sdfmt.format(ra.getStartDate()));
        Log.i(this.getClass().toString(), "getView: startDate:"+sdfmt.format(ra.getStartDate()));
        holder.tv2.setText(String.format(Locale.GERMAN,"%.2f",ra.getDistance()/1000)+"km");
        if (ra.getChallengeId()==null || ra.getChallengeId()==-1){
            holder.img.setImageResource(R.drawable.tracking);
        }else{
            //TODO - hier eigentlich challange laden und alles dazu hinschreiben!
            holder.img.setImageResource(R.drawable.challenges);
        }

        rowView.setLongClickable(true); //otherwise contextmenu won't show!

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //onChallengesClick(result[position]);
            }
        });
        return rowView;
    }

    public void onChallengesClick(String chosenChallenge){
        //addChallengeToPool()
    }
}