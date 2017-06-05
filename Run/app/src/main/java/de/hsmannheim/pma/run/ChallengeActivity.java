package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class ChallengeActivity extends Activity {
    ListView lv;
    Context context;

    public static int[] prgmImages = {R.drawable.buschkind, R.drawable.ehrensache};
    public static String[] prgmNameList = {"Buschkind", "Ehrensache"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        context = this;
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new ChallengeAdapter(this, prgmNameList, prgmImages));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public void onProfileButtonClick(View view){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        startActivity(myIntent);
    }
}
