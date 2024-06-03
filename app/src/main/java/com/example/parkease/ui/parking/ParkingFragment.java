package com.example.parkease.ui.parking;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkease.R;
import com.example.parkease.databinding.FragmentParkingBinding;
import com.example.parkease.databinding.FragmentProfileBinding;

import javax.xml.transform.Templates;

public class ParkingFragment extends Fragment {
    private FragmentParkingBinding binding;
    private ParkingViewModel mViewModel;

    public static ParkingFragment newInstance() {
        return new ParkingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ParkingViewModel parkingViewModel = new ViewModelProvider(this).get(ParkingViewModel.class);

        binding = FragmentParkingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}