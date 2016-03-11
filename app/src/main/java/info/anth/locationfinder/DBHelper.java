package info.anth.locationfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyLocationsDB.db";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FeedReaderContractLocations.FeedEntryLocations.TABLE_NAME + " (" +
                    FeedReaderContractLocations.FeedEntryLocations._ID + " INTEGER PRIMARY KEY " +
                    "," + FeedReaderContractLocations.FeedEntryLocations.COLUMN_NAME + " TEXT " +
                    "," + FeedReaderContractLocations.FeedEntryLocations.COLUMN_LONGITUDE + " DOUBLE " +
                    "," + FeedReaderContractLocations.FeedEntryLocations.COLUMN_LATITUDE + " DOUBLE " +
                    "," + FeedReaderContractLocations.FeedEntryLocations.COLUMN_ADDRESS + " TEXT " +
            " )";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + FeedReaderContractLocations.FeedEntryLocations.TABLE_NAME;

/*
    public static final String LOCATIONS_TABLE_NAME = "locations";
    public static final String LOCATIONS_COLUMN_ID = "id";
    public static final String LOCATIONS_COLUMN_NAME = "name";
    public static final String LOCATIONS_COLUMN_LONGITUDE = "longitude";
    public static final String LOCATIONS_COLUMN_LATITUDE = "latitude";
    public static final String LOCATIONS_COLUMN_ADDRESS = "address";
    private HashMap hp;
*/

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

//    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    public long insertLocation(String name, Double longitude, Double latitude, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContractLocations.FeedEntryLocations.COLUMN_NAME, name);
        values.put(FeedReaderContractLocations.FeedEntryLocations.COLUMN_LONGITUDE, longitude);
        values.put(FeedReaderContractLocations.FeedEntryLocations.COLUMN_LATITUDE, latitude);
        values.put(FeedReaderContractLocations.FeedEntryLocations.COLUMN_ADDRESS, address);

        long newRowId;
        newRowId = db.insert(FeedReaderContractLocations.FeedEntryLocations.TABLE_NAME, null, values);
        return newRowId;
    }

    public Cursor getOneLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // columns to retrieve
        String[] projection = {
                FeedReaderContractLocations.FeedEntryLocations._ID,
                FeedReaderContractLocations.FeedEntryLocations.COLUMN_NAME,
                FeedReaderContractLocations.FeedEntryLocations.COLUMN_LONGITUDE,
                FeedReaderContractLocations.FeedEntryLocations.COLUMN_LATITUDE,
                FeedReaderContractLocations.FeedEntryLocations.COLUMN_ADDRESS
        };

        // Where clause
        String where = FeedReaderContractLocations.FeedEntryLocations._ID + " = " + String.valueOf(id);

        // sort order
        String sortOrder = FeedReaderContractLocations.FeedEntryLocations.COLUMN_NAME + " DESC";

        return db.query(
                FeedReaderContractLocations.FeedEntryLocations.TABLE_NAME,
                projection,         // The columns to return
                where,               // The columns for the WHERE clause
                null,               // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups (having)
                sortOrder           // The sort Order
        );

    }

    public Cursor getAllLocations() {
        SQLiteDatabase db = this.getReadableDatabase();

        // columns to retrieve
        String[] projection = {
                FeedReaderContractLocations.FeedEntryLocations._ID,
                FeedReaderContractLocations.FeedEntryLocations.COLUMN_NAME,
                FeedReaderContractLocations.FeedEntryLocations.COLUMN_LONGITUDE,
                FeedReaderContractLocations.FeedEntryLocations.COLUMN_LATITUDE,
                FeedReaderContractLocations.FeedEntryLocations.COLUMN_ADDRESS
        };

        // sort order
        String sortOrder = FeedReaderContractLocations.FeedEntryLocations.COLUMN_NAME + " DESC";

        return db.query(
                FeedReaderContractLocations.FeedEntryLocations.TABLE_NAME,
                projection,         // The columns to return
                null,               // The columns for the WHERE clause
                null,               // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups (having)
                sortOrder           // The sort Order
        );

    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, FeedReaderContractLocations.FeedEntryLocations.TABLE_NAME);
    }

    /*
    public boolean updateContact(Integer id, String name, String phone, String email, String street, String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
    */

    public Integer deleteLocation(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String[]> getListOfLocations() {
        ArrayList<String[]> array_list = new ArrayList<String[]>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select " +
                FeedReaderContractLocations.FeedEntryLocations._ID
                        + ", " + FeedReaderContractLocations.FeedEntryLocations.COLUMN_NAME
                        + " from " + FeedReaderContractLocations.FeedEntryLocations.TABLE_NAME, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String[] twoFields = {
                    c.getString(0),
                    c.getString(1)
            };
            array_list.add(twoFields);
            c.moveToNext();
        }

        c.close();

        return array_list;
    }
}
