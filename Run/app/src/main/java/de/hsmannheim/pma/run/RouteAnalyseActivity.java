package de.hsmannheim.pma.run;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hsmannheim.pma.run.model.MyCredentials;
import de.hsmannheim.pma.run.model.Route;
import de.hsmannheim.pma.run.model.RouteAnalyse;
import de.hsmannheim.pma.run.storage.WebConnection;
import de.hsmannheim.pma.run.storage.WebConnectionImpl;

public class RouteAnalyseActivity extends FragmentActivity implements OnMapReadyCallback {
    protected Route route;
    protected RouteAnalyse routeAnalyse;
    protected GoogleMap myMap;
    protected Polyline line;
    protected WebConnection webConnection;
    protected boolean map_Ready = false;
    protected GlobalApplication state;

    protected TextView usernameText;
    protected TextView startDateText;
    protected TextView distanceText;
    protected TextView timeText;
    protected TextView paceText;
    protected TextView speedText;
    protected TextView meterUpText;
    protected TextView meterDownText;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            route = b.getParcelable("route");
            if (route != null) {
                updateMap();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = ((GlobalApplication) getApplicationContext());
        route = getIntent().getExtras().getParcelable("route");
        routeAnalyse = getIntent().getExtras().getParcelable("routeAnalyse");
        webConnection = new WebConnectionImpl(state.getMyCredentials());


        setContentView(R.layout.activity_route_analyse);
        usernameText = (TextView) findViewById(R.id.usernameText);
        startDateText = (TextView) findViewById(R.id.startDateText);
        distanceText = (TextView) findViewById(R.id.distanceText);
        timeText = (TextView) findViewById(R.id.timeText);
        paceText = (TextView) findViewById(R.id.paceText);
        speedText = (TextView) findViewById(R.id.speedText);
        meterUpText = (TextView) findViewById(R.id.meterUpText);
        meterDownText = (TextView) findViewById(R.id.meterDownText);

        if (route == null && routeAnalyse.getRouteId() != null && routeAnalyse.getRouteId() > 0) {
            Log.i(this.getClass().toString(), "onCreate: Route separat laden");
            Thread t = new Thread() {
                public void run() {
                    final Route route = webConnection.getRoute(routeAnalyse.getRouteId());
                    final Message msg = new Message();
                    final Bundle b = new Bundle();
                    b.putParcelable("route", route);
                    msg.setData(b);
                    handler.sendMessage(msg);
                }
            };
            t.start();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        refreshValues();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.470383, 8.482488), 12));
        PolylineOptions p = new PolylineOptions().geodesic(true);
        p.color(Color.RED);
        line = map.addPolyline(p);
        Log.i(this.getClass().toString(), "Map ready");
        map_Ready = true;
        if (route != null) {
            updateMap();
        }
    }

    protected void updateMap() {
        List<LatLng> points = route.getWayPoints();
        line.setPoints(points);
        LatLng lastPoint = points.get(points.size() - 1);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, 15));
    }


    protected void refreshValues() {
        //prepare Values
        String strUsername = "username: " + state.getMyCredentials().getUsername();
        SimpleDateFormat sdfmt = new SimpleDateFormat();
        sdfmt.applyPattern("E', 'dd. MMM yyyy HH:mm");
        String strStartDateText = sdfmt.format(routeAnalyse.getStartDate());
        String strDistanceText = String.format("%.2f", routeAnalyse.getDistance() / 1000) + "km";
        int sec = (int) (routeAnalyse.getTimeInSeconds() % 60);
        int min = (int) (routeAnalyse.getTimeInSeconds() / 60);
        String strTimeText = String.format("%02d", min) + ":" + String.format("%02d", sec);
        String strPaceText = String.format("%.2f", routeAnalyse.getPaceMinPerKm()) + "min/km";
        String strSpeedText = String.format("%.2f", routeAnalyse.getSpeedKmh()) + "km/h";
        String strMeterUpText = String.format("%.1f", routeAnalyse.getMeterUp()) + "m up";
        String strMeterDownText = String.format("%.1f", routeAnalyse.getMeterDown()) + "m down";


        //Set values
        usernameText.setText(strUsername);
        startDateText.setText(strStartDateText);
        distanceText.setText(strDistanceText);
        timeText.setText(strTimeText);
        paceText.setText(strPaceText);
        speedText.setText(strSpeedText);
        meterUpText.setText(strMeterUpText);
        meterDownText.setText(strMeterDownText);
    }
}
