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

import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Route;


public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback {
    final int MY_PERMISSIONS_REQUEST = 1;

    //TODO: Location von GPS + Network nehmen und die bessere nehmen
    protected Button startButton;
    protected TextView infoText;
    protected Route route;
    protected GoogleMap myMap;
    protected Polyline line;
    MyCredentials myCredentials;
    LocationManager locationManager;
    LocationListener locationListener;
    boolean tracking = false;
    private int count = 0;

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
                Toast.makeText(this, "Permission for Location is Required", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST);
            }
            return false;
        } else {
            Toast.makeText(this, "We Have Permissions", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (check_permissions()) {
            initPhase1();
        }
    }

    private void initPhase2() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
                startTimeUpdate();
                startButton.setText("Stop");
                startButton.setBackgroundColor(Color.RED);
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopTracking();
                    }
                });
            }
        });

    }


    //Real Start of App!
    private void initPhase1() {
        setContentView(R.layout.activity_maps);

        myCredentials = getIntent().getExtras().getParcelable("creds");


        startButton = (Button) findViewById(R.id.buttonStart);

        infoText = (TextView) findViewById(R.id.infoText);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    protected void startTimeUpdate() {
        //TODO: kann raus in eine separate Klasse!
        Thread refreshThread = new Thread(new Runnable() {
            TextView textViewTime = (TextView) findViewById(R.id.time);
            int time = 0;

            public void run() {
                while (true) {
                    time = time + 1;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            int sec = (int) (time % 60);
                            int min = (int) (time / 60);
                            textViewTime.setText(min + ":" + String.format("%02d", sec));
                        }
                    });
                }
            }
        });
        refreshThread.start();
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
        //map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(49.4031925, 9.2712083)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.4031925, 9.2712083), 7));
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-18.142, 178.431), 2));

        // Polylines are useful for marking paths and routes on the map.

        PolylineOptions p = new PolylineOptions().geodesic(true);

        p.color(Color.RED);
        line = map.addPolyline(p);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        Log.i(this.getClass().toString(), "Map ready");
        this.initPhase2();


    }

    protected void updateMap() {
        List<LatLng> points = route.getWayPoints();
        line.setPoints(points);
        LatLng lastPoint = points.get(points.size() - 1);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, 16));

    }

    private void handleNewLocation(Location location) {
        //Info Ausgabe auf dem Display
        Log.i(this.getClass().toString(), "handleNewLocation: " + location.getLatitude() + "/" + location.getLongitude() + "/" + location.getAltitude() + "üNN");
        count++;
        infoText.setText(count + "New Location " + location.getLatitude() + "/" + location.getLongitude() + "/" + location.getAltitude() + "üNN");

        //Add to Route Object
        LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());
        route.addWaypoint(new Date(), actualPosition, location.getAltitude());
        updateMap();

    }

    private void stopTracking() {
        tracking = false;
        startButton.setBackgroundColor(Color.GREEN);
        Intent resultData = new Intent();


        //Separat gepackt - da weder alles in route Serialisierbar noch parcable

        stopLocationUpdates();
        resultData.putExtra("route", route);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    protected void stopLocationUpdates() {
        //TODO: Irgendwie beenden?
        //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //locationManager.removeUpdates(locationListener);
    }

    private void startTracking() {
        tracking = true;
        route = new Route(new Date());

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

    public void onProfileButtonClick(View view) {
        Intent myIntent = new Intent(this, ProfileActivity.class);
        myIntent.putExtra("creds", myCredentials);
        startActivity(myIntent);
    }
}
