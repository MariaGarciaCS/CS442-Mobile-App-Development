package com.marigarci.civicadvocacy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.Context.LOCATION_SERVICE;

public class Locator {
    private MainActivity ma;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Locator(MainActivity ma) {
        this.ma = ma;

        if (checkPermission()) {
            setUpLocationManager();
            determineLocation();

        }
    }

    public void determineLocation() {
        if (!checkPermission())
            return;

        if (locationManager == null)
            setUpLocationManager();

        //Network
        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                ma.setLocation(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(ma, "Using " + LocationManager.NETWORK_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //Passive
        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (loc != null) {
                ma.setLocation(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(ma, "Using " + LocationManager.PASSIVE_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //GPS
        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                ma.setLocation(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(ma, "Using " + LocationManager.GPS_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        return;
    }



    private void setUpLocationManager() {
        if (locationManager != null)
            return;

        if (!checkPermission())
            return;

        // Get the system's Location Manager
        locationManager = (LocationManager) ma.getSystemService(LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                ma.setLocation(location.getLatitude(), location.getLongitude());
                ma.closeWarning();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(ma, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ma,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }
}
