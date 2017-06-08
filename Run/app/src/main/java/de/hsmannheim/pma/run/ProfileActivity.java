package de.hsmannheim.pma.run;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Profil;

public class ProfileActivity extends Activity {
    protected MyCredentials myCredentials;
    protected GlobalApplication state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = ((GlobalApplication) getApplicationContext());
        Log.i(this.getClass().toString(), "onCreate: create");
        setContentView(R.layout.activity_profile);

        myCredentials = getIntent().getExtras().getParcelable("creds");
        Toast.makeText(this, myCredentials.getUsername().toString(),Toast.LENGTH_LONG).show();
        TextView nameTextField = (TextView) findViewById(R.id.nameTextField);
        TextView userInfo = (TextView) findViewById(R.id.userInfo);
        ImageView profileImage;profileImage= (ImageView) findViewById(R.id.profileImage);

        final Profil profile = state.getMyProfile();

        String fullName = profile.getVorname() + " " +profile.getNachname();
        String strUserInfo= profile.getAlter() + "\n"+profile.getLieblingsort();

        nameTextField.setText(fullName);
        userInfo.setText(strUserInfo);
        profileImage.setImageBitmap(state.getProfielImageBitmap());

    }
}
