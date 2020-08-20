package com.tapan.grocydelivery.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.models.DeliveryModel;
import com.tapan.grocydelivery.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UnderDeliveryAdapter extends FirestoreRecyclerAdapter<DeliveryModel, UnderDeliveryAdapter.MyViewHolder> {

    Context context;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Map<String, Object> updateStatus = new HashMap<>();
    HashMap<String, Object> updateDeliveryCount = new HashMap<>();
    int delCount;

    public UnderDeliveryAdapter(@NonNull FirestoreRecyclerOptions<DeliveryModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull DeliveryModel model) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        holder.textViewUnderUserName.setText(model.getUserName());
        holder.textViewUnderUserAddress.setText(model.getUserAddress());
        holder.textViewUnderOrderId.setText(model.getOrderNumberId());
        holder.textViewUnderOrderDateTime.setText(model.getOrderDateTime());
        String statusPick = "<u>" + model.getPickStatus() + "</u>";
        holder.textViewPickedUnder.setText(Html.fromHtml(statusPick));
        Glide.with(context).load(model.getUserImage()).into(holder.imageViewUnderUserImage);
        Glide.with(context).load(model.getMapLocation()).into(holder.imageViewUnderMapLocationTrack);

        holder.buttonUnderMarkDelivered.setOnClickListener(v -> {
            updateStatusData(getSnapshots().getSnapshot(position).getId());
            Toast.makeText(context, "Order add to under delivery...", Toast.LENGTH_SHORT).show();
        });

        holder.textViewPickedUnder.setOnClickListener(v -> {
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
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_under_delivery_view, parent, false);
        return new MyViewHolder(view);
    }

    void updateStatusData(String documentId) {

        updateStatus.put("fragmentStatus", "delivered");
        updateStatus.put("orderDeliveryStatus", "Delivered");
        updateStatus.put("deliveredTo", "Delivered to");


        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (Objects.requireNonNull(document.getData()).containsKey("totalDeliveries")) {
                    delCount = Integer.parseInt("" + document.get("totalDeliveries"));
                } else {
                    delCount = 0;
                }
            }
        });

        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).collection(Constants.notificationCollection).document(documentId)
                .update(updateStatus).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                delCount += 1;
                updateDeliveryCount.put("totalDeliveries", delCount);
                updateDeliveryCount.put("badgeCountUnder", 0);
                firebaseFirestore.collection("DeliveryBoy").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                        .update(updateDeliveryCount).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "total deliveries updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "not updated", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(context, "Order is delivered...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Firestore error!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUnderUserName, textViewUnderUserAddress, textViewUnderOrderId, textViewUnderOrderDateTime, textViewPickedUnder;
        ImageView imageViewUnderUserImage, imageViewUnderMapLocationTrack;
        Button buttonUnderMarkDelivered;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUnderUserName = itemView.findViewById(R.id.under_userName);
            textViewUnderUserAddress = itemView.findViewById(R.id.under_userAddress);
            textViewUnderOrderDateTime = itemView.findViewById(R.id.under_all_order_time);
            textViewUnderOrderId = itemView.findViewById(R.id.under_id_order);
            textViewPickedUnder = itemView.findViewById(R.id.text_under_picked_from);
            imageViewUnderUserImage = itemView.findViewById(R.id.under_userImage);
            buttonUnderMarkDelivered = itemView.findViewById(R.id.under_btn_markDelivered);
            imageViewUnderMapLocationTrack = itemView.findViewById(R.id.under_mapSnapshot);
        }
    }

}
