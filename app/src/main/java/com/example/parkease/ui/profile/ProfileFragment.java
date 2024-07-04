package com.example.parkease.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkease.MainActivity;
import com.example.parkease.R;
import com.example.parkease.databinding.FragmentNotificationsBinding;
import com.example.parkease.databinding.FragmentProfileBinding;
import com.example.parkease.object.User;
import com.example.parkease.ui.notifications.NotificationsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel mViewModel;

    DatabaseReference databaseUsers;
    EditText etName, etPhoneNumber, etEmail, etPassword, etICNumber, etDOB;
    Button btnLogout, btnEditSaveProfile;
    String currentBalance;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseUsers= FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");

        btnLogout = root.findViewById(R.id.btn_profile_signout);
        btnEditSaveProfile = root.findViewById(R.id.btn_profile_edit);

        etName = root.findViewById(R.id.et_profile_name);
        etPhoneNumber = root.findViewById(R.id.et_profile_phonenumber);
        etEmail = root.findViewById(R.id.et_profile_email);
        etPassword = root.findViewById(R.id.et_profile_password);
        etICNumber = root.findViewById(R.id.et_profile_IcNumber);
        etDOB = root.findViewById(R.id.et_profile_dob);

        etName.setEnabled(false);
        etPhoneNumber.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
        etICNumber.setEnabled(false);
        etDOB.setEnabled(false);

        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(currentUserUid == null){
            ((MainActivity)getActivity()).logout();
        }

        databaseUsers.child(currentUserUid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    User currentUser = task.getResult().getValue(User.class);
                    etName.setText(currentUser.getUserName());
                    etPhoneNumber.setText(currentUser.getUserPhoneNumber());
                    etEmail.setText(currentUser.getUserEmail());
                    etPassword.setText(currentUser.getUserPassword());
                    etICNumber.setText(currentUser.getUserICNumber());
                    etDOB.setText(currentUser.getUserDOB());
                    currentBalance = currentUser.getUserBalance();
                }else{
                    Toast.makeText(getActivity(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).logout();
            }
        });

        btnEditSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnEditSaveProfile.getText().toString().equals("Edit Profile")){
                    btnEditSaveProfile.setText("Save Profile");
                    etName.setEnabled(true);
                    etPhoneNumber.setEnabled(true);
                    etEmail.setEnabled(true);
                    etICNumber.setEnabled(true);
                    etDOB.setEnabled(true);
                    btnLogout.setEnabled(false);
                }else{
                    btnEditSaveProfile.setText("Edit Profile");
                    etName.setEnabled(false);
                    etPhoneNumber.setEnabled(false);
                    etEmail.setEnabled(false);
                    etICNumber.setEnabled(false);
                    etDOB.setEnabled(false);
                    btnLogout.setEnabled(true);

                    User updatedUser = new User(
                            currentUserUid,
                            etName.getText().toString(),
                            etPhoneNumber.getText().toString(),
                            etEmail.getText().toString(),
                            etPassword.getText().toString(),
                            etICNumber.getText().toString(),
                            etDOB.getText().toString(),
                            currentBalance
                    );
                    databaseUsers.child(currentUserUid).setValue(updatedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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