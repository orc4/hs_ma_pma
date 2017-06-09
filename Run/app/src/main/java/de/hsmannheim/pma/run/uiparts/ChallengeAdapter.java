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

import java.util.List;

import de.hsmannheim.pma.run.ChallengeActivity;
import de.hsmannheim.pma.run.ChallengeMapsActivity;
import de.hsmannheim.pma.run.MainMenuActivity;
import de.hsmannheim.pma.run.R;
import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;

public class ChallengeAdapter extends BaseAdapter{

    List<Challenge> allAvailableChallenges;
    //String [] result;
    //String [] descriptions;
    Context context;
    Activity activity;
    int [] imageId;

    private static LayoutInflater inflater=null;
    public ChallengeAdapter(Activity activity, int[] prgmImages, List<Challenge> allAvailableChallenges) {
        // TODO Auto-generated constructor stub
        //result=prgmNameList;
        //descriptions = descriptionList;
        context=activity;
        imageId=prgmImages;
        this.allAvailableChallenges=allAvailableChallenges;
        this.activity=activity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return allAvailableChallenges.size();
    }

    @Override
    public Object getItem(int position) {
        return allAvailableChallenges.get(position);
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
        ImageView imgCup;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.challenge_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.challengeName);
        holder.tv2 = (TextView) rowView.findViewById(R.id.challengeDetails);
        holder.img = (ImageView) rowView.findViewById(R.id.challengeImage);
        holder.imgCup=(ImageView) rowView.findViewById(R.id.challengeDone);
        if(allAvailableChallenges.get(position).isChecked()){
            holder.imgCup.setImageResource(R.drawable.done);
        }else{
            holder.imgCup.setImageResource(R.drawable.undone);
        }

        holder.tv.setText(allAvailableChallenges.get(position).getName());
        holder.tv2.setText(allAvailableChallenges.get(position).getDescription());
        //FIXME: die noch aus dem web laden
        holder.img.setImageResource(imageId[(position%2)]);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onChallengesClick(allAvailableChallenges.get(position));
            }
        });
        return rowView;
    }

    public void onChallengesClick(Challenge chosenChallenge){

        //Toast.makeText(context, "klicked Name: "+chosenChallenge.getName() ,Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(context,ChallengeMapsActivity.class);
        intent.putExtra("challenge", chosenChallenge);

        activity.startActivityForResult(intent, ChallengeActivity.RESULT_ID_ROUTE_TRACKING_CHALLANGE);
    }
}