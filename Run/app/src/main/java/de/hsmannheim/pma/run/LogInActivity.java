package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Profil;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;
import de.hsmannheim.pma.run.uiparts.TypefaceUtil;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LogInActivity extends FragmentActivity {
    SplashFragment splashFragment;
    LogInFragment logInFragment;
    EditText password, username;
    Context context = this;
    Activity activity;
    MyCredentials myCredentials;
    protected GlobalApplication state;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            Integer value = b.getInt("KEY");

            if (value == 1) {
                state.setMyCredentials(myCredentials);
                Intent myIntent = new Intent(context, MainMenuActivity.class);
                //myIntent.putExtra("creds", myCredentials);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                Toast.makeText(LogInActivity.this,"Login Correct!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LogInActivity.this,"Wrong Credentials!",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = ((GlobalApplication) getApplicationContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        activity=this;

        splashFragment = new SplashFragment();
        logInFragment = new LogInFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, splashFragment);
        fragmentTransaction.commit();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/AdventPro-Regular.ttf");
        new CountDownTimer(2500, 1000) {
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
        password = (EditText) findViewById(R.id.passwordTextField);
        username = (EditText) findViewById(R.id.nameTextField);
        String myPassword = password.getText().toString().toLowerCase();
        String myUsername = username.getText().toString();

        //FIXME: only for debug!
        //myPassword = "muster";
        //myUsername = "aaron";
        myCredentials = new MyCredentials(myUsername, myPassword);
        final WebConnection webConnection = new WebConnectionImpl(myCredentials);
        Thread t = new Thread() {
            public void run() {
                final boolean result = webConnection.checkLogin();
                if(result==true){
                    Profil profil = webConnection.getMyProfile();
                    state.setMyProfile(profil);
                    URL newurl = null;
                    try {
                        newurl = new URL(profil.getPicUrl());
                        state.setProfielImageBitmap(BitmapFactory.decodeStream(newurl.openConnection().getInputStream()));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Profil lala = webConnection.getMyProfile();
                    //Log.i("lala", "run: "+lala.toString());

                }
                final Message msg = new Message();
                final Bundle b = new Bundle();
                int bool = (result) ? 1 : 0;
                b.putInt("KEY", bool);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };
        t.start();



    }
}