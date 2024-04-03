package com.example.parkease.ui.ewallet;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkease.R;
import com.example.parkease.databinding.FragmentEwalletBinding;

public class EwalletFragment extends Fragment {

    private FragmentEwalletBinding binding;
    private EwalletViewModel mViewModel;

    public static EwalletFragment newInstance() {
        return new EwalletFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EwalletViewModel ewalletViewModel = new ViewModelProvider(this).get(EwalletViewModel.class);


        binding = FragmentEwalletBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}