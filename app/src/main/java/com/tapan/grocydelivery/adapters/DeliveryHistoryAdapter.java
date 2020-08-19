package com.tapan.grocydelivery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.models.DeliveryModel;

public class DeliveryHistoryAdapter extends FirestoreRecyclerAdapter<DeliveryModel, DeliveryHistoryAdapter.MyViewHolder> {

    private Context context;

    public DeliveryHistoryAdapter(@NonNull FirestoreRecyclerOptions<DeliveryModel> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull DeliveryModel model) {

        Glide.with(context).load(model.getUserImage()).into(holder.imageViewUserImage);
        holder.textViewUserName.setText(model.getUserName());
        holder.textViewUserAddress.setText(model.getUserAddress());
        holder.textViewOrderDate.setText(model.getOrderDate());
        holder.textViewOrderId.setText(model.getOrderNumberId());
        holder.textViewDeliveryStatus.setText(model.getOrderDeliveryStatus());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history_view, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserName, textViewUserAddress, textViewOrderId, textViewOrderDate, textViewDeliveryStatus;
        private ImageView imageViewUserImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.history_userName);
            textViewUserAddress = itemView.findViewById(R.id.history_userAddress);
            textViewOrderDate = itemView.findViewById(R.id.history_order_date);
            textViewOrderId = itemView.findViewById(R.id.history_id_order);
            textViewDeliveryStatus = itemView.findViewById(R.id.delivery_status);
            imageViewUserImage = itemView.findViewById(R.id.history_userImage);
        }
    }
}
