package hurri.ucsc.edu.hurri;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewContactActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> list;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);


        display();
    }

    private void display() {
        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();

        Cursor cr = MainActivity.db.get();

        while (cr.moveToNext()) {
            int id = cr.getInt(0);
            String name = cr.getString(1);
            String phone = cr.getString(2);
            list.add(name + "   " + phone);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }
}
