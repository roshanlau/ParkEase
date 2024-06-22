package com.example.parkease.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkease.R;
import com.example.parkease.object.Parking;
import com.example.parkease.ui.ParkingPaymentActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ParkingRecyclerViewAdapter extends RecyclerView.Adapter<ParkingRecyclerViewAdapter.ParkingViewHolder> {

    public List<Parking> parkingList;
    private Context context;



    public ParkingRecyclerViewAdapter(Context context, List<Parking> parkingList) {
        this.context = context;
        this.parkingList = parkingList;
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View parking_row = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_row, null);

        ParkingViewHolder parkingVH = new ParkingViewHolder(parking_row);

        return parkingVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvParkingID.setText(parkingList.get(position).getParkingSpaceID());
        holder.tvParkingLocation.setText(getAddress(parkingList.get(position).getLatitude(), parkingList.get(position).getLongitude()));

        holder.btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(context, ParkingPaymentActivity.class);
                startIntent.putExtra("parkingID", parkingList.get(position).getParkingSpaceID());
                context.startActivity(startIntent);
            }
        });

        holder.btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + parkingList.get(position).getLatitude() + "," + parkingList.get(position).getLongitude() );
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });

        holder.tbStatus.setBackgroundColor(Color.GREEN);
        holder.tbStatus.setText("Available");

    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    public class ParkingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvParkingID, tvParkingLocation;
        public Button btnReserve, btnDirection;
        public ToggleButton tbStatus;



        public ParkingViewHolder(View itemView){
            super(itemView);
            tvParkingID = itemView.findViewById(R.id.tv_parkingRow_parkingID);
            tvParkingLocation = itemView.findViewById(R.id.tv_parkingRow_location);
            btnDirection = itemView.findViewById(R.id.btn_parkingRow_direction);
            btnReserve = itemView.findViewById(R.id.btn_parkingRow_reserve);
            tbStatus = itemView.findViewById(R.id.tb_parkingRow_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //does nothing


//            Toast.makeText(view.getContext(), "Flower Name: " +  flowerList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(view.getContext(), FlowerDetailActivity.class);
//            intent.putExtra("flowerName", flowerList.get(getAdapterPosition()).getName());
//
//            view.getContext().startActivity(intent);
        }


    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getLocality() + ", " + obj.getAdminArea();

            Log.v("IGA", "Address" + add);
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }
}
