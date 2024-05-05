package com.example.parkease.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.parkease.R;
import com.example.parkease.model.DataModel;
import com.example.parkease.ui.TopUpDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodsChildAdapter extends RecyclerView.Adapter<PaymentMethodsChildAdapter.ItemViewHolder> {
    private String amount;
    private List<DataModel> mList;
    private List<String> list = new ArrayList<>();
    //new
    private Context context;

    //new argument context pass
    public PaymentMethodsChildAdapter(String amount,Context context, List<DataModel> mList){
        this.amount = amount;
        this.mList  = mList;
        this.context=context;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_payment_methods_child_adapter , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        DataModel model = mList.get(position);
        holder.mTextView.setText(model.getItemText());

        boolean isExpandable = model.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable){
            holder.mArrowImage.setImageResource(R.drawable.arrow_right_24);
        }else{
            holder.mArrowImage.setImageResource(R.drawable.arrow_down_24);
        }

        PaymentMethodsParentAdapter adapter = new PaymentMethodsParentAdapter(list);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);

        //new 56-66
        PaymentMethodsParentAdapter ParentAdapter = new PaymentMethodsParentAdapter(model.getNestedList());

        adapter.setOnItemClickListener(new PaymentMethodsParentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle click event for child item
                String clickedItem = model.getNestedList().get(position);
                // Implement your logic here
                Intent intent = new Intent(context, TopUpDetailsActivity.class);
                intent.putExtra("nestedItem",clickedItem);
                intent.putExtra("amount",amount);
                context.startActivity(intent);

            }
        });

        holder.nestedRecyclerView.setAdapter(adapter);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable());
                list = model.getNestedList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        private TextView mTextView;
        private ImageView mArrowImage;
        private RecyclerView nestedRecyclerView;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mTextView = itemView.findViewById(R.id.itemTv);
            mArrowImage = itemView.findViewById(R.id.arro_imageview);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
        }
    }
}