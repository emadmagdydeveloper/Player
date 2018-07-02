package com.alatheer.myplayer.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Models.DirectionModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private double distLat = 0.0;
    private double distLng = 0.0;
    private Location myLocation;
    private String CoarseLoc = Manifest.permission.ACCESS_COARSE_LOCATION;
    private String FineLoc = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int PER_REQ = 1021;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Marker myMarker;
    private List<LatLng> polylineList;
    private PolylineOptions polylineOptions;
    private TextView tv_distance,tv_time,tv_destination,tv_time_walk;
    private ImageView back;
    private final float zoom =11.5f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getDataFromIntent();
        initView();
        if (isServiceOk())
        {
            CheckPermission();
        }

    }

    private void initView() {
        tv_destination = findViewById(R.id.tv_destination);
        tv_distance = findViewById(R.id.tv_distance);
        tv_time = findViewById(R.id.tv_time);
        tv_time_walk = findViewById(R.id.tv_time_walk);
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            distLat = Double.parseDouble(intent.getStringExtra("lat"));
            distLng = Double.parseDouble(intent.getStringExtra("lng"));

            Log.e("lat111",distLat+"");
            Log.e("lng111",distLng+"");
        }
    }

    private boolean isServiceOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, 9001);
            dialog.show();
        } else {
            return false;
        }
        return false;
    }

    private void CheckPermission() {
        String [] permissions = new String[]{FineLoc,CoarseLoc};
        if (ContextCompat.checkSelfPermission(getApplicationContext(), FineLoc) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), CoarseLoc) == PackageManager.PERMISSION_GRANTED) {
                initMap();
                buildGoogleApiClient();
            } else {
                Log.e("5","5");

                ActivityCompat.requestPermissions(this,permissions,PER_REQ);
            }
        } else {
            Log.e("6","6");

            ActivityCompat.requestPermissions(this,permissions,PER_REQ);

        }
    }

    private void buildGoogleApiClient() {
        Log.e("1","1");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
    }

    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void StartLocationUpdate() {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation !=null)
        {
            myLocation = lastLocation;
            Log.e("2","2");

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap!=null)
        {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.uber_style_map));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setBuildingsEnabled(false);
            mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(false);

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        StartLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        if (myMarker!=null)
        {
            myMarker.remove();


        };
        myMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),zoom));
        getDirection(myLocation.getLatitude(),myLocation.getLongitude(),distLat,distLng);
        Log.e("lat",location.getLatitude()+"");
        Log.e("lng",location.getLongitude()+"");

    }


    private void getDirection(double myLat,double myLng , double distLat,double distLng)
    {

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+myLat+","+myLng+"&destination="+distLat+","+distLng+
                "&mode=driving&transit_routing_preference=less_driving&key=AIzaSyBtFSyNXCKpjzMH0lzvDWXGUb1aI6eCsBo";

        Tags.getServiceDir().getDirection(url)
                .enqueue(new Callback<DirectionModel>() {
                    @Override
                    public void onResponse(Call<DirectionModel> call, Response<DirectionModel> response) {
                        if (response.isSuccessful())
                        {

                            List<DirectionModel.RouteObject> routes = response.body().getRoutes();
                            if (routes.size()>0)
                            {
                                for (int i=0;i<routes.size();i++)
                                {
                                    String points = routes.get(i).getPolyLineObject().getPoints();
                                    polylineList = decodePoly(points);
                                    Log.e("points",points);
                                }
                                Log.e("polSize",polylineList.size()+"");

                                getWalkTime(myLat,myLng,distLat,distLng);
                                int time = Integer.parseInt(routes.get(0).getLegs().get(0).getDurationObject().getValue())/60;
                                if (time<59)
                                {
                                    tv_time.setText(String.valueOf(time)+" min");
                                }else if (time>=60&&time<1440)
                                {
                                    int h = time/60;
                                    int m = time%60;
                                    if (m>0)
                                    {
                                        tv_time.setText(String.valueOf(h)+"H"+","+m);

                                    }else
                                    {
                                        tv_time.setText(String.valueOf(h)+"H");

                                    }

                                }else if (time>=1440)
                                {
                                    int d = time/24;
                                    tv_time.setText(String.valueOf(d)+"D");

                                }
                                tv_destination.setText(routes.get(0).getLegs().get(0).getEnd_address());
                                int dis = Integer.parseInt(routes.get(0).getLegs().get(0).getDistanceObject().getValue().replace(",",""))/1000;
                                tv_distance.setText(String.valueOf(dis)+" km");
                                polylineOptions = new PolylineOptions();
                                polylineOptions.addAll(polylineList);
                                polylineOptions.color(ContextCompat.getColor(MapsActivity.this,R.color.colorPrimary));
                                polylineOptions.width(8f);
                                polylineOptions.startCap(new SquareCap());
                                polylineOptions.endCap(new SquareCap());
                                polylineOptions.jointType(JointType.ROUND);
                                mMap.addPolyline(polylineOptions);
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                        .position(polylineList.get(polylineList.size()-1)));

                            }



                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        Toast.makeText(MapsActivity.this, "Can't draw direction", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getWalkTime(double myLat,double myLng , double distLat,double distLng)
    {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+myLat+","+myLng+"&destination="+distLat+","+distLng+
                "&mode=walking&transit_routing_preference=less_driving&key=AIzaSyBtFSyNXCKpjzMH0lzvDWXGUb1aI6eCsBo";

        Tags.getServiceDir().getDirection(url)
                .enqueue(new Callback<DirectionModel>() {
                    @Override
                    public void onResponse(Call<DirectionModel> call, Response<DirectionModel> response) {
                        if (response.isSuccessful())
                        {

                            List<DirectionModel.RouteObject> routes = response.body().getRoutes();
                            if (routes.size()>0)
                            {


                                int time = Integer.parseInt(routes.get(0).getLegs().get(0).getDurationObject().getValue())/60;

                                if (time<59)
                                {
                                    tv_time_walk.setText(String.valueOf(time)+" min");
                                }else if (time>=60&&time<1440)
                                {
                                    int h = time/60;
                                    int m = time%60;
                                    if (m>0)
                                    {
                                        tv_time_walk.setText(String.valueOf(h)+"H"+","+m);

                                    }else
                                        {
                                            tv_time_walk.setText(String.valueOf(h)+"H");

                                        }

                                }else if (time>=1440)
                                {
                                    int d = time/24;
                                    tv_time_walk.setText(String.valueOf(d)+"D");

                                }

                            }



                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        Toast.makeText(MapsActivity.this, "Can't get time", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googleApiClient!=null&&googleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            googleApiClient.disconnect();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PER_REQ)
        {
            if (grantResults.length>0)
            {
                for (int i =0;i<grantResults.length;i++)
                {
                    if (grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                    {
                        Log.e("4","4");

                        return;
                    }
                }

                initMap();
                buildGoogleApiClient();
                Log.e("3","3");

            }
        }


    }
}
