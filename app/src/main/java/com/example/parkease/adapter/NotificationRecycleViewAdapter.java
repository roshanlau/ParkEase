package com.example.parkease.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkease.R;
import com.example.parkease.object.Notification;


import java.util.List;

public class NotificationRecycleViewAdapter extends RecyclerView.Adapter<NotificationRecycleViewAdapter.NotificationViewHolder>{
    public List<Notification> notificationList;
    private Context context;



    public NotificationRecycleViewAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View notification_row = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, null);

        NotificationViewHolder notificationVH = new NotificationViewHolder(notification_row);

        return notificationVH;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //set Text here
        holder.tvMessage.setText(notificationList.get(position).getMessage());
        holder.tvTime.setText(notificationList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvTime, tvMessage;

        public NotificationViewHolder(View itemView){
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_notificationRow_time);
            tvMessage = itemView.findViewById(R.id.tv_notificationRow_message);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //does nothing

        }


    }
}
