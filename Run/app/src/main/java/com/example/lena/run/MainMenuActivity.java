package com.example.lena.run;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
    ListView lv;
    Context context;

    public static int[] prgmImages = {R.drawable.challenges, R.drawable.log, R.drawable.trophys};
    public static String[] prgmNameList = {"challenges", "log", "trophäen"};


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
}