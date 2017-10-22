package com.sillyv.garbagecan.screen.history;

/**
 * Created by Vasili on 10/22/2017.
 */

class HistoryItem {

    private String dateString;
    private String path;
    private String locationString;

    HistoryItem(String dateString, String path, double lat, double lon) {
        this.dateString = dateString;
        this.path = path;
        this.locationString = lat + "," + lon;
    }

    String getDateString() {
        return dateString;
    }

    String getPath() {
        return path;
    }

    String getLocationString() {
        return locationString;
    }
}
