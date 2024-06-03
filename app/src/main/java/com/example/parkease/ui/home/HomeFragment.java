package com.example.parkease.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkease.CaptureActivityPortrait;
import com.example.parkease.R;
import com.example.parkease.databinding.FragmentHomeBinding;
import com.example.parkease.ui.ParkingPaymentActivity;
import com.example.parkease.ui.TopUpSuccessfulActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.location.LocationListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    Button btnScan;
    TextView tvLocation;
    DatabaseReference databaseParkings = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("parking");
    double longitude, latitude;

    FusedLocationProviderClient client;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            latitude = getArguments().getDouble("Latitude");
//            longitude = getArguments().getDouble("Longitude");
//        }



        btnScan = root.findViewById(R.id.btn_home_scanqr);
        tvLocation = root.findViewById(R.id.tv_home_location);

        client = LocationServices.getFusedLocationProviderClient(getActivity());


        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            getCurrentLocation();
        }
        else {
            // When permission is not granted
            // Call method
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
        }



        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                intentIntegrator.setPrompt("Scan a QR code");
                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.QR_CODE);
                intentIntegrator.setOrientationLocked(false );
                intentIntegrator.setTorchEnabled(true);

                intentIntegrator.forSupportFragment(HomeFragment.this).initiateScan();
            }
        });



        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(intentResult != null){
            String contents = intentResult.getContents();
            if(contents != null){
                //start pay parking activity
                //find parking status in firebase, if available then proceed to payment
                //add extra the parking id from the qrscanner

                databaseParkings.child(intentResult.getContents()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getActivity(), "Parking ID not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent startIntent = new Intent(getActivity(), ParkingPaymentActivity.class);
                        startIntent.putExtra("parkingID", intentResult.getContents());
                        startActivity(startIntent);
                    }
                });
            }else{
                Toast.makeText(getActivity(), "Scan Cancelled", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Qr Code not valid", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }


    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getLocality() + ", " + obj.getAdminArea();

            Log.v("IGA", "Address" + add);
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
        // Check condition
        if (requestCode == 100 && (grantResults.length > 0)
                && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            // When permission are granted
            // Call  method
            getCurrentLocation();
        }
        else {
            // When permission are denied
            // Display toast
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        // Initialize Location manager
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            // Initialize location
                            Location location = task.getResult();
                            // Check condition
                            LocationRequest locationRequest = null;
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                latitude = location.getLatitude();
                                // set longitude
                                longitude = location.getLongitude();
                                Toast.makeText(getContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                                tvLocation.setText(getAddress(latitude, longitude));
                            } else {
                                // When location result is null
                                // initialize location request
                                locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                            }

                            // Initialize location call back
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    // Initialize
                                    // location
                                    Location location1 = locationResult.getLastLocation();
                                    // Set latitude
                                    latitude = location1.getLatitude();
                                    // Set longitude
                                    longitude = location1.getLongitude();
                                    Toast.makeText(getContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                                }
                            };

                            // Request location updates
                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

                        }

                    });
        }
        else {
            // When location service is not enabled
            // open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}