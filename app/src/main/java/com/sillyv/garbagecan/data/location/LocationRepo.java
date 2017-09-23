package com.sillyv.garbagecan.data.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;
import com.patloew.rxlocation.RxLocation;
import com.sillyv.garbagecan.data.RepositoryContract;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;

/**
 * Created by Vasili on 9/15/2017.
 */

public class LocationRepo
        implements RepositoryContract.Location {


    public static final double NO_PERMISSION_LOCATION = -20d;
    public static final double NO_LOCATION_LOCATION = -10d;
    private static LocationRepo instance;
    private final RxLocation rxLocation;
    private final LocationRequest locationRequest;
    private final RxPermissions rxPermissions;

    @SuppressLint("MissingPermission")
    private LocationRepo(Activity context) {
        rxLocation = new RxLocation(context);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);
        rxPermissions = new RxPermissions(context);

    }


    public static LocationRepo getInstance(Activity context) {
        if (instance == null) {
            instance = new LocationRepo(context);
        }
        return instance;
    }

    @Override
    public Single<LatLonModel> getLocation() {
        return rxLocation.settings()
                .checkAndHandleResolution(locationRequest)
                .flatMapObservable(this::getAddressObservable)
                .first(getNoLocationFoundEver());
    }

    @Override
    public void detach() {
        instance = null;
    }

    @NonNull
    private LatLonModel getNoLocationFoundEver() {
        return new LatLonModel(NO_LOCATION_LOCATION, NO_LOCATION_LOCATION);
    }

    //Lint suppressed because RxPermission does make sure permissions are granted
    private Observable<LatLonModel> getAddressObservable(boolean success) {
        Observable<Boolean> permissionRequest = rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionRequest.flatMap(granted -> {
            Observable<LatLonModel> addressObservable;
            if (!granted)
                return mockNoPermissionsLocation();
            else {
                addressObservable = getMostAccurateLocation(success);
            }
            return addressObservable;
        });
    }

    private Observable<LatLonModel> getMostAccurateLocation(boolean success) {
        Observable<LatLonModel> addressObservable;
        if (success) {
            addressObservable = getLocationObservable().mergeWith(Observable.timer(3,
                    TimeUnit.SECONDS).flatMap(__ -> getLastLocationObservable().toObservable()))
                    .take(1);
        } else {
            addressObservable = getLastLocationObservable().toObservable();
        }
        return addressObservable;
    }

    private ObservableSource<? extends LatLonModel> mockNoPermissionsLocation() {
        return Observable.just(new LatLonModel(NO_PERMISSION_LOCATION, NO_PERMISSION_LOCATION));
    }

    @SuppressLint("MissingPermission")
    private Maybe<LatLonModel> getLastLocationObservable() {
        return rxLocation
                .location()
                .lastLocation()
                .flatMap(this::getAddressFromLocation);
    }

    @SuppressLint("MissingPermission")
    private Observable<LatLonModel> getLocationObservable() {
        return rxLocation
                .location()
                .updates(locationRequest)
                .flatMapMaybe(this::getAddressFromLocation);
    }

    private Maybe<LatLonModel> getAddressFromLocation(Location location) {

        return Maybe.just(new LatLonModel(location.getLatitude(), location
                .getLongitude()));

    }
}
