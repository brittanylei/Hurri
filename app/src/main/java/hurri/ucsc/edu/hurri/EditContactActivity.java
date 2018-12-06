package hurri.ucsc.edu.hurri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContactActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave,btnDelete;
    private EditText editable_item, editable_name;

//    MyDB mDatabaseHelper;

    private String selectedName;
    private String selectedNumber;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        editable_item = (EditText) findViewById(R.id.editable_item);
        editable_name = (EditText) findViewById(R.id.editable_name);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");

        selectedNumber = receivedIntent.getStringExtra("number");

        //set the text to show the current selected name
//        editable_item.setText(selectedName);
        editable_item.setText(selectedNumber);
        editable_name.setText(selectedName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editable_item.getText().toString();
                String name = editable_name.getText().toString();
                if(!item.equals("")){
                    MainActivity.db.updateNumber(item,selectedID,selectedNumber);
                    if(!name.equals("")) {
                        MainActivity.db.updateName(name, selectedID, selectedName);
                        back();
                    }else{

                    }
                }else {

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.db.deleteName(selectedID,selectedName);
                editable_item.setText("");
                back();
            }
        });

    }

    public void back() {
        Intent intent = new Intent(EditContactActivity.this, ViewContactActivity.class);
        startActivity(intent);
    }
}
