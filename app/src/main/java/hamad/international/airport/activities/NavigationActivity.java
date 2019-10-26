package hamad.international.airport.activities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import hamad.international.airport.R;
import hamad.international.airport.classes.BeaconCls;

import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static hamad.international.airport.utils.NavUtils.getLatLong;
import com.mapbox.turf.TurfJoins;

public class NavigationActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "NavActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    HashMap<String, BeaconCls> fixedBeacons = new HashMap<>();

    // 96c56157-9079-4704-9c29-1ec87fa75e8e      bis
    // 3ed627da-392a-48d4-a7a5-a6c9091a39e2      porom

    private MapView mapView;
    private GeoJsonSource indoorBuildingSource;

    private List<List<Point>> boundingBoxList;
    private View levelButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_navigation);

        // bis
        fixedBeacons.put("96c56157-9079-4704-9c29-1ec87fa75e8e", new BeaconCls(
                "96c56157-9079-4704-9c29-1ec87fa75e8e",
                25.1361128,
                51.7416359
        ));

        // porom
        fixedBeacons.put("3ed627da-392a-48d4-a7a5-a6c9091a39e2", new BeaconCls(
                "3ed627da-392a-48d4-a7a5-a6c9091a39e2",
                25.8861128343,
                51.2216359
        ));

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                        /*final List<Point> boundingBox = new ArrayList<>();

                        boundingBox.add(Point.fromLngLat(25.3261128,51.4416359));*/

                       /* try {

                            final List<Point> boundingBox = new ArrayList<>();

                            boundingBox.add(Point.fromLngLat(25.03791, 51.89715));
                            boundingBox.add(Point.fromLngLat(25.03791, 51.89811));
                            boundingBox.add(Point.fromLngLat(25.03532, 51.89811));
                            boundingBox.add(Point.fromLngLat(25.03532, 51.89708));

                            boundingBoxList = new ArrayList<>();
                            boundingBoxList.add(boundingBox);

                            mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
                                @Override
                                public void onCameraMove() {
                                    *//*if (mapboxMap.getCameraPosition().zoom > 16) {
                                        if (TurfJoins.inside(Point.fromLngLat(mapboxMap.getCameraPosition().target.getLongitude(),
                                                mapboxMap.getCameraPosition().target.getLatitude()), Polygon.fromLngLats(boundingBoxList))) {
                                            if (levelButtons.getVisibility() != View.VISIBLE) {
                                                showLevelButton();
                                            }
                                        } else {
                                            if (levelButtons.getVisibility() == View.VISIBLE) {
                                                hideLevelButton();
                                            }
                                        }
                                    } else if (levelButtons.getVisibility() == View.VISIBLE) {
                                        hideLevelButton();
                                    }*//*
                                }
                            });
                            indoorBuildingSource = new GeoJsonSource(
                                    "indoor-building", loadJsonFromAsset("white_house_lvl_0.geojson"));
                            style.addSource(indoorBuildingSource);

                            // Add the building layers since we know zoom levels in range
                            loadBuildingLayer(style);

                        } catch (Exception e){
                            e.printStackTrace();
                        }*/
                    }
                });


                Button buttonSecondLevel = findViewById(R.id.second_level_button);
                buttonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        indoorBuildingSource.setGeoJson(loadJsonFromAsset("white_house_lvl_1.geojson"));
                    }
                });

                Button buttonGroundLevel = findViewById(R.id.ground_level_button);
                buttonGroundLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        indoorBuildingSource.setGeoJson(loadJsonFromAsset("white_house_lvl_0.geojson"));
                    }
                });

            }
        });
    }

    private void loadBuildingLayer(@NonNull Style style) {
// Method used to load the indoor layer on the map. First the fill layer is drawn and then the
// line layer is added.

        FillLayer indoorBuildingLayer = new FillLayer("indoor-building-fill", "indoor-building").withProperties(
                fillColor(Color.parseColor("#eeeeee")),
// Function.zoom is used here to fade out the indoor layer if zoom level is beyond 16. Only
// necessary to show the indoor map at high zoom levels.
                fillOpacity(interpolate(exponential(1f), zoom(),
                        stop(16f, 0f),
                        stop(16.5f, 0.5f),
                        stop(17f, 1f))));

        style.addLayer(indoorBuildingLayer);

        LineLayer indoorBuildingLineLayer = new LineLayer("indoor-building-line", "indoor-building").withProperties(
                lineColor(Color.parseColor("#50667f")),
                lineWidth(0.5f),
                lineOpacity(interpolate(exponential(1f), zoom(),
                        stop(16f, 0f),
                        stop(16.5f, 0.5f),
                        stop(17f, 1f))));
        style.addLayer(indoorBuildingLineLayer);
    }

    private String loadJsonFromAsset(String filename) {
// Using this method to load in GeoJSON files from the assets folder.

        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, Charset.forName("UTF-8"));

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
        mapView.onResume();
    }

    @Override
    public void onBeaconServiceConnect() {

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                Log.d(TAG, "Beacon count:  "+beacons.size());

                if (beacons.size() > 1) {
                  /*BeaconCls firstBeacon = beacons.iterator().next();
                  logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");*/

                    int i = 1;
                    double min = 99999999f;
                    double max = 0f;

                    ArrayList<Beacon> beaconsminmax = new ArrayList<>(2);

                    // insert initial 2 elements
                    beaconsminmax.add(beacons.iterator().next());
                    beaconsminmax.add(beacons.iterator().next());

                    for (Beacon beacon : beacons) {
                        double dist = beacon.getDistance();

                        if (min > dist){
                            min = dist;
                            beaconsminmax.set(0, beacon);
                        }

                        if (max < dist){
                            max = dist;

                            beaconsminmax.set(1, beacon);

                            /*if (beaconsminmax.size() == 0){
                                beaconsminmax.set(0, beacon); // for min
                                beaconsminmax.add(1, beacon); // for max
                            } else {
                                beaconsminmax.add(1, beacon);
                            }*/
                        }

                        i++;
                    }

                    // logToDisplay("min => " + min + " :: " + "max => " + max);
                    getBeaconsLatLong(beaconsminmax);
                } else {
                    // showToast("Need at least 2 beacon devices");
                }
            }

        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) { e.printStackTrace();  }
    }


    private void hideLevelButton() {
// When the user moves away from our bounding box region or zooms out far enough the floor level
// buttons are faded out and hidden.
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        levelButtons.startAnimation(animation);
        levelButtons.setVisibility(View.GONE);
    }

    private void showLevelButton() {
// When the user moves inside our bounding box region or zooms in to a high enough zoom level,
// the floor level buttons are faded out and hidden.
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        levelButtons.startAnimation(animation);
        levelButtons.setVisibility(View.VISIBLE);
    }


    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = NavigationActivity.this.findViewById(R.id.rangingText);
                // editText.append(line+"\n");
                editText.setText(line);
            }
        });
    }

    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void getBeaconsLatLong(ArrayList<Beacon> beacons){

        String bluetoothName1 = beacons.get(0).getId1().toString();
        String bluetoothName2 = beacons.get(1).getId1().toString();
        // logToDisplay(bluetoothName1 + " == " + bluetoothName2);

        BeaconCls be1 = fixedBeacons.get(bluetoothName1);
        BeaconCls be2 = fixedBeacons.get(bluetoothName2);

         Log.w("Nav", bluetoothName1 + " == " + bluetoothName2);

         if (be1 != be2){
             BeaconCls usrLoc = getLatLong(beacons.get(0).getDistance(),
                     beacons.get(1).getDistance(),
                     be1,
                     be2
             );

             logToDisplay("Lat: " + usrLoc.getLat() + "; Long: " + usrLoc.getLng());
         }
    }
}
