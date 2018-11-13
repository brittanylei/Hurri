package hurri.ucsc.edu.hurri;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class emergency_text_message extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button snd;
    private EditText tel;
    private String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_text_message);
        snd = findViewById(R.id.Button01);
        tel = findViewById(R.id.EditText01);
        txt = "Help!";
        snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    protected void sendMessage() {
        String phoneNo = tel.getText().toString();
        String message = txt;
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},0);
        }
        try{
            SmsManager smsManager = SmsManager.getDefault();
            List<String> texts = smsManager.divideMessage(message);
            for (String text : texts){
                smsManager.sendTextMessage(phoneNo,null,text,null,null);
            }
            Toast.makeText(getApplicationContext(), "Sent successfully", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Failed to send", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
