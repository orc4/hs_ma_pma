package de.hsmannheim.pma.run;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.uiparts.TypefaceUtil;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LogInActivity extends FragmentActivity {
    SplashFragment splashFragment;
    LogInFragment logInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        splashFragment = new SplashFragment();
        logInFragment = new LogInFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, splashFragment);
        fragmentTransaction.commit();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/AdventPro-Regular.ttf");

        new CountDownTimer(3000,1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 =
                        fragmentManager.beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container, logInFragment);
                fragmentTransaction2.commit();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }.start();
    }

    public void onLogInClick(View view) {
        Intent myIntent = new Intent(this, MainMenuActivity.class);
        MyCredentials myCredentials = new MyCredentials("aaron","muster");
        myIntent.putExtra("creds", myCredentials);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }
}