package com.example.lena.run;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {
    Typeface custom_font;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        mContentView = findViewById(R.id.logInHeadline);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/AdventPro-Regular.ttf");
        Button b = (Button)findViewById(R.id.logInButton);
        b.setTypeface(custom_font);
        EditText e1 = (EditText)findViewById(R.id.nameTextField);
        e1.setTypeface(custom_font);
        EditText e2 = (EditText)findViewById(R.id.passwordTextField);
        e2.setTypeface(custom_font);
        TextView tv = (TextView)findViewById(R.id.logInHeadline);
        tv.setTypeface(custom_font);
        Button b2 = (Button)findViewById(R.id.registerButton);
        b2.setTypeface(custom_font);
    }

    public void onButtonNextActivityClick(final View view){
        Intent myIntent = new Intent(this, MainMenuActivity.class);
        startActivity(myIntent);
    }
}
