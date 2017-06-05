package de.hsmannheim.pma.run.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

import de.hsmannheim.pma.run.utils.DistanceCalculator;


/**
 * Created by aaron on 30.05.17.
 */

public class RouteAnalyse {
    private final Route route;

    private Double distance;
    private Double paceMinPerKm;
    private Double speedKmh;
    private Double meterUp;
    private Double meterDown;
    private long time;


    public RouteAnalyse(Route route){
        this.route = route;

    }

    public void analyseAll(){

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
