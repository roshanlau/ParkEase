package com.example.parkease.ui.ewallet;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkease.R;
import com.example.parkease.databinding.FragmentEwalletBinding;
import com.example.parkease.object.Transaction;
import com.example.parkease.ui.TopUpDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EwalletFragment extends Fragment {
    View root;
    ListView listview;

    TextView tvBalance;
    ImageButton btnTopUp;
    double currentBalance;

    DatabaseReference databaseUsers = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");

    private FragmentEwalletBinding binding;
    private EwalletViewModel mViewModel;
    private ArrayAdapter<String> adapter;
    public static EwalletFragment newInstance() {
        return new EwalletFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EwalletViewModel ewalletViewModel = new ViewModelProvider(this).get(EwalletViewModel.class);


        binding = FragmentEwalletBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        loadListView();

        ewalletViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                for (Transaction transaction: transactions)
                    adapter.insert(transaction.getTransactionType() + "\n" + transaction.getTransactionAmount() + "\n" + transaction.getTransactionTime(), 0);
            }
        });

        String currentUser = ewalletViewModel.getCurrentUserID();

        databaseUsers.child(currentUser).child("userBalance").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String userBalance = task.getResult().getValue(String.class);
                    currentBalance = Double.parseDouble(userBalance);
                    tvBalance.setText(String.format("%.2f", currentBalance));
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch user balance", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TopUpDetailsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadListView() {
        listview = root.findViewById(R.id.lv_ewallet_transaction);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
        listview.setAdapter(adapter);
    }

}