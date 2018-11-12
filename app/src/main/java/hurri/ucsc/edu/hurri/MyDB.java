package hurri.ucsc.edu.hurri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MyDB extends SQLiteOpenHelper {

    Context ctx;
    private static SQLiteDatabase db;
    private static String DB_NAME = "contacts";
    private static String TABLE_NAME = "contact_table";
    private static int VERSION = 1;


    public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, VERSION);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(id integer primary key, " + "name String, phone String);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        VERSION++;
        onCreate(db);
    }

    public long insert(String n, String p) throws SQLiteException {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", n);
        cv.put("phone", p);
        return db.insert(TABLE_NAME, null, cv);
    }

    public Cursor get() {
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME + ";", null);
        return cr;
    }

    // delete function
    public int deleteID(String s){
        db = getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[] {s});
    }
    public int deleteName(String s){
        db = getWritableDatabase();
        return db.delete(TABLE_NAME, "name = ?", new String[] {s});
    }
}

