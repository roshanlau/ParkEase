package com.example.parkease.ui.ewallet;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkease.object.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EwalletViewModel extends ViewModel {
    private final MutableLiveData<List<com.example.parkease.object.Transaction>> transactions = new MutableLiveData<List<com.example.parkease.object.Transaction>>();
    private final MutableLiveData<String> userBalance = new MutableLiveData<>();
    // TODO: Implement the ViewModel
    DatabaseReference databaseUsers = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");

    Query query = FirebaseDatabase.getInstance()
            .getReference("transactions")
            .orderByChild("userId")
            .equalTo(getCurrentUserID());

    public EwalletViewModel(){

    }

    public MutableLiveData<List<Transaction>> getTransactions(){
        if(transactions.getValue() == null){
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<com.example.parkease.object.Transaction> tempTransactionList = new ArrayList<>();
                        for (DataSnapshot transactionDataSnapshot : snapshot.getChildren()){
                            com.example.parkease.object.Transaction transaction = transactionDataSnapshot.getValue(com.example.parkease.object.Transaction.class);
                            tempTransactionList.add(0, transaction);
                        }
                        transactions.postValue(tempTransactionList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return transactions;
    }


    public String getCurrentUserID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}