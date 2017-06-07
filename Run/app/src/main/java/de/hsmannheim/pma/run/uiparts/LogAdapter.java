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

import de.hsmannheim.pma.run.ChallengeMapsActivity;
import de.hsmannheim.pma.run.R;
import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;

public class LogAdapter extends BaseAdapter{
    String [] result;
    String [] descriptions;
    Context context;
    int [] imageId;
    MyCredentials myCredentials;

    private static LayoutInflater inflater=null;
    public LogAdapter(Activity activity, String[] challengeOrTrackingList, String[] dateList, int[] prgmImages, MyCredentials cred) {
        // TODO Auto-generated constructor stub
        result=challengeOrTrackingList;
        descriptions = dateList;
        context=activity;
        imageId=prgmImages;
        myCredentials = cred;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        TextView tv2;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.log_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.challengeOrTrackingName);
        holder.tv2 = (TextView) rowView.findViewById(R.id.challengeOrTrackingDetails);
        holder.img = (ImageView) rowView.findViewById(R.id.challengeOrTrackingImage);
        holder.tv.setText(result[position]);
        holder.tv2.setText(descriptions[position]);
        holder.img.setImageResource(imageId[position]);

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