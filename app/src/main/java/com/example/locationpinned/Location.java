package com.example.locationpinned;
import java.util.ArrayList;
import java.util.Date;

public class Location {

    // A list to store location objects
    public static ArrayList<Location> locationArrayList = new ArrayList<>();

    // Key used for editing a location
    public static String LOCATION_EDIT_EXTRA = "locationEdit";

    // Location properties
    private int id;
    private double latitude;
    private double longitude;
    private String address;
    private Date deleted;

    // A constructor for creating a location with no deletion date
    public Location(int id, double latitude, double longitude, String address) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        deleted = null; // No deletion date initially
    }

    // A constructor for creating a location WITH a deletion date
    public Location(int id, double latitude, double longitude, String address, Date deleted) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.deleted = deleted; // Set the deletion date
    }

    // Default constructor
    public Location() {}

    // Get a location by its ID
    public static Location getNoteForID(int passedNoteID) {
        for (Location location : locationArrayList) {
            if (location.getId() == passedNoteID)
                return location;
        }

        // Location not found
        return null;
    }

    // Get a list of locations that are not marked as deleted
    public static ArrayList<Location> nonDeletedNotes() {
        ArrayList<Location> nonDeleted = new ArrayList<>();
        for (Location location : locationArrayList) {
            if (location.getDeleted() == null)
                nonDeleted.add(location);
        }

        return nonDeleted;
    }

    // Getters and setters for location properties
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
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

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    // Override toString method to provide a string representation of the location
    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
