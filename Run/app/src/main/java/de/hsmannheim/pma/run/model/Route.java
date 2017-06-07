package de.hsmannheim.pma.run.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aaron on 30.05.17.
 * Simpler Datentyp
 */

public class Route implements Parcelable {
    private ArrayList<LatLng> wayPoints=new ArrayList<>();
    private ArrayList<Double> wayPointsAltitude=new ArrayList<>();
    private ArrayList<Date> wayPointsDates=new ArrayList<>();
    private Date startDate;
    private String createUser;

    public static final Creator<Route> CREATOR =
            new Creator<Route>() {
                public Route createFromParcel(Parcel in) {
                    return new Route(in);
                }

                public Route[] newArray(int size) {
                    return new Route[size];
                }
            };

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    private Route(Parcel in) {
        in.readList(wayPoints, LatLng.class.getClassLoader());
        in.readList(wayPointsAltitude, Double.class.getClassLoader());
        in.readList(wayPointsDates, Date.class.getClassLoader());
        startDate = (Date) in.readSerializable();
        createUser=in.readString();

    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeList(wayPoints);
        out.writeList(wayPointsAltitude);
        out.writeList(wayPointsDates);
        out.writeSerializable(startDate);
        out.writeString(createUser);
    }
    public Route(Date startDate){
        this.startDate=startDate;

    }
    public void addWaypoint(Date time, LatLng pos, Double altitude){
        this.wayPoints.add(pos);
        this.wayPointsDates.add(time);
        this.wayPointsAltitude.add(altitude);

    }
    public List<LatLng> getWayPoints(){
        return wayPoints;
    }

    public List<Double> getWayPointsAltitude() {
        return wayPointsAltitude;
    }

    public List<Date> getWayPointsDates() {
        return wayPointsDates;
    }

    public Date getStartDate() {
        return startDate;
    }
    public int describeContents() {
        return this.hashCode();
    }

}
