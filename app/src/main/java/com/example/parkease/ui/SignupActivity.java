package com.example.parkease.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkease.MainActivity;
import com.example.parkease.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.parkease.object.User;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseUsers;

    EditText etName, etPhoneNumber, etEmail, etPassword, etICNumber, etDOB;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseUsers= FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");

        //initialise ui
        etName = findViewById(R.id.et_signupact_name);
        etPhoneNumber = findViewById(R.id.et_signupact_phone);
        etEmail = findViewById(R.id.et_signupact_email);
        etPassword = findViewById(R.id.et_signupact_password);
        etICNumber = findViewById(R.id.et_signupact_ic);
        etDOB = findViewById(R.id.et_signupact_dob);
        btnSignUp = findViewById(R.id.btn_signupact_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //process password and email
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();

                password = password.trim();
                email = email.trim();

                //check if password and email is empty
                if(password.isEmpty() || email.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("Please enter an email and password").setTitle("Warning").setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String userId = mFirebaseAuth.getUid();
                                double userBalance = 0.00;

                                User newUser = new User(
                                        userId,
                                        etName.getText().toString(),
                                        etPhoneNumber.getText().toString(),
                                        etEmail.getText().toString(),
                                        etPassword.getText().toString(),
                                        etICNumber.getText().toString(),
                                        etDOB.getText().toString(),
                                        userBalance
                                );

                                databaseUsers.child(userId).setValue(newUser);

                                goToMainActivity();
                            }else{
                                Toast.makeText(SignupActivity.this, "The account already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    private void goToMainActivity(){
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}