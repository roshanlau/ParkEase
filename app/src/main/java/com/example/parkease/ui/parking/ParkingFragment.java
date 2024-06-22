package com.example.parkease.ui.parking;

import androidx.core.graphics.PaintKt;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkease.R;
import com.example.parkease.adapter.ParkingRecyclerViewAdapter;
import com.example.parkease.databinding.FragmentParkingBinding;
import com.example.parkease.databinding.FragmentProfileBinding;
import com.example.parkease.object.Parking;
import com.example.parkease.object.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

public class ParkingFragment extends Fragment {
    private FragmentParkingBinding binding;
    private ParkingViewModel mViewModel;
    LinearLayoutManager linearLayoutManager;

    public static ParkingFragment newInstance() {
        return new ParkingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ParkingViewModel parkingViewModel = new ViewModelProvider(this).get(ParkingViewModel.class);

        binding = FragmentParkingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.rv_parking);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Parking> allAvailableParking = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/")
                .getReference("parking")
                .orderByChild("status")
                .equalTo(false);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allAvailableParking.clear();

                for (DataSnapshot parkingDataSnapShot : snapshot.getChildren()) {
                    Parking parking = parkingDataSnapShot.getValue(Parking.class);
                    allAvailableParking.add(parking);
                }

                ParkingRecyclerViewAdapter parkingRecyclerViewAdapter = new ParkingRecyclerViewAdapter(getContext(), allAvailableParking);
                recyclerView.setAdapter(parkingRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}