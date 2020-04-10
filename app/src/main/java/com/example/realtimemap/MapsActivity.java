package com.example.realtimemap;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.BubbleIconFactory;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String url = "https://api.openweathermap.org/data/2.5/weather?q=Almaty&appid=42a4fd234571acd3ad844f8ffd4f74f7";  // Url fo get data from
    private RestTemplate restTemplate;
    private GetAWebResourceTask dataFetcher;
    //public JSONObject CityWeather;
    private static final String API_KEY = "42a4fd234571acd3ad844f8ffd4f74f7";
    public String retunnumfromAsyncTask;

    private GoogleMap mMap;
    private HashMap<String, HashMap<String, Double>> cities = new HashMap<String, HashMap<String, Double>>() {{
        put("Almaty", new HashMap<String, Double>(){{ put("lat", 43.25); put("lon", 76.94); }});
//        put("Nur-Sultan", new HashMap<String, Double>(){{ put("lat", 51.18); put("lon", 71.45); }});
//        put("Shymkent", new HashMap<String, Double>(){{ put("lat", 42.29); put("lon", 69.59); }});
//        put("Karagandy", new HashMap<String, Double>(){{ put("lat", 49.50); put("lon", 73.10); }});
//        put("Kostanay", new HashMap<String, Double>(){{ put("lat", 53.17); put("lon", 63.58); }});
//        put("Atyrau", new HashMap<String, Double>(){{ put("lat", 47.12); put("lon", 51.88); }});
//        put("Semey", new HashMap<String, Double>(){{ put("lat", 50.41); put("lon", 80.23); }});

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


//    public String getWeatherType(String city){
//        ArrayList<Map<String, Object>> weather = null;
//        Map<String, Object> weather_0 = null;
//        weather = (ArrayList<Map<String, Object>>) getCityWeather().get("weather");
//        weather_0 = weather.get(0);
//
//        return (String) weather_0.get("main");
//    }
//
//    public String getWeatherDescription(String city){
//
//        ArrayList<Map<String, Object>> weather = null;
//        Map<String, Object> weather_0 = null;
//        weather = (ArrayList<Map<String, Object>>) getCityWeather().get("weather");
//        weather_0 = weather.get(0);
//
//        return (String) weather_0.get("description");
//    }
//
//    public Double getTemp(String city){
//        ArrayList<Map<String, Object>> weather = null;
//        Map<String, Double> main = null;
//        weather = (ArrayList<Map<String, Object>>) getCityWeather().get("weather");
//        main = (Map<String, Double>) getCityWeather().get("main");
//
//        return main.get("temp");
//    }


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

        // Set up RestTemplate() for making Web request
        restTemplate = new RestTemplate();

        // Set up a new object for asynchronous task
        new GetAWebResourceTask(restTemplate, mMap).execute(url);



//        for (HashMap.Entry<String, HashMap<String, Double>> entry : cities.entrySet()) {
//            String currCity = entry.getKey();
//            HashMap<String, Double> coords = entry.getValue();
//            LatLng MapCoords = new LatLng(coords.get("lat"), coords.get("lon"));
//
//            setUrl(currCity);
//            Log.e("CURRENT URL", getUrl());
//            getData(dataFetcher, getUrl());
//            Log.e("CURRENT CITYWEATHER", getCityWeather().toString());
//            String weatherType = getWeatherType(currCity);
//            Double temp = getTemp(currCity);
//            String result = currCity + ":\n" + weatherType + ", " + temp;
//
//
//            mMap.addMarker(new MarkerOptions().position(MapCoords).icon(BitmapDescriptorFactory
//                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(result));
//        }

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String city) {
        this.url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
    }



    private class GetAWebResourceTask extends AsyncTask<String, Void, String> {

        final public static String TAG = "GetAWebResourceTask";
        private RestTemplate restTemplate ;
        private GoogleMap mMap;
        public JSONObject CityWeather;


        // Constructor
        public GetAWebResourceTask(RestTemplate restTemplate, GoogleMap gmap) {
            this.restTemplate = restTemplate ;
            this.mMap = gmap;
        }

        public String getWeatherType(String city){
            ArrayList<Map<String, Object>> weather = null;
            Map<String, Object> weather_0 = null;
            weather = (ArrayList<Map<String, Object>>) CityWeather.get("weather");
            weather_0 = weather.get(0);

            return (String) weather_0.get("main");
        }

        public String getWeatherDescription(String city){

            ArrayList<Map<String, Object>> weather = null;
            Map<String, Object> weather_0 = null;
            weather = (ArrayList<Map<String, Object>>) CityWeather.get("weather");
            weather_0 = weather.get(0);

            return (String) weather_0.get("description");
        }

        public Double getTemp(String city){
            ArrayList<Map<String, Object>> weather = null;
            Map<String, Double> main = null;
            weather = (ArrayList<Map<String, Object>>) CityWeather.get("weather");
            main = (Map<String, Double>) CityWeather.get("main");

            return main.get("temp");
        }


        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {


            // get the string from params, which is an array
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String result = restTemplate.getForObject(params[0], String.class , "Android");

            // Result is passed into onPostExecute
            return result;
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {


            JSONParser parser = new JSONParser();
            try {
                CityWeather = (JSONObject) parser.parse(result);


                super.onPostExecute(result);


            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (HashMap.Entry<String, HashMap<String, Double>> entry : cities.entrySet()) {
                String currCity = entry.getKey();
                HashMap<String, Double> coords = entry.getValue();
                LatLng MapCoords = new LatLng(coords.get("lat"), coords.get("lon"));


                String weatherType = getWeatherType(currCity);
                Double temp = getTemp(currCity);
                String res = currCity + ":\n" + weatherType + ", " + temp;

                IconGenerator micongen = new IconGenerator(MapsActivity.this);
                Bitmap mBitmap = micongen.makeIcon(res);

                mMap.addMarker(new MarkerOptions().position(MapCoords).icon(BitmapDescriptorFactory
                        .fromBitmap(mBitmap)).title(currCity));
        }

        }
    }
}
