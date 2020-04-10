package com.example.realtimemap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<String, HashMap<String, Double>> cities = new HashMap<String, HashMap<String, Double>>() {{
        put("Almaty", new HashMap<String, Double>(){{ put("lat", 43.25); put("lon", 76.94); }});
        put("Nur-Sultan", new HashMap<String, Double>(){{ put("lat", 51.18); put("lon", 71.45); }});
        put("Shymkent", new HashMap<String, Double>(){{ put("lat", 42.29); put("lon", 69.59); }});
        put("Karagandy", new HashMap<String, Double>(){{ put("lat", 49.50); put("lon", 73.10); }});
        put("Kostanay", new HashMap<String, Double>(){{ put("lat", 53.17); put("lon", 63.58); }});
        put("Atyrau", new HashMap<String, Double>(){{ put("lat", 47.12); put("lon", 51.88); }});
        put("Semey", new HashMap<String, Double>(){{ put("lat", 50.41); put("lon", 80.23); }});

    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.e("MAP of THE CITIES", cities.toString());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (HashMap.Entry<String, HashMap<String, Double>> entry : cities.entrySet()) {
            String currCity = entry.getKey();
            HashMap<String, Double> coords = entry.getValue();

            LatLng MapCoords = new LatLng(coords.get("lat"), coords.get("lon"));
            mMap.addMarker(new MarkerOptions().position(MapCoords).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Marker in " + currCity));
        }

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
