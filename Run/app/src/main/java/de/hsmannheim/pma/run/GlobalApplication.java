package de.hsmannheim.pma.run;

import android.app.Application;

import de.hsmannheim.pma.run.model.MyCredentials;

/**
 * Created by aaron on 08.06.17.
 */

public class GlobalApplication extends Application {
    protected MyCredentials myCredentials;

    public MyCredentials getMyCredentials() {
        return myCredentials;
    }

    public void setMyCredentials(MyCredentials myCredentials) {
        this.myCredentials = myCredentials;
    }
}


