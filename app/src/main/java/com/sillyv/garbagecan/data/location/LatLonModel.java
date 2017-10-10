package com.sillyv.garbagecan.data.location;

/**
 * Created by Vasili on 10/5/2017.
 */

public class LatLonModel {


    Double latitude;
    Double longitude;

    public LatLonModel(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLonModel() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
