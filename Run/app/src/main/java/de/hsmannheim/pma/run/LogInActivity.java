package de.hsmannheim.pma.run;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.hsmannheim.pma.run.model.MyCredentials;
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
    MyCredentials myCredentials;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            Integer value = b.getInt("KEY");

            if(value==1){
                Intent myIntent = new Intent(context, MainMenuActivity.class);
                myIntent.putExtra("creds", myCredentials);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }else{
                Toast.makeText(context,"login false", Toast.LENGTH_SHORT);
            }
        }
    };

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
        //new CountDownTimer(3000,1000)
        new CountDownTimer(1000,1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 =
                        fragmentManager.beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container, logInFragment);
                fragmentTransaction2.commit();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                 password = (EditText) findViewById(R.id.passwordTextField);
                 username = (EditText) findViewById(R.id.nameTextField);
            }
        }.start();
    }

    public void onLogInClick(View view) {
        password = (EditText) findViewById(R.id.passwordTextField);
        username = (EditText) findViewById(R.id.nameTextField);
        String myPassword = password.getText().toString().toLowerCase();
        String myUsername = username.getText().toString();

        //FIXME: only for debug!
        myPassword="muster"; myUsername="aaron";
        myCredentials = new MyCredentials(myUsername, myPassword);
        final WebConnection webConnection = new WebConnectionImpl(myCredentials);
        Thread t = new Thread() {
            public void run() {
                final boolean result = webConnection.checkLogin();
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