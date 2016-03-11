package info.anth.locationfinder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Primary on 3/10/2016.
 */
public class LocationContent {

    /**
     * An array of Location items.
     */
    public static final List<LocationItem> ITEMS = new ArrayList<LocationItem>();

    /**
     * A map of sample Location items, by ID.
     */
    public static final Map<String, LocationItem> ITEM_MAP = new HashMap<String, LocationItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        //for (int i = 1; i <= COUNT; i++) {
        //    addItem(createDummyItem(i));
        //}
        Double longitude = 41.1799662;
        Double latitude = -73.8684637;
        Double longitude2 = longitude * -1;
        Double latitude2 = latitude + 180;
        //addItem(createLocationItem(1, longitude, latitude));
        //addItem(createLocationItem(2, longitude, latitude2));
        //addItem(createLocationItem(3, longitude2, latitude));
        //addItem(createLocationItem(4, longitude2, latitude2));
    }

    public static void clear() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void addItem(LocationItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static LocationItem createLocationItem(int position, Double longitude, Double latitude) {
        String id = String.valueOf(position);
        String location_name = "home " + String.valueOf(position);
        //Double longitude = 41.1799662;
        //Double latitude = -73.8684637;
        String address = "11 Piping Rock";

        return new LocationItem(id, location_name, longitude, latitude, address);
    }
/*
        private static DummyItem createDummyItem(int position) {
            return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
        }

        private static String makeDetails(int position) {
            StringBuilder builder = new StringBuilder();
            builder.append("Details about Item: ").append(position);
            for (int i = 0; i < position; i++) {
                builder.append("\nMore details information here.");
            }
            return builder.toString();
        }
    */

    /**
     * Location Content
     */
    public static class LocationItem {
        public final String id;
        public final String location_name;
        public final Double longitude;
        public final Double latitude;
        public final String address;

        public LocationItem(String id, String location_name, Double longitude, Double latitude, String address) {
            this.id = id;
            this.location_name = location_name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.address = address;
        }

        @Override
        public String toString() {
            return location_name + " Long: " + String.valueOf(longitude) + " Lat: " + String.valueOf(latitude);
        }

        public Uri geoUri() {
            String uriBegin = "geo:" + String.valueOf(latitude) + "," + String.valueOf(longitude);
            String query = String.valueOf(latitude) + "," + String.valueOf(longitude) + "(" + location_name + ")";
            String encodedQuery = Uri.encode(query);
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=23";
            return Uri.parse(uriString);
        }
    }

}
