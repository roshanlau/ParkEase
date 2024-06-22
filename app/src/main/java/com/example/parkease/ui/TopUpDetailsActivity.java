package com.example.parkease.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkease.R;
import com.example.parkease.object.Notification;
import com.example.parkease.object.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TopUpDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference databaseUsers = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");
    DatabaseReference databaseTransactions, databaseNotification;
    String paymentMethod;
    EditText et_amount;
    Button btn_10, btn_20, btn_30, btn_50, btn_100, btn_200, btn_300, btn_500, btn_paynow;
    TextView tvTotal, tvTotalPayment, tvPaymentMethod;
    CardView cardView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_details);
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");
        databaseTransactions = FirebaseDatabase.getInstance().getReference("transactions");
        databaseNotification = FirebaseDatabase.getInstance().getReference("notifications");

        et_amount = findViewById(R.id.et_topup_ammount);
        btn_10 = findViewById(R.id.btn_topup_10);
        btn_20 = findViewById(R.id.btn_topup_20);
        btn_30 = findViewById(R.id.btn_topup_30);
        btn_50 = findViewById(R.id.btn_topup_50);
        btn_100 = findViewById(R.id.btn_topup_100);
        btn_200 = findViewById(R.id.btn_topup_200);
        btn_300 = findViewById(R.id.btn_topup_300);
        btn_500 = findViewById(R.id.btn_topup_500);
        btn_paynow = findViewById(R.id.btn_topup_paynow);
        tvTotal = findViewById(R.id.tv_topup_total);
        tvTotalPayment = findViewById(R.id.tv_topup_totalpayment);
        tvPaymentMethod = findViewById(R.id.tv_topup_paymentmethod);
        cardView1 = findViewById(R.id.cardView_topup_paymentMethod);

        btn_10.setOnClickListener(this);
        btn_20.setOnClickListener(this);
        btn_30.setOnClickListener(this);
        btn_50.setOnClickListener(this);
        btn_100.setOnClickListener(this);
        btn_200.setOnClickListener(this);
        btn_300.setOnClickListener(this);
        btn_500.setOnClickListener(this);
        btn_paynow.setOnClickListener(this);
        cardView1.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvTotal.setText(s.toString());
                tvTotalPayment.setText(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_amount.addTextChangedListener(textWatcher);
        Intent intent = getIntent();
        paymentMethod = intent.getStringExtra("nestedItem");
        et_amount.setText("amount");

        if (paymentMethod != null){
            tvPaymentMethod.setText(paymentMethod);
        }




    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_topup_10){
            et_amount.setText(btn_10.getText().toString());
            tvTotal.setText(btn_10.getText().toString());
            tvTotalPayment.setText(btn_10.getText().toString());
        }else if(id == R.id.btn_topup_20){
            et_amount.setText(btn_20.getText().toString());
            tvTotal.setText(btn_20.getText().toString());
            tvTotalPayment.setText(btn_20.getText().toString());
        }else if(id == R.id.btn_topup_30){
            et_amount.setText(btn_30.getText().toString());
            tvTotal.setText(btn_30.getText().toString());
            tvTotalPayment.setText(btn_30.getText().toString());
        }else if(id == R.id.btn_topup_50){
            et_amount.setText(btn_50.getText().toString());
            tvTotal.setText(btn_50.getText().toString());
            tvTotalPayment.setText(btn_50.getText().toString());
        }else if(id == R.id.btn_topup_100){
            et_amount.setText(btn_100.getText().toString());
            tvTotal.setText(btn_100.getText().toString());
            tvTotalPayment.setText(btn_100.getText().toString());
        }else if(id == R.id.btn_topup_200){
            et_amount.setText(btn_200.getText().toString());
            tvTotal.setText(btn_200.getText().toString());
            tvTotalPayment.setText(btn_200.getText().toString());
        }else if(id == R.id.btn_topup_300){
            et_amount.setText(btn_300.getText().toString());
            tvTotal.setText(btn_300.getText().toString());
            tvTotalPayment.setText(btn_300.getText().toString());
        }else if(id == R.id.btn_topup_500){
            et_amount.setText(btn_500.getText().toString());
            tvTotal.setText(btn_500.getText().toString());
            tvTotalPayment.setText(btn_500.getText().toString());
        }else if(id == R.id.cardView_topup_paymentMethod){
            //to create PaymentMethodDetailsActivity
            Intent intent = new Intent(TopUpDetailsActivity.this, PaymentMethodsDetailsActivity.class);
            intent.putExtra("amount", et_amount.getText().toString());
            startActivity(intent);
        }else if(id == R.id.btn_topup_paynow){
            if (tvTotalPayment.getText().equals("") || tvPaymentMethod.getText().equals("Please Select a Payment Method")) {
                Toast.makeText(this, "Please Input Amount & Select Payment Method", Toast.LENGTH_SHORT).show();
            }
            if (!tvTotalPayment.getText().equals("") && !tvPaymentMethod.getText().equals("Please Select a Payment Method")){
                String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseUsers.child(currentUserUid).child("userBalance").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(TopUpDetailsActivity.this, "Failed to get user balance", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Get the current userBalance value
                        String currentBalance = task.getResult().getValue(String.class);

                        // Calculate the new balance by adding the value of tv7.getText().toString()
                        double newValue = Double.parseDouble(currentBalance) + Double.parseDouble(tvTotalPayment.getText().toString());

                        // Update the userBalance with the new value
                        databaseUsers.child(currentUserUid).child("userBalance").setValue(String.valueOf(newValue))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Successfully updated the userBalance
                                            // Get the current time
                                                /*Calendar calendar = Calendar.getInstance();
                                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                                int minute = calendar.get(Calendar.MINUTE);*/
                                            Date currentTime = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            String dateString = sdf.format(currentTime);
                                            // Now you have the current hour and minute
                                            //String currentTime = hour + ":" + minute;
                                            addTransaction(dateString);
                                            addNotification(dateString);
                                            Intent intent = new Intent(TopUpDetailsActivity.this, TopUpSuccessfulActivity.class);
                                            intent.putExtra("amount", tvTotalPayment.getText().toString());
                                            intent.putExtra("paymentType", tvPaymentMethod.getText().toString());
                                            intent.putExtra("time", dateString);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                });
            }
        }
    }

    private void addTransaction(String currentTime) {
        try {
            String transactionID = databaseTransactions.push().getKey();
            String transactionType = "Top up";
            Double transactionAmount = Double.parseDouble(tvTotalPayment.getText().toString());
            String transactionTime = currentTime;
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Transaction newTransaction = new Transaction(transactionID, transactionType, transactionAmount, transactionTime, userId);
            databaseTransactions.child(transactionID).setValue(newTransaction);
        } catch (NumberFormatException e) {
            Toast.makeText(TopUpDetailsActivity.this, "Please enter a valud number", Toast.LENGTH_LONG).show();
            tvTotalPayment.setText("");
            tvTotalPayment.requestFocus();
        }


    }
    private void addNotification(String currentTime){
        try{
            String notificationID = databaseNotification.push().getKey();
            String message = "You sucessfully top up " + tvTotalPayment.getText().toString() + " at " + currentTime;
            String notificationTime = currentTime;
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Notification notification = new Notification(notificationID, userID, message, notificationTime);
            databaseNotification.child(notificationID).setValue(notification);
        }catch (NumberFormatException e){
            // does nothing
        }
    }
}