package com.kunalc.trackerapp.shadow.database;

/**
 * Created by KunalC on 6/10/2017.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kunalc.trackerapp.shadow.bean.TrackDetails;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "LocationDetails";

    // TrackDetailss table name
    private static final String TABLE_CORDINATES = "cordinates";

    // TrackDetailss Table Columns names
    private static final String KEY_ID = "id";
    private static final String TIME_SPENT = "time_spent";
    private static final String VISITED_DATE = "visited_date";
    private static final String ADDRESS = "address";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CORDINATES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TIME_SPENT + " TEXT,"
                + VISITED_DATE + " TEXT," + ADDRESS + " TEXT," + LATITUDE + " TEXT," + LONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORDINATES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addTrackDetails(TrackDetails trackDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(KEY_ID,trackDetails.getKeyId());
        values.put(TIME_SPENT, trackDetails.getTimeSpent());
        values.put(VISITED_DATE, (trackDetails.getVisitedDate().toString()));
        values.put(ADDRESS, trackDetails.getAddress());
        values.put(LATITUDE, (String.valueOf(trackDetails.getLatitude())));
        values.put(LONGITUDE, String.valueOf(trackDetails.getLongitude()));

        // Inserting Row
        db.insert(TABLE_CORDINATES, null, values);
        db.close(); // Closing database connection
    }

    // Getting All TrackDetailss
    public List<TrackDetails> getAllTrackDetailss() {

        List<TrackDetails> locationList = new ArrayList<TrackDetails>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CORDINATES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TrackDetails contact = new TrackDetails();
                contact.setKeyId(Integer.parseInt(cursor.getString(0)));
                contact.setTimeSpent(Long.valueOf(cursor.getString(1)));
                contact.setVisitedDate(new Date(cursor.getString(2)));
                contact.setAddress(cursor.getString(3));
                contact.setLatitude(Double.valueOf(cursor.getString(4)));
                contact.setLongitude(Double.valueOf(cursor.getString(5)));

                // Adding contact to list
                locationList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return locationList;
    }

}
