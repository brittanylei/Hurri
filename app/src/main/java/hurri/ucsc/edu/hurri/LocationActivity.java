package hurri.ucsc.edu.hurri;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {
    final int ACCESS_FINE_LOCATION =1;
    final int ACCESS_COARSE_LOCATION =1;
    private GoogleMap mMap;
    Location myLocation;
    LocationManager locationManager;
    final LatLng PRIPYAT = new LatLng(51.405556, 30.056944);
    final LatLng HOME = PRIPYAT;
    final LatLng WORK = new LatLng(51.261926,30.236045);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setPointer();
    }

    private void setPointer() {
        //get location service (system service)
        getGPSPermission(); // ask for permission by code for API 6 and above
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //set location updates
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
        locationManager.requestLocationUpdates(getCriteria(), 10, 50, this);
        //locationManager.getLastKnownLocation(getCriteria()); //get the last known location
        findViewById(R.id.btnHome).setOnClickListener(this); // btn listeners
        findViewById(R.id.btnWork).setOnClickListener(this);
    }

    private void getGPSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("gps Permission needed to use gps jutsu")
                    .setMessage("This permission is needed because the app needs to be able to use maps")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LocationActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);

        }

    }

    public String getCriteria()
    {
        Criteria criteria = new Criteria(); //indicates criteria for selction on location provider
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);

        //get our best provider
        String bestProvicer =  locationManager.getBestProvider(criteria,true);// if false best GPS service will be wokend and used
        // if true only the gps wifi or gprs that are lit
        return bestProvicer;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // this is a call back methode it fires up with the service
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng hackerU = new LatLng(32.0831646, 34.8032984);
        LatLng home=HOME;

        mMap.addMarker(new MarkerOptions().position(home).title(motiToast(home)));

        //move instantly
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(hackerU));
        //animate move
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 17.4f)); // zoom on map
    }

    @Override
    public void onLocationChanged(Location location) {
        // fires up when device phisically moves
        this.myLocation=location;
        LatLng myCurrentLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCurrentLocation,17.4f));
        findViewById(R.id.lastLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                motiToast(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()));
            }
        });

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnHome:
                //it's coming home, it's coming home
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(HOME,17.4f));
                //Toast.makeText(this, HOME.latitude+","+HOME.longitude, Toast.LENGTH_SHORT).show();
                motiToast(HOME);
                break;
            case R.id.btnWork:
                //let's got to work (not)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(WORK,17.4f));
                //Toast.makeText(this, WORK.latitude+","+WORK.longitude, Toast.LENGTH_SHORT).show();
                motiToast(WORK);
                break;
        }
    }

    public String motiToast(LatLng myLocation)
    {
        // convert gps coordinates into place name
        String returnString = "";
        Geocoder gcdLocale = new Geocoder(this); //new Geocoder(this, Locale.ENGLISH)
        List<Address> localeAdd = null;
        try {
            localeAdd = gcdLocale.getFromLocation(myLocation.latitude,myLocation.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if localeAdd is null (false) get out of the function (method)
        assert localeAdd != null;
        if (localeAdd.size()>0)
        {
            String myAdd = localeAdd.get(0).getAddressLine(0);
            Toast.makeText(this, myAdd, Toast.LENGTH_LONG).show();
            returnString=myAdd;
        }
        return returnString;
    }

}