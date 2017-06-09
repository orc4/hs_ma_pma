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
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import de.hsmannheim.pma.run.model.Challenge;
import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Route;
import de.hsmannheim.pma.run.storage.WebConnectionFactory;
import de.hsmannheim.pma.run.utils.DistanceCalculator;

public class ChallengeMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final int MY_PERMISSIONS_REQUEST = 1;

    protected Button startButton;
    //protected Button stopButton;
    protected TextView infoText;
    protected Route routeTracked;
    protected Route routeToRun;
    protected Challenge challenge;
    protected Location lastLocation;
    protected GoogleMap myMap;
    protected Polyline lineTracked;
    protected Polyline lineToRun;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected boolean tracking = false;
    protected boolean startReached = false;
    protected GlobalApplication state;
    private int count = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            routeToRun = b.getParcelable("routeToRun");
            if (check_permissions()) {
                initPhase1();
            }
            Log.i(this.getClass().toString(), "handleMessage: routeToRun erfogreich geladen");
        }
    };

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
        state = ((GlobalApplication) getApplicationContext());
        challenge = getIntent().getExtras().getParcelable("challenge");
        final int routeId = challenge.getRouteId();

        Thread t = new Thread() {
            public void run() {
                routeToRun = new WebConnectionFactory().getWebConnection(state.getMyCredentials()).getRoute(routeId);

                final Message msg = new Message();
                final Bundle b = new Bundle();
                b.putParcelable("routeToRun", routeToRun);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };
        t.start();

        //routeToRun = new WebConnectionFactory().getWebConnection(cred).getRoute(routeId);
    }

    //Real Start of App!
    private void initPhase1() {
        setContentView(R.layout.activity_challange_maps);
        ImageView userPic = (ImageView) findViewById(R.id.user);
        userPic.setImageBitmap(state.getProfielImageBitmap());

        TextView headline = (TextView) findViewById(R.id.challengeHeadline);
        headline.setText(challenge.getName());

        startButton = (Button) findViewById(R.id.startButton);
        //stopButton = (Button) findViewById(R.id.buttonStop);
        infoText = (TextView) findViewById(R.id.infoText);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initPhase2() {
        startLocationUpdates();
        startButton.setText("Navigate to begin of Route");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng startPoint = routeToRun.getWayPoints().get(0);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + startPoint.latitude + "," + startPoint.longitude + "&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    protected void startTimeUpdate() {
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.4031925, 9.2712083), 7));
        PolylineOptions p = new PolylineOptions().geodesic(true);
        p.color(Color.RED);
        lineTracked = map.addPolyline(p);
        PolylineOptions p2 = new PolylineOptions().geodesic(true);
        p2.color(Color.BLUE);
        lineToRun = map.addPolyline(p2);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        Log.i(this.getClass().toString(), "Map ready");
        this.initPhase2();
    }

    protected void checkRouteToRun(Location location) {
        LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
        List<LatLng> list = routeToRun.getWayPoints();
        List<Date> listTimes = routeToRun.getWayPointsDates();
        Date startDateOld = routeToRun.getStartDate();
        Date startDateNew = routeTracked.getStartDate();
        Date now = new Date();


        int index = 0;
        int maxDiff = 50;
        boolean lastDeleted = true;
        boolean seconLastDeleted = true;
        while ((lastDeleted | seconLastDeleted) & index < list.size()) {
            // System.out.println("");
            // System.out.println("index "+ index + " last "+ lastDeleted + " sec " + seconLastDeleted);
            // System.out.println(list.toString());
            double distance = DistanceCalculator.distanceMeter(currentPos, list.get(index));
            Log.i(this.getClass().toString(), "checkRouteToRun: Distance: " + distance + "m");
            if (distance < maxDiff) {
                long timeDiff = (((now.getTime() - startDateNew.getTime()) - (listTimes.get(index).getTime() - startDateOld.getTime())) / 1000);
                if (timeDiff < 0) {
                    infoText.setTextColor(Color.GREEN);
                    infoText.setText("aktuell " + (-timeDiff) + " sec schneller als die Bestzeit");
                } else {
                    infoText.setTextColor(Color.RED);
                    infoText.setText("aktuell " + timeDiff + " sec langsamer als die Bestzeit");
                }

                list.remove(index);
                listTimes.remove(index);
                if (!lastDeleted && index > 0) {
                    list.remove(index - 1);
                    listTimes.remove(index - 1);
                    index--;
                }
                seconLastDeleted = lastDeleted;
                lastDeleted = true;
            } else {
                seconLastDeleted = lastDeleted;
                lastDeleted = false;
                index++;
            }
        }
        if (list.size() == 0) {
            stopTracking();
        }

    }

    protected void updateMap() {
        List<LatLng> pointsToRun = routeToRun.getWayPoints();
        lineToRun.setPoints(pointsToRun);

        if (routeTracked != null) {
            List<LatLng> pointsTracked = routeTracked.getWayPoints();
            lineTracked.setPoints(pointsTracked);
            LatLng lastPoint = pointsTracked.get(pointsTracked.size() - 1);
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, 16));
            Log.i(this.getClass().toString(), "updateMap: " + pointsToRun.size() + " pointsTo Run    " + pointsTracked.size() + " tracked");
        } else {
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointsToRun.get(0), 16));
        }

    }

    private void handleNewLocation(Location location) {
        if (!tracking) {
            LatLng currPos = new LatLng(location.getLatitude(), location.getLongitude());
            double distance = DistanceCalculator.distanceMeter(currPos, routeToRun.getWayPoints().get(0));
            if (distance < 100 && !startReached) {
                startReached = true;
                startButton.setEnabled(false);
                startButton.setText("Start");
                startButton.setBackgroundColor(Color.LTGRAY);
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startTracking();
                        startTimeUpdate();
                        if (lastLocation != null) {
                            handleNewLocation(lastLocation);
                        }
                        startButton.setBackgroundColor(Color.RED);
                        //FIXME: Raus - ist nur fürs Testen da!
                        startButton.setText("stop");
                        startButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stopTracking();
                            }
                        });
                    }
                });

                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        startButton.setEnabled(true);
                        startButton.setBackgroundColor(Color.GREEN);
                    }
                }.start();
            }
        }


        lastLocation = location;
        //Info Ausgabe auf dem Display
        Log.i(this.getClass().toString(), "handleNewLocation: " + location.getLatitude() + "/" + location.getLongitude() + "/" + location.getAltitude() + "üNN");
        count++;

        //Add to Route Object
        LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());
        if (tracking) {
            routeTracked.addWaypoint(new Date(), actualPosition, location.getAltitude());
            checkRouteToRun(location);
        }
        updateMap();
    }

    private void stopTracking() {
        tracking = false;
        startButton.setBackgroundColor(Color.GREEN);
        Intent resultData = new Intent();

        stopLocationUpdates();
        resultData.putExtra("route", routeTracked);
        resultData.putExtra("challenge", challenge);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    protected void stopLocationUpdates() {
        //TODO: Irgendwie beenden?
        //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //locationManager.removeUpdates(locationListener);
    }

    private void startLocationUpdates() {
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

    private void startTracking() {
        tracking = true;
        routeTracked = new Route(new Date());


    }

    public void challengeDone(View view) {
        Intent myIntent = new Intent(this, ChallengeDoneActivity.class);
        this.startActivity(myIntent);
    }
}
