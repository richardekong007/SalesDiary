package com.daveace.salesdiary.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.type.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;

public class LocationUtil {

    private static final int BUILD_VERSION = Build.VERSION.SDK_INT;
    private static final int MASHMALLOW = Build.VERSION_CODES.M;
    private static LocationListener locationListener;
    private static double latitude;
    private static double longitude;

    public static void requestLocationUpdate(Context ctx, LocationManager locationManager) {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent locationSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(locationSettingIntent);
            }

        };
        if (BUILD_VERSION >= MASHMALLOW) {
            if ((ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                    && (ctx.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(
                        (Activity) ctx,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 104);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
        }
    }

    public static void stopLocationUpdate(LocationManager locationManager){
        locationManager.removeUpdates(locationListener);
    }

    public static double getLatitude(){
        return latitude;
    }

    public static double getLongitude(){
        return longitude;
    }

    public static String getPlaceFromLocation(Context ctx, LatLng location){
        String place = "";
        try{
            place = (new ReverseGeoCodingTask(ctx)).execute(location).get();
        }catch (Exception e){
           Log.e("Exception", e.getMessage());
        }
        return place;
    }

    public static class ReverseGeoCodingTask extends AsyncTask<LatLng, Void, String>{

        @SuppressLint("StaticFieldLeak")
        Context ctx;
        private String place;

        ReverseGeoCodingTask(Context ctx){
            super();
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(LatLng ... locations) {
            LatLng location = locations[0];
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            }catch (IOException e){
                Log.e("IOException", e.getMessage());
            }
            if (addresses != null && addresses.size() > 0){
                Address address = addresses.get(0);
                String ADDRESS_FORMAT = "%s, %s, %s";
                setPlace(String.format(ADDRESS_FORMAT,
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0):"",
                        address.getLocality(),
                        address.getCountryName()));

            }
            return place;
        }

        private void setPlace(String thePlace){
            this.place = thePlace;
        }
    }
}
