package com.bStudio.petcare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    private double targetLatitude = 7.4718043;
    private double targetLongitude = 80.3034404;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);


        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add a marker at the target location
        LatLng targetLocation = new LatLng(targetLatitude, targetLongitude);
        googleMap.addMarker(new MarkerOptions().position(targetLocation).title("Target Location"));

        // Move the camera to the target location with zoom level
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 15f));
    }

    private String getAddressFromLatLng(LatLng latLng) {
        // Implement your geocoding logic here to get the address from the LatLng
        return null;
    }
}
