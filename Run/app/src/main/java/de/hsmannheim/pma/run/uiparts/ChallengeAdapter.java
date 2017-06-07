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

public class ChallengeAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public ChallengeAdapter(Activity activity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=activity;
        imageId=prgmImages;
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
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.challenge_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.challengeName);
        holder.img = (ImageView) rowView.findViewById(R.id.challengeImage);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
                onChallengesClick(result[position]);
            }
        });
        return rowView;
    }

    public void onChallengesClick(String chosenChallenge){
        //Intent myIntent = new Intent(context, ChallengeMapsActivity.class);
        //context.startActivity(myIntent);
    }

    /*Hier wie man eine Challenge aufruft:
    Intent intent = new Intent(context,ChallengeMapsActivity.class);
        //Entweder neue erstellen - route geht über routenId online
        Challenge challenge = new Challenge("First", "Schefflenz", "bisschen laufen","http://getraenke-letzguss.de/plugins/system/jat3/jat3/base-themes/default/images/favicon.ico",60,100,20);
        //Alternativ Liste über webconnection holen und dann durchgehen und anzeigen - für die Liste passt das eher
        List<Challenge> list = webConnection.getAllChallanges();

        intent.putExtra("challenge", challenge);
        intent.putExtra("credentials",new MyCredentials("aaron","muster"));

        //  startActivity(intent);
        startActivityForResult(intent,RESULT_ID_ROUTE_TRACKING);
     */
}