package de.hsmannheim.pma.run;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import de.hsmannheim.pma.run.model.Route;


public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    //TODO: Location von GPS + Network nehmen und die bessere nehmen

    final int MY_PERMISSIONS_REQUEST = 1;

    protected GoogleMap myMap;
    protected Polyline line;
    protected Route route;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        route = getIntent().getExtras().getParcelable("route");
        setContentView(R.layout.avtivity_show_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.4031925, 9.2712083), 7));
        PolylineOptions p = new PolylineOptions().geodesic(true);
        p.color(Color.RED);
        line = map.addPolyline(p);
        Log.i(this.getClass().toString(), "Map ready");
        this.updateMap();
    }

    protected void updateMap(){
        List<LatLng> points = route.getWayPoints();
        line.setPoints(points);
        LatLng lastPoint = points.get(points.size()-1);
        //TODO: Mov zu einem Punkt dazwischen - und zoom einigermaßen anpassen!!!
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, 16));
    }
}
