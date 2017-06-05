package de.hsmannheim.pma.run;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import de.hsmannheim.pma.run.uiparts.TypefaceUtil;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().toString(), "onCreate: create");

        setContentView(R.layout.activity_splash_screen);

        mContentView = findViewById(R.id.fullscreen_content_controls);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/AdventPro-Regular.ttf");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new CountDownTimer(1000,1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                changeToLogInActivity();
            }
        }.start();
    }


    public void changeToLogInActivity(){
        Intent myIntent = new Intent(this, LogInActivity.class);
        startActivity(myIntent);
    }
}
