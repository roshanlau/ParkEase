package com.example.parkease.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkease.R;
import com.example.parkease.adapter.NotificationRecycleViewAdapter;
import com.example.parkease.databinding.FragmentNotificationsBinding;
import com.example.parkease.object.Notification;
import com.example.parkease.object.Parking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    LinearLayoutManager linearLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.rv_notification);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Notification> allUserNotification = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/")
                .getReference("notifications")
                .orderByChild("userID")
                .equalTo(getCurrentUserID());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allUserNotification.clear();

                for(DataSnapshot notificationDataSnapShot : snapshot.getChildren()){
                    Notification notification = notificationDataSnapShot.getValue(Notification.class);
                    Toast.makeText(getActivity(), notification.getNotificationID(), Toast.LENGTH_SHORT).show();
                    allUserNotification.add(notification);
                }

                NotificationRecycleViewAdapter notificationRecycleViewAdapter = new NotificationRecycleViewAdapter(getContext(), allUserNotification);
                recyclerView.setAdapter(notificationRecycleViewAdapter);
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

    public String getCurrentUserID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}