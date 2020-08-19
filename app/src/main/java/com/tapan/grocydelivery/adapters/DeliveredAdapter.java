package com.tapan.grocydelivery.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.models.DeliveryModel;

import java.util.Objects;

public class DeliveredAdapter extends FirestoreRecyclerAdapter<DeliveryModel, DeliveredAdapter.MyViewHolder> {

    Context context;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public DeliveredAdapter(@NonNull FirestoreRecyclerOptions<DeliveryModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull DeliveryModel model) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        holder.textViewDeliveredUserName.setText(model.getUserName());
        holder.textViewDeliveredUserAddress.setText(model.getUserAddress());
        holder.textViewDeliveredOrderId.setText(model.getOrderNumberId());
        holder.textViewDeliveredOrderDateTime.setText(model.getOrderDateTime());
        String statusPick = "<u>" + model.getPickStatus() + "</u>";
        holder.textViewPickedDelivered.setText(Html.fromHtml(statusPick));
        Glide.with(context).load(model.getUserImage()).into(holder.imageViewDeliveredUserImage);
        Glide.with(context).load(model.getMapLocation()).into(holder.imageViewDeliveredMapLocation);

        holder.textViewPickedDelivered.setOnClickListener(v -> {
            ViewGroup viewGroup = holder.itemView.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.shop_details_dialog, viewGroup, false);
            openShopDialog(dialogView, model);
        });

    }

    private void openShopDialog(View dialogView, DeliveryModel deliveryModel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        TextView textViewShopName = alertDialog.findViewById(R.id.all_delivery_shopName);
        TextView textViewShopAddress = alertDialog.findViewById(R.id.all_delivery_shopAddress);
        ImageView imageViewShopImage = alertDialog.findViewById(R.id.all_delivery_shopImage);
        ImageView imageViewClose = alertDialog.findViewById(R.id.close_image);

        assert textViewShopName != null;
        textViewShopName.setText(deliveryModel.getShopName());
        assert textViewShopAddress != null;
        textViewShopAddress.setText(deliveryModel.getShopAddress());
        assert imageViewShopImage != null;
        Glide.with(context).load(deliveryModel.getShopImage()).into(imageViewShopImage);
        assert imageViewClose != null;
        imageViewClose.setImageResource(R.drawable.ic_baseline_close);
        imageViewClose.setOnClickListener(v -> alertDialog.dismiss());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_delivered_view, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDeliveredUserName, textViewDeliveredUserAddress, textViewDeliveredOrderId, textViewDeliveredOrderDateTime, textViewPickedDelivered;
        ImageView imageViewDeliveredUserImage, imageViewDeliveredMapLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDeliveredUserName = itemView.findViewById(R.id.delivered_userName);
            textViewDeliveredUserAddress = itemView.findViewById(R.id.delivered_userAddress);
            textViewDeliveredOrderDateTime = itemView.findViewById(R.id.delivered_order_time);
            textViewDeliveredOrderId = itemView.findViewById(R.id.delivered_id_order);
            imageViewDeliveredUserImage = itemView.findViewById(R.id.delivered_userImage);
            imageViewDeliveredMapLocation = itemView.findViewById(R.id.delivered_mapSnapshot);
            textViewPickedDelivered = itemView.findViewById(R.id.text_delivered_picked_from);

        }
    }
}
