package de.hsmannheim.pma.run.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaron on 31.05.17.
 */

public class MyCredentials implements Parcelable{
    private final String username;
    private final String password;
    public MyCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static final Creator<MyCredentials> CREATOR =
            new Creator<MyCredentials>() {
                public MyCredentials createFromParcel(Parcel in) {
                    return new MyCredentials(in);
                }

                public MyCredentials[] newArray(int size) {
                    return new MyCredentials[size];
                }
            };

    private MyCredentials(Parcel in) {
       username = in.readString();
       password= in.readString();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(username);
        out.writeString(password);
    }

    public int describeContents() {
        return this.hashCode();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
