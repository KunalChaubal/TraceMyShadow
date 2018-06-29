package com.kunalc.trackerapp.shadow.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.kunalc.trackerapp.shadow.bean.TrackDetails;
import com.kunalc.trackerapp.shadow.database.DatabaseHandler;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.sqrt;

public class LocationTrackerService extends Service {
    private static final String TAG = "KunalC";

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    double INIT_POS_LAT;
    double INIT_POS_LONG;
    double RADIUS_LAT_LOWER;
    double RADIUS_LONG_LOWER;
    double RADIUS_LAT_HIGHER;
    double RADIUS_LONG_HIGHER;

    Context context = this;
    Boolean insideRadius;

    DatabaseHandler databaseHandler = new DatabaseHandler(this);
    TrackDetails trackDetails = new TrackDetails();

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;


        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            sendMessageToActivity(location, "");
            checkUserHalted(location);
        }

        Boolean CIRCLE_RADIUS = false;

        final long THRESHOLD_TIME = 1;

        public void checkUserHalted(Location location) {
            INIT_POS_LAT = location.getLatitude();
            INIT_POS_LONG = location.getLongitude();
            if (CIRCLE_RADIUS || FIRST_TIME) {
                double DIFFERENCE = 0.00009;

                RADIUS_LAT_LOWER = INIT_POS_LAT - DIFFERENCE;
                RADIUS_LONG_LOWER = INIT_POS_LONG - DIFFERENCE;

                RADIUS_LAT_HIGHER = INIT_POS_LAT + DIFFERENCE;
                RADIUS_LONG_HIGHER = INIT_POS_LONG + DIFFERENCE;
                FIRST_TIME = false;
                startTimer();
            }

            double distance = getDistance(INIT_POS_LAT, INIT_POS_LONG, RADIUS_LAT_LOWER, RADIUS_LONG_LOWER);
            Log.e(TAG, "Distance: " + distance);

            if (distance > 0.5) {
                insideRadius = false;
            } else {
                insideRadius = true;
            }


            if (insideRadius) {
                Long time_visited = getTime();
                Log.e(TAG, "Start time: " + START_TIME + "time_visited: " + time_visited);
                if (time_visited >= THRESHOLD_TIME) {
                    if (proximity_flag) {
                        Log.e(TAG, "Time greater");
                        top_flag = true;
                    }
                    proximity_flag = false;
                }
            } else {
                if (top_flag) {
                    long duration = getTime();
                    saveToDb(location, INIT_POS_LAT, INIT_POS_LONG);

                    Log.e(TAG, "Duration is: " + duration);
                    top_flag = false;
                }
                proximity_flag = true;
                resetTimer();
                CIRCLE_RADIUS = true;
            }
        }

        Boolean proximity_flag = true;
        Boolean top_flag = false;

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    String START_TIME;

    public void startTimer() {
        START_TIME = DateFormat.getDateTimeInstance().format(new Date());
    }

    public void resetTimer() {
        Log.e(TAG, "Stop Time: " + currentTime());
        START_TIME = DateFormat.getDateTimeInstance().format(new Date());
    }

    public String currentTime() {
        String current_time = DateFormat.getDateTimeInstance().format(new Date());
        return current_time;
    }

    public long getTime() {
        Date time1 = null;
        Date time2 = null;
        long minutes = 0;
        try {
            time1 = new Date(START_TIME);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            time2 = new Date(currentTime());
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            Date x = calendar1.getTime();
            Date xy = calendar2.getTime();
            long diff = xy.getTime() - x.getTime();

            long seconds = diff / 1000;
            minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return minutes;
    }

    Boolean FIRST_TIME;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        FIRST_TIME = true;
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        /*} catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {*/
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "provider does not exist " + ex.getMessage());
        }
    }

    private void saveToDb(Location location, double lati, double longi) {
        getCurrentAddress(location);
        trackDetails.setKeyId(1);
        trackDetails.setVisitedDate(new Date(currentTime()));
        trackDetails.setTimeSpent(getTime());
        trackDetails.setLatitude(lati);
        trackDetails.setLongitude(longi);
        databaseHandler.addTrackDetails(trackDetails);

    }

    private TrackDetails getCurrentAddress(Location location) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            trackDetails.setAddress(address + ", " + city);
            Toast.makeText(this, "Address: " + knownName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trackDetails;
    }

    public double getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double earth_radius = 6371;

        double Lat = Math.toRadians(latitude2 - latitude1);
        double Lon = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(Lat / 2) * Math.sin(Lat / 2) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.sin(Lon / 2) * Math.sin(Lon / 2);
        double c = 2 * Math.asin(sqrt(a));
        double d = earth_radius * c;

        return d;
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void sendMessageToActivity(Location l, String msg) {
        Intent intent = new Intent("GPSLocationUpdates");
        // You can also include some extra data.
        intent.putExtra("Status", msg);
        Bundle b = new Bundle();
        b.putParcelable("Location", l);
        intent.putExtra("Location", b);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}