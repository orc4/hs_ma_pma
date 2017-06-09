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

import java.util.List;

import de.hsmannheim.pma.run.ChallengeActivity;
import de.hsmannheim.pma.run.ChallengeMapsActivity;
import de.hsmannheim.pma.run.R;
import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.storage.ConstantPics;

public class ChallengeAdapter extends BaseAdapter{

    List<Challenge> challengeList;
    //String [] result;
    //String [] descriptions;
    Context context;
    Activity activity;
    int [] imageId;

    private static LayoutInflater inflater=null;
    public ChallengeAdapter(Activity activity, int[] prgmImages, List<Challenge> challengeList) {
        //result=prgmNameList;
        //descriptions = descriptionList;
        context=activity;
        imageId=prgmImages;
        this.challengeList = challengeList;
        this.activity=activity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return challengeList.size();
    }

    @Override
    public Object getItem(int position) {
        return challengeList.get(position);
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
        ImageView imgCup;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.challenge_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.challengeName);
        holder.tv2 = (TextView) rowView.findViewById(R.id.challengeDetails);
        holder.img = (ImageView) rowView.findViewById(R.id.challengeImage);
        holder.imgCup=(ImageView) rowView.findViewById(R.id.challengeDone);
        if(challengeList.get(position).isChecked()){
            holder.imgCup.setImageResource(R.drawable.done);
        }else{
            holder.imgCup.setImageResource(R.drawable.undone);
        }

        holder.tv.setText(challengeList.get(position).getName());
        holder.tv2.setText(challengeList.get(position).getDescription());

        holder.img.setImageResource(ConstantPics.getPicId(challengeList.get(position).getPicUrl()));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onChallengesClick(challengeList.get(position));
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