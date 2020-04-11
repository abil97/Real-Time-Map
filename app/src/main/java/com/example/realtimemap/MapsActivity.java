package com.example.realtimemap;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
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

    private GoogleMap mMap;
    private String url;                                      // Url fo get data from
    private RestTemplate restTemplate;                      // Some stuff from SpringFramework for making Web Request
    private GetAWebResourceTask dataFetcher;                // Some stuff for making async. task
    LatLng Astana = new LatLng(51.18, 71.45);       // This is default location, where camera on map will be initially looking

    private static final String API_KEY = "42a4fd234571acd3ad844f8ffd4f74f7";   // API Key is necessary for making a request.


    // The HashMap of cities with coordinates
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

        mMap = googleMap;           // Initialize google map

        /* Iterating through map of cities defined above, and making an Web API request for each city*/
        for (HashMap.Entry<String, HashMap<String, Double>> entry : cities.entrySet()) {

            String currCity = entry.getKey();   // Get current city name
            setUrl(currCity);                   // Set up the url for this specific city, for making API request


            restTemplate = new RestTemplate();                                      // Set up RestTemplate() for making Web request
            new GetAWebResourceTask(restTemplate, mMap, currCity).execute(url);     // Set up a new object for asynchronous task. Making a Web API Request
        }

        // Initially move camera to Astana, with Landmass/continent view
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Astana));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(5));

    }

    // Get the URL for web API request
    public String getUrl() {
        return url;
    }

    // SET the URL for web API request, for specific city
    public void setUrl(String city) {
        this.url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
    }


    // All Asynchronous stuff is happening here
    private class GetAWebResourceTask extends AsyncTask<String, Void, String> {

        private RestTemplate restTemplate;          // Spring Framework stuff for making web request
        private GoogleMap mMap;                     // Map
        private String currentCity;                 // Name of the currently fetched city
        public JSONObject CityWeather;              // Fetched city data in JSON format


        // Constructor
        public GetAWebResourceTask(RestTemplate restTemplate, GoogleMap gmap, String city) {
            this.restTemplate = restTemplate ;
            this.mMap = gmap;
            this.currentCity = city;
        }

        // Json parser. Returns weather type - sunny, rain, cloud, etc.
        public String getWeatherType(String city){
            ArrayList<Map<String, Object>> weather = (ArrayList<Map<String, Object>>) CityWeather.get("weather");;
            Map<String, Object> weather_0 = weather.get(0);

            return (String) weather_0.get("main");
        }

        // Json parser. Returns a description of a weather
        public String getWeatherDescription(String city){

            ArrayList<Map<String, Object>> weather = (ArrayList<Map<String, Object>>) CityWeather.get("weather");
            Map<String, Object> weather_0 =  weather.get(0);

            return (String) weather_0.get("description");
        }

        // Json parser. Returns temperature in a given city
        public Double getTemp(String city){
            ArrayList<Map<String, Object>> weather = (ArrayList<Map<String, Object>>) CityWeather.get("weather");
            Map<String, Double> main = (Map<String, Double>) CityWeather.get("main");

            // return temp. in Celsius
            return main.get("temp") - 273.15;
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

            JSONParser parser = new JSONParser();                       // Set up JSON Parser
            try {
                CityWeather = (JSONObject) parser.parse(result);        // Parse result (String) into JSON Object
                super.onPostExecute(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }

                HashMap<String, Double> coords = cities.get(currentCity);               // Get coordinates of current city
                LatLng MapCoords = new LatLng(coords.get("lat"), coords.get("lon"));    // Initialize a new Google map LatLng object with coords


                String weatherType = getWeatherType(currentCity);                           // Get weather type
                Double temp = getTemp(currentCity);                                         // Get temperature and round up to 1 decimal place
                String roundedTemp = String.format("%.1f", temp);
                String res = currentCity + "\n" + weatherType + ", " + roundedTemp + "°";   // Final String of data (weather type, temp) to be displayed on map


                // Generate a new GoogleMAp icon with Android Utility Library
                // https://developers.google.com/maps/documentation/android-sdk/utility
                IconGenerator micongen = new IconGenerator(MapsActivity.this);
                Bitmap mBitmap = micongen.makeIcon(res);

                // Add marker with data on the map
                mMap.addMarker(new MarkerOptions().position(MapCoords).icon(BitmapDescriptorFactory
                        .fromBitmap(mBitmap)).title(currentCity));



        }
    }
}
