package de.hsmannheim.pma.run.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

import de.hsmannheim.pma.run.utils.DistanceCalculator;


/**
 * Created by aaron on 30.05.17.
 */

public class RouteAnalyse implements Parcelable {

    private Route route;

    private double distance;
    private double paceMinPerKm;
    private double speedKmh;
    private double meterUp;
    private double meterDown;
    private long time;
    private int routeId;
    private int challengeId;
    private String username;
    private Date startDate;



    public RouteAnalyse(Route route){
        this.route = route;
        routeId=-1;
        challengeId=-1;
        startDate = route.getStartDate();
    }

    public static final Creator<RouteAnalyse> CREATOR =
            new Creator<RouteAnalyse>() {
                public RouteAnalyse createFromParcel(Parcel in) {
                    return new RouteAnalyse(in);
                }

                public RouteAnalyse[] newArray(int size) {
                    return new RouteAnalyse[size];
                }
            };

    private RouteAnalyse(Parcel in) {
        distance=in.readDouble();
        paceMinPerKm=in.readDouble();
        speedKmh=in.readDouble();
        meterUp=in.readDouble();
        meterDown=in.readDouble();
        time=in.readLong();
        routeId=in.readInt();
        challengeId=in.readInt();
        username=in.readString();
        startDate = (Date) in.readSerializable();

    }

    public void writeToParcel(Parcel out, int flags) {
       out.writeDouble(distance);
        out.writeDouble(paceMinPerKm);
        out.writeDouble(speedKmh);
        out.writeDouble(meterUp);
        out.writeDouble(meterDown);
        out.writeLong(time);
        out.writeInt(routeId);
        out.writeInt(challengeId);
        out.writeString(username);
        out.writeSerializable(startDate);
    }
    public int describeContents() {
        return this.hashCode();
    }

    public Double getPaceMinPerKm() {
        return paceMinPerKm;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Double getSpeedKmh() {
        return speedKmh;
    }

    public long getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public void setChallengeId(int challangeId) {
        this.challengeId = challangeId;
    }

    public void analyseAll(){
        analyseDistance();
        analyseHigh();
        analyseTime();
    }
    public void analyseDistance(){
        distance=0d;
        boolean first=true;
        LatLng lastPoint=null;
        for (LatLng point: route.getWayPoints()) {
            if(first){
                first=false;
            }else{
                distance=distance+ DistanceCalculator.distanceMeter(lastPoint,point);
            }
            lastPoint=point;
        }
    }
    public void analysePaceMinPerKm(){

    }
    public void analysespeedKmh(){

    }
    public void analyseHigh(){
        meterDown=0d;
        meterUp=0d;
        boolean first=true;
        double lastHigh=0d;
        for (Double high: route.getWayPointsAltitude()) {
            if(first){
                first=false;
            }else{
                double diff = high-lastHigh;
                if(diff>0){
                    meterUp += diff;
                }else{
                    meterDown += diff;
                }

            }
            lastHigh=high;
        }

    }
    public void analyseTime(){
        List<Date> points = route.getWayPointsDates();
        Date end = points.get(points.size()-1);
        time = end.getTime()-route.getStartDate().getTime();
    }

    public Double getDistance() {
        return distance;
    }
    public long getTimeInSeconds(){
        return time/1000;
    }

    public Double getMeterDown() {
        return meterDown;
    }

    public Double getMeterUp() {
        return meterUp;
    }
}
