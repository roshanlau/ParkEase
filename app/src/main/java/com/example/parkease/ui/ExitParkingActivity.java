package com.example.parkease.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkease.MainActivity;
import com.example.parkease.R;
import com.example.parkease.object.Notification;
import com.example.parkease.object.Parking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ExitParkingActivity extends AppCompatActivity {
    TextView tvLocation, tvParkingID, tvStartTime, tvEndTime;
    Button btnConfirm, btnCancel;
    String parkingID;
    DatabaseReference databaseParkings = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("parking");
    DatabaseReference databaseNotification = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("notifications");
    Parking currentParking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_parking);

        tvLocation = findViewById(R.id.tv_exitParking_location);
        tvParkingID = findViewById(R.id.tv_exitParking_parkingID);
        tvStartTime = findViewById(R.id.tv_exitParking_startTime);
        tvEndTime = findViewById(R.id.tv_exitParking_endTime);
        btnCancel = findViewById(R.id.btn_exitParking_cancel);
        btnConfirm = findViewById(R.id.btn_exitParking_confirm);

        Intent intent = getIntent();
        parkingID = intent.getStringExtra("parkingID");

        databaseParkings.child(parkingID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(ExitParkingActivity.this, "Parking Not Available", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentParking = task.getResult().getValue(Parking.class);

                tvLocation.setText(getAddress(currentParking.getLatitude(), currentParking.getLongitude()));

                tvParkingID.setText(parkingID);

                tvStartTime.setText(currentParking.getStartTime());
                tvEndTime.setText(currentParking.getEndTime());

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseParkings.child(parkingID).child("status").setValue(false);
                        databaseParkings.child(parkingID).child("startTime").setValue("none");
                        databaseParkings.child(parkingID).child("endTime").setValue("none");
                        databaseParkings.child(parkingID).child("currentUser").setValue("none");
                        goToMainActivity();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToMainActivity();
                    }
                });
            }
        });




    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(ExitParkingActivity.this, Locale.getDefault());
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
            Toast.makeText(ExitParkingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    private void addNotification(String parkingID, String address, String currentTime, String endTime){
//        Date currentTime = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        String dateString = sdf.format(currentTime);
        String notificationID = databaseNotification.push().getKey();
        String message = "You sucessfully pay for parking " + parkingID + " at " + address + " starting at " + currentTime + " and end at " + endTime;
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Notification notification = new Notification(notificationID, userID, message, currentTime);
        databaseNotification.child(notificationID).setValue(notification);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(ExitParkingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}