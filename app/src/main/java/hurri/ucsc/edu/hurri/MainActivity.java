package hurri.ucsc.edu.hurri;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    static MyDB db;

    static String vUp = "CALL";
    static String vDown = "MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDB(this, "contacts", null, 1);

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action, keycode;

        action = event.getAction();
        keycode = event.getKeyCode();

        if (keycode == KeyEvent.KEYCODE_VOLUME_UP) {
            Toast.makeText(this, "HII", Toast.LENGTH_SHORT).show();

            if (KeyEvent.ACTION_DOWN == action) {
                if (vUp.equals("CALL")) {
                    makePhoneCall();
                }
                else if (vUp.equals("MESSAGE")) {
                    sendMessage();
                }

            }
        } else if (keycode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (KeyEvent.ACTION_DOWN == action) {
                if (vDown.equals("CALL")) {
                    makePhoneCall();
                }
                else if (vDown.equals("MESSAGE")) {
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
            }
        }
    }

    private void sendMessage() {
        String phoneNo = "5554";
        String txtMessage = "Help me";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);

        } else {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, txtMessage, null, null);
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
            case REQUEST_CALL:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
                    Toast.makeText(this, "Permission denied :(", Toast.LENGTH_SHORT).show();
                }
            }
        }
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


