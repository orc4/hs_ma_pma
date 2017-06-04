package com.example.lena.run;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

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
}
