package com.kunalc.trackerapp.shadow.bean;

import java.util.Date;

/**
 * Created by KunalC on 6/10/2017.
 */
//log1
//log2
public class TrackDetails {
    private int keyId;
    private String address;
    private Date visitedDate;
    private long timeSpent;
    private double latitude;
    private double longitude;

    public TrackDetails() {
    }

    public TrackDetails(int KEY_ID, String address, Date visitedDate, long timeSpent, long latitude, long longitude) {
        this.keyId = KEY_ID;
        this.address = address;
        this.visitedDate = visitedDate;
        this.timeSpent = timeSpent;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getVisitedDate() {
        return visitedDate;
    }

    public void setVisitedDate(Date visitedDate) {
        this.visitedDate = visitedDate;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    @Override
    public String toString() {
        return "TrackDetails{" +
                "address='" + address + '\'' +
                ", visitedDate=" + visitedDate +
                ", timeSpent=" + timeSpent +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
