package de.hsmannheim.pma.run.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaron on 30.05.17.
 */

public class Challenge implements Parcelable {
    public static final Creator<Challenge> CREATOR =
            new Creator<Challenge>() {
                public Challenge createFromParcel(Parcel in) {
                    return new Challenge(in);
                }

                public Challenge[] newArray(int size) {
                    return new Challenge[size];
                }
            };
    protected final String name;
    protected final String location;
    protected final String description;
    protected final String picUrl;
    protected final int durationInSec;
    protected final int lengthInM;
    protected final int routeId;
    protected final int id;
    protected boolean checked;

    private Challenge(Parcel in) {
        name = in.readString();
        location = in.readString();
        description = in.readString();
        picUrl = in.readString();
        durationInSec = in.readInt();
        lengthInM = in.readInt();
        routeId = in.readInt();
        id= in.readInt();
        checked  = (in.readInt() == 0) ? false : true;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(location);
        out.writeString(description);
        out.writeString(picUrl);
        out.writeInt(durationInSec);
        out.writeInt(lengthInM);
        out.writeInt(routeId);
        out.writeInt(id);
        out.writeInt(checked ? 1 : 0);
    }

    public int describeContents() {
        return this.hashCode();
    }

    public Challenge(String name, String location, String description, String picUrl, int durationInSec, int lengthInM, int routeId){
        this.name = name;
        this.location=location;
        this.description=description;
        this.picUrl = picUrl;
        this.durationInSec=durationInSec;
        this.lengthInM=lengthInM;
        this.routeId=routeId;
        this.id=0;
        this.checked=false;

    }


    public boolean isChecked() {
        return checked;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getDurationInSec() {
        return durationInSec;
    }

    public int getLengthInM() {
        return lengthInM;
    }

    public int getRouteId() {
        return routeId;
    }
}
