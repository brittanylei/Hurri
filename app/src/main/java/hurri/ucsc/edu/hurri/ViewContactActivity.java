package hurri.ucsc.edu.hurri;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
//            list.add(name);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString().replaceAll(" .*", "");
                String number = adapterView.getItemAtPosition(i).toString().replaceAll(".* ", "");
//                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = MainActivity.db.getItemID(name); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
//                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(ViewContactActivity.this, EditContactActivity.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",name);
                    editScreenIntent.putExtra("number",number);

                    startActivity(editScreenIntent);
                }
                else{
//                    toastMessage("No ID associated with that name");
                }
            }
        });
    }
}
