package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainMenuActivity extends Activity {
    ListView lv;
    Context context;

    public static int[] prgmImages = {R.drawable.challenges, R.drawable.log, R.drawable.trophys ,R.drawable.tracking};
    public static String[] prgmNameList = {"challenges", "log", "trophäen", "tracking"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        context = this;
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new MainMenuAdapter(this, prgmNameList, prgmImages));
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