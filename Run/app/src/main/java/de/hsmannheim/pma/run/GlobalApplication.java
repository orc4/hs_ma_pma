package de.hsmannheim.pma.run;

import android.app.Application;
import android.graphics.Bitmap;

import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Profil;

/**
 * Created by aaron on 08.06.17.
 */

public class GlobalApplication extends Application {
    protected MyCredentials myCredentials;
    protected Profil myProfile;
    protected Bitmap profielImageBitmap;

    public MyCredentials getMyCredentials() {
        return myCredentials;
    }

    public void setMyCredentials(MyCredentials myCredentials) {
        this.myCredentials = myCredentials;
    }

    public Profil getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(Profil myProfile) {
        this.myProfile = myProfile;
    }

    public Bitmap getProfielImageBitmap() {
        return profielImageBitmap;
    }

    public void setProfielImageBitmap(Bitmap profielImageBitmap) {
        this.profielImageBitmap = profielImageBitmap;
    }
}


