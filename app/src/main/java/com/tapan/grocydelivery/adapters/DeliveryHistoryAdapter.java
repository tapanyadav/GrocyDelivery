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
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.models.DeliveryModel;

import java.util.Objects;

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
        String statusPick = "<u>" + model.getPickStatus() + "</u>";
        holder.textViewHistoryPickFrom.setText(Html.fromHtml(statusPick));
        holder.textViewHistoryDeliveryTo.setText(model.getDeliveredTo());

        holder.textViewHistoryPickFrom.setOnClickListener(v -> {
            ViewGroup viewGroup = holder.itemView.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.shop_details_dialog, viewGroup, false);
            openShopDialog(dialogView, model);
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history_view, parent, false);
        return new MyViewHolder(view);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserName, textViewUserAddress, textViewOrderId, textViewOrderDate, textViewDeliveryStatus, textViewHistoryPickFrom, textViewHistoryDeliveryTo;
        private ImageView imageViewUserImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.history_userName);
            textViewUserAddress = itemView.findViewById(R.id.history_userAddress);
            textViewOrderDate = itemView.findViewById(R.id.history_order_date);
            textViewOrderId = itemView.findViewById(R.id.history_id_order);
            textViewDeliveryStatus = itemView.findViewById(R.id.delivery_status);
            imageViewUserImage = itemView.findViewById(R.id.history_userImage);
            textViewHistoryPickFrom = itemView.findViewById(R.id.history_text_picked_from);
            textViewHistoryDeliveryTo = itemView.findViewById(R.id.history_delivery_to);
        }
    }
}
