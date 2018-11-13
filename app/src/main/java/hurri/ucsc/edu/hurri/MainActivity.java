package hurri.ucsc.edu.hurri;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1 ;
    static MyDB db;
    Button btn_emgc_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDB(this, "contacts", null, 1);
        btn_emgc_message = (Button) findViewById(R.id.btn_emergency_message);
        toEmergencyMessage();

    }


    public void toEmergencyMessage(){
        btn_emgc_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,emergency_text_message.class));
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        int action, keycode;

        action = event.getAction();
        keycode = event.getKeyCode();

        if (keycode == KeyEvent.KEYCODE_VOLUME_UP) {
            Toast.makeText(this, "HII", Toast.LENGTH_SHORT).show();

            if (KeyEvent.ACTION_DOWN == action) {
                makePhoneCall();
            }
        }

        return super.dispatchKeyEvent(event);
    }


    private void makePhoneCall() {
        String number = "9497356738";
        if (number.length() > 0) {
           if (ContextCompat.checkSelfPermission(MainActivity.this,
                   Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) { // phone call permission not yet granted
               ActivityCompat.requestPermissions(MainActivity.this,
                       new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL );
           }
           else { // make the phone call
               String dial = "tel:" + number;
               startActivity( new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
           }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
            else {
                Toast.makeText(this,"Permission denied :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
