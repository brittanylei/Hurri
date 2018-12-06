package hurri.ucsc.edu.hurri;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    static MyDB db;
    private FusedLocationProviderClient client;

    static String vUp = "CALL";
    static String vDown = "MESSAGE";
    static Double Latitude;
    static String SLatitude;
    static Double Longitude;
    static String SLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDB(this, "contacts", null, 1);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

//        Button findLocation = findViewById(R.id.openMaps);
//        findLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }

                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location)
                    {
                        if(location != null)
                        {
                            Latitude = location.getLatitude();
                            SLatitude = Double.toString(Latitude);
                            Longitude = location.getLongitude();
                            SLongitude = Double.toString(Longitude);
                            Toast.makeText(MainActivity.this, "Latitude:"+SLatitude+"Longitude:"+SLongitude, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
//            }
//        });
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action, keycode;

        action = event.getAction();
        keycode = event.getKeyCode();

        if (keycode == KeyEvent.KEYCODE_VOLUME_UP) {

            if (KeyEvent.ACTION_DOWN == action) {
                if (vUp.equals("CALL")) {
                    makePhoneCall();
                } else if (vUp.equals("MESSAGE")) {
                    sendMessage();
                }

            }
        } else if (keycode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (KeyEvent.ACTION_DOWN == action) {
                if (vDown.equals("CALL")) {
                    makePhoneCall();
                } else if (vDown.equals("MESSAGE")) {
                    sendMessage();
                }
            }
        }


        return super.dispatchKeyEvent(event);
    }


    private void makePhoneCall() {
//        String number = "9497356738";
        String number = "4157937830";
        if (number.length() > 0) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) { // phone call permission not yet granted
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else { // make the phone call
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                Toast.makeText(this, "Calling", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMessage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);

        } else {
            Cursor cr = db.get();
            while (cr.moveToNext()) {
                String phoneNo = cr.getString(2);
                String txtMessage = "This is an auto-generated message from an emergency app, do not reply.  I'm in danger. Help!\n" +
                        "- Sent from Hurri app\n";
                String txtLocation = "https://maps.google.com/?q="+SLatitude+","+SLongitude;
//                String txtMessage = "This is an auto-generated message from an emergency app, do not reply.  I'm in danger. Help!\n" +
//                        "- Sent from Hurri app\n" + "https://www.google.co.id/maps/@37,-122";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, txtMessage, null, null);
                smsManager.sendTextMessage(phoneNo, null, txtLocation, null, null);
            }
            Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_LONG).show();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission request successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission request failed", Toast.LENGTH_LONG).show();
                }
            }
            case REQUEST_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
//                    Toast.makeText(this, "Permission denied :(", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void recorder(View view) {
        Intent intent = new Intent(this, AudioRecorderActivity.class);
        startActivity(intent);
    }

    public void add(View view) {
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivity(intent);
    }


    public void view(View view) {
        Intent intent = new Intent(this, ViewContactActivity.class);
        startActivity(intent);
    }

    public void setButtons(View view) {
        Intent intent = new Intent(this, SetButtonActivity.class);
        startActivity(intent);
    }
}


