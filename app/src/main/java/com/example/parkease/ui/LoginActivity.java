package com.example.parkease.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkease.MainActivity;
import com.example.parkease.R;
import com.example.parkease.object.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    protected EditText etEmail, etPassword;
    protected Button btnLogin;
    protected TextView tvSignup;
    DatabaseReference databaseNotification = FirebaseDatabase.getInstance().getReference("notifications");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();


        etEmail = findViewById(R.id.et_loginact_email);
        etPassword = findViewById(R.id.et_loginact_password);
        btnLogin = findViewById(R.id.btn_loginact_login);
        tvSignup = findViewById(R.id.tv_loginact_signup);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();

                password = password.trim();
                email = email.trim();

                if(password.isEmpty() || email.isEmpty()){
                    //remind
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Please enter an email and password").setTitle("Warning").setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                addNotification();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

    private void addNotification(){
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = sdf.format(currentTime);
        String notificationID = databaseNotification.push().getKey();
        String message = "You sucessfully login at " + currentTime;
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Notification notification = new Notification(notificationID, userID, message, dateString);
        databaseNotification.child(notificationID).setValue(notification);
    }
}