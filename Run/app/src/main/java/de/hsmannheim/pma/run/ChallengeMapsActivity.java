package de.hsmannheim.pma.run;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;
import java.util.List;

import de.hsmannheim.pma.run.model.*;
import de.hsmannheim.pma.run.storage.*;
import de.hsmannheim.pma.run.utils.*;

public class ChallengeMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final int MY_PERMISSIONS_REQUEST = 1;

    protected Button startButton;
    protected Button stopButton;
    protected TextView infoText;
    protected Route routeTracked;
    protected Route routeToRun;
    protected Challenge challenge;


    LocationManager locationManager;
    LocationListener locationListener;

    private int count = 0;
    boolean tracking = false;
    protected GoogleMap myMap;
    protected Polyline lineTracked;
    protected Polyline lineToRun;

    private boolean check_permissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                Toast.makeText(this, "Permission for Location is Required", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST);
            }
            return false;
        } else {
            Toast.makeText(this, "We Have Permissions", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        challenge = getIntent().getExtras().getParcelable("challenge");
        MyCredentials cred = getIntent().getExtras().getParcelable("credentials");
        int routeId = challenge.getRouteId();
        routeToRun = new WebConnectionFactory().getWebConnection(cred).getRoute(routeId);

        if (check_permissions()) {
            initPhase1();
        }
    }

    //Real Start of App!
    private void initPhase1() {
        setContentView(R.layout.activity_challange_maps);

        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);
        infoText = (TextView) findViewById(R.id.infoText);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initPhase2() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
                startButton.setBackgroundColor(Color.RED);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTracking();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initPhase1();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.4031925, 9.2712083), 7));
        PolylineOptions p = new PolylineOptions().geodesic(true);
        p.color(Color.RED);
        lineTracked = map.addPolyline(p);
        PolylineOptions p2 = new PolylineOptions().geodesic(true);
        p2.color(Color.BLUE);
        lineToRun = map.addPolyline(p2);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        Log.i(this.getClass().toString(), "Map ready");
        this.initPhase2();


    }

    protected void checkRouteToRun(Location location){
        LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
        List<LatLng> list = routeToRun.getWayPoints();


        int index = 0;
        int maxDiff=50;
        boolean lastDeleted=true;
        boolean seconLastDeleted=true;
        while((lastDeleted | seconLastDeleted)&index<list.size()){
           // System.out.println("");
           // System.out.println("index "+ index + " last "+ lastDeleted + " sec " + seconLastDeleted);
           // System.out.println(list.toString());
            double distance= DistanceCalculator.distanceMeter(currentPos,list.get(index));
            Log.i(this.getClass().toString(), "checkRouteToRun: Distance: "+distance+"m");
            if(distance<maxDiff){
                list.remove(index);
                if(!lastDeleted&&index>0){
                    list.remove(index-1);
                    index--;
                }
                seconLastDeleted = lastDeleted;
                lastDeleted=true;
            }else{
                seconLastDeleted = lastDeleted;
                lastDeleted=false;
                index++;
            }
        }
    }

    protected void updateMap(){
        List<LatLng> pointsToRun = routeToRun.getWayPoints();
        lineToRun.setPoints(pointsToRun);

        List<LatLng> pointsTracked = routeTracked.getWayPoints();
        lineTracked.setPoints(pointsTracked);
        Log.i(this.getClass().toString(), "updateMap: " + pointsToRun.size() + " pointsTo Run    "+pointsTracked.size()+" tracked");

        LatLng lastPoint = pointsTracked.get(pointsTracked.size()-1);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, 16));

    }

    private void handleNewLocation(Location location) {
        //Info Ausgabe auf dem Display
        Log.i(this.getClass().toString(), "handleNewLocation: " + location.getLatitude() + "/" + location.getLongitude() + "/" + location.getAltitude() + "üNN");
        count++;
        infoText.setText(count + "New Location " + location.getLatitude() + "/" + location.getLongitude() + "/" + location.getAltitude() + "üNN");

        //Add to Route Object
        LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());
        routeTracked.addWaypoint(new Date(), actualPosition, location.getAltitude());
        checkRouteToRun(location);
        updateMap();
    }

    private void stopTracking(){
        tracking = false;
        startButton.setBackgroundColor(Color.GREEN);
        Intent resultData = new Intent();

        //Separat gepackt - da weder alles in route Serialisierbar noch parcable

        stopLocationUpdates();
        resultData.putExtra("route", routeTracked);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    protected void stopLocationUpdates() {
        //TODO: Irgendwie beenden?
        //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //locationManager.removeUpdates(locationListener);
    }
    private void startTracking(){
        tracking = true;
        routeTracked = new Route(new Date());

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                handleNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        Log.i(this.getClass().toString(), "Location Manager initialisiert");
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, locationListener);
    }
}
