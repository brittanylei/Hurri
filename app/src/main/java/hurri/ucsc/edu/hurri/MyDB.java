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

    private static final String COL1 = "id";
    private static final String COL2 = "name";
    private static final String COL3 = "phone";



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

    public Cursor getItemID(String name){
        db = getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

//    public int deleteName(String s){
//        db = getWritableDatabase();
//        return db.delete(TABLE_NAME, "name = ?", new String[] {s});
//    }


    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
//        Log.d(TAG, "updateName: query: " + query);
//        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    public void updateNumber(String newNumber, int id, String oldNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + newNumber + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL3 + " = '" + oldNumber + "'";
//        Log.d(TAG, "updateName: query: " + query);
//        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
//        Log.d(TAG, "deleteName: query: " + query);
//        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
}

