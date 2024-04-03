package com.example.parkease.ui.ewallet;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EwalletViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    DatabaseReference databaseUsers = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");
    public EwalletViewModel(){

    }

    public String getCurrentUserID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}