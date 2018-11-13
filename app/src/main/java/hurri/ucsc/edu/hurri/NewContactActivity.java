package hurri.ucsc.edu.hurri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewContactActivity extends AppCompatActivity {

    EditText contact_name;
    EditText phone_num;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        contact_name = findViewById(R.id.editText1);
        phone_num = findViewById(R.id.editText2);

        button = (Button) findViewById(R.id.button);
    }

    public void addContact(View view) {
        String s1 = contact_name.getText().toString();
        String s2 = phone_num.getText().toString();

        MainActivity.db.insert(s1,s2);
    }
}
