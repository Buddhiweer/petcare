package com.bStudio.petcare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private Marker marker;
    private TextView textViewAddress;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private EditText editTextSearch;
    private Button buttonSearch;
    private String district;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textViewAddress = findViewById(R.id.textViewAddress);
        Button buttonSaveLocation = findViewById(R.id.buttonSaveLocation);
        buttonSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });

        //Search bar
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocation();
            }
        });

        // Set an OnEditorActionListener for the search EditText
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchLocation();
                    return true;
                }
                return false;
            }
        });

    }


    private void searchLocation() {
        String location = editTextSearch.getText().toString().trim();
        if (!location.isEmpty()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(location, 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    moveMapCamera(latLng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    hideKeyboard();
                    clearSearch();
                } else {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error searching location", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (imm != null && currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void clearSearch() {
        editTextSearch.getText().clear();
    }


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        // Enable the location layer
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Get the last known location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            moveMapCamera(latLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    private void moveMapCamera(LatLng latLng) {
        mMap.clear();

        // change Marker icon
        int markerWidth = 120; // Desired marker width in pixels
        int markerHeight = 120; // Desired marker height in pixels

        BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.drawable.pin);
        BitmapDescriptor resizedMarker = resizeMarker(customMarker, markerWidth, markerHeight);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(resizedMarker);

        marker = mMap.addMarker(markerOptions);
        updateAddressTextView(latLng);

        //        // default Marker colour change
//        float markerColorHue = BitmapDescriptorFactory.HUE_ORANGE; // Set the desired color here
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(latLng)
//                .icon(BitmapDescriptorFactory.defaultMarker(markerColorHue));
//        marker = mMap.addMarker(markerOptions);
//        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private BitmapDescriptor resizeMarker(BitmapDescriptor marker, int width, int height) {
        int scaledWidth = width;
        int scaledHeight = height;

        Bitmap originalMarker = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        Bitmap scaledMarker = Bitmap.createScaledBitmap(originalMarker, scaledWidth, scaledHeight, false);

        return BitmapDescriptorFactory.fromBitmap(scaledMarker);
    }

    private void updateAddressTextView(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                textViewAddress.setText(addressText);

                // Get the district
                district = address.getSubAdminArea();
                if (district != null) {
                    // Do something with the district
                    // For example, you can store it in a variable or use it in your code
                    // district variable can be accessed from other parts of your code
                    // You can also display it in a TextView or perform any other desired operation
                    Toast.makeText(this, "Area: " + district, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        private void saveLocation() {
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            String address = textViewAddress.getText().toString();
            String district = this.district;

            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("address", address);
            intent.putExtra("district",district);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        marker = mMap.addMarker(new MarkerOptions().position(latLng));
        moveMapCamera(latLng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable the location layer
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
