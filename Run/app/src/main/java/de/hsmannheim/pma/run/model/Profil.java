package de.hsmannheim.pma.run.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaron on 31.05.17.
 */

public class Profil implements Parcelable {
    public static final Creator<Profil> CREATOR =
            new Creator<Profil>() {
                public Profil createFromParcel(Parcel in) {
                    return new Profil(in);
                }

                public Profil[] newArray(int size) {
                    return new Profil[size];
                }
            };
    protected String username;
    protected String vorname;
    protected String nachname;
    protected String picUrl;
    protected int alter;
    protected String lieblingsort;

    public String toString(){
        return "username: "+username+ " vorname:"+vorname+" nachname: "+nachname+" picUrl: "+picUrl +
                " alter: "+alter+" Lieblingsort "+lieblingsort;
    }

    public Profil(String username, String vorname, String nachname, String picUrl, int alter, String lieblingsort) {
        this.username = username;
        this.vorname = vorname;
        this.nachname = nachname;
        this.picUrl = picUrl;
        this.alter = alter;
        this.lieblingsort = lieblingsort;
    }

    private Profil(Parcel in) {
        username = in.readString();
        vorname = in.readString();
        nachname = in.readString();
        picUrl = in.readString();
        alter = in.readInt();
        lieblingsort = in.readString();
    }

    public int describeContents() {
        return this.hashCode();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(username);
        out.writeString(vorname);
        out.writeString(nachname);
        out.writeString(picUrl);
        out.writeInt(alter);
        out.writeString(lieblingsort);
    }

    public String getUsername() {
        return username;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getAlter() {
        return alter;
    }

    public String getLieblingsort() {
        return lieblingsort;
    }
}
