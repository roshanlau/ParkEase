package com.example.parkease.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parkease.MainActivity;
import com.example.parkease.R;

public class TopUpSuccessfulActivity extends AppCompatActivity {
    Button btn_topup_successful;
    TextView tv_amount, tv_payment_type, tv_time;
    String paymentType, time;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_successful);
        Intent intent = getIntent();
        amount = Double.parseDouble(intent.getStringExtra("amount"));
        paymentType = intent.getStringExtra("paymentType");
        time = intent.getStringExtra("time");
        tv_amount = findViewById(R.id.topupsuccess_tv_amount);
        tv_payment_type = findViewById(R.id.topupsuccess_tv_paymenttype);
        tv_time = findViewById(R.id.topupsuccess_tv_time);
        btn_topup_successful = findViewById(R.id.btn_topup_successful);
        tv_amount.setText("+"+String.format("%.2f",amount));
        tv_payment_type.setText(paymentType);
        tv_time.setText(time);

        btn_topup_successful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopUpSuccessfulActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}