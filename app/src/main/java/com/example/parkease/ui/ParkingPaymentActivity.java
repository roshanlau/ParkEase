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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkease.MainActivity;
import com.example.parkease.R;
import com.example.parkease.object.Notification;
import com.example.parkease.object.Parking;
import com.example.parkease.object.Transaction;
import com.example.parkease.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParkingPaymentActivity extends AppCompatActivity {
    /*To-do*/
    //get parking ID - done
    //set on click listen for up and down btn to increase time by 30min - done
    //determine the time and calculate the price of the parking time -done
    //deduct price from ewallet
    //update parking status
    TextView tvParkingID, tvLocation, tvTime, tvPrice;
    Button btnPayNow;
    ImageView btnUp, btnDown;
    String parkingID;
    double parkingRate = 0.5;

    DecimalFormat df = new DecimalFormat("0.00");
    DatabaseReference databaseUsers = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");

    DatabaseReference databaseTransactions = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("transactions");
    DatabaseReference databaseParkings = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("parking");
    DatabaseReference databaseNotification = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("notifications");
    Parking currentParking;
    String address;
    User currentUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_payment);
        tvParkingID = findViewById(R.id.tv_parkingPayment_parkingID);
        tvLocation = findViewById(R.id.tv_parkingPayment_location);
        tvTime = findViewById(R.id.tv_parkingPayment_time);
        tvPrice = findViewById(R.id.tv_parkingPayment_price);
        btnPayNow = findViewById(R.id.btn_parkingPayment_paynow);
        btnDown = findViewById(R.id.btn_parkingPayment_down);
        btnUp = findViewById(R.id.btn_parkingPayment_up);

        Intent intent = getIntent();
        parkingID = intent.getStringExtra("parkingID");

        tvParkingID.setText(parkingID);



        databaseParkings.child(parkingID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(ParkingPaymentActivity.this, "Parking Not Available", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentParking = task.getResult().getValue(Parking.class);
                address = getAddress(currentParking.getLatitude(), currentParking.getLongitude());
                tvPrice.setText(df.format(currentParking.getPrice()));
                tvLocation.setText(address);
            }
        });


        String currentUserID = getCurrentUserID();

        databaseUsers.child(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                currentUser = task.getResult().getValue(User.class);
            }
        });



        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTime = Integer.parseInt(tvTime.getText().toString());
                currentTime += 30;
                tvTime.setText(Integer.toString(currentTime));
                double currentPrice = Double.parseDouble(tvPrice.getText().toString());
                currentPrice += parkingRate;
                tvPrice.setText(df.format(currentPrice));
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvTime.getText().toString().equals("30")){
                    Toast.makeText(ParkingPaymentActivity.this, "Cannot be lower that 30 min", Toast.LENGTH_SHORT).show();
                }else{
                    int currentTime = Integer.parseInt(tvTime.getText().toString());
                    currentTime -= 30;
                    tvTime.setText(Integer.toString(currentTime));
                    double currentPrice = Double.parseDouble(tvPrice.getText().toString());
                    currentPrice -= parkingRate;
                    tvPrice.setText(df.format(currentPrice));
                }
            }
        });

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUsers.child(currentUserID).child("userBalance").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ParkingPaymentActivity.this, "Failed Payment", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date currentTime = new Date();
                        Date endTime = new Date(currentTime.getTime() + Integer.parseInt(tvTime.getText().toString()) * 1000 * 60);

                        String currentBalance = task.getResult().getValue(String.class);

                        double newValue = Double.parseDouble(currentBalance) - Double.parseDouble(tvPrice.getText().toString());

                        databaseUsers.child(currentUserID).child("userBalance").setValue(String.valueOf(newValue));
                        databaseParkings.child(parkingID).child("status").setValue(true);
                        databaseParkings.child(parkingID).child("startTime").setValue(sdf.format(currentTime));
                        databaseParkings.child(parkingID).child("endTime").setValue(sdf.format(endTime));
                        databaseParkings.child(parkingID).child("currentUser").setValue(currentUserID);

                        currentParking.addParkingHistory(currentUser.getUserName() + " " + currentUserID + " " + sdf.format(currentTime));
                        databaseParkings.child(parkingID).child("parkingHistory").setValue(currentParking.getParkingHistory());

                        addTransaction(sdf.format(currentTime));
                        addNotification(parkingID, address, sdf.format(currentTime), sdf.format(endTime));

                        Intent intent1 = new Intent(ParkingPaymentActivity.this, MainActivity.class);
                        startActivity(intent1);
                    }
                });



            }
        });



    }

    public String getCurrentUserID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void addTransaction(String currentTime) {
        try {

            String transactionID = databaseTransactions.push().getKey();
            String transactionType = "Parking Payment";
            Double transactionAmount = Double.parseDouble(tvPrice.getText().toString());
            String transactionTime = currentTime;
            String userId = getCurrentUserID();
            Transaction newTransaction = new Transaction(transactionID, transactionType, transactionAmount, transactionTime, userId);
            databaseTransactions.child(transactionID).setValue(newTransaction);
        } catch (NumberFormatException e) {
            Toast.makeText(ParkingPaymentActivity.this, "Please enter a valid number", Toast.LENGTH_LONG).show();
        }
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(ParkingPaymentActivity.this, Locale.getDefault());
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
            Toast.makeText(ParkingPaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    private void addNotification(String parkingID, String address, String currentTime, String endTime){
        String notificationID = databaseNotification.push().getKey();
        String message = "You sucessfully pay for parking " + parkingID + " at " + address + " starting at " + currentTime + " and end at " + endTime;
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Notification notification = new Notification(notificationID, userID, message, currentTime);
        databaseNotification.child(notificationID).setValue(notification);
    }
}