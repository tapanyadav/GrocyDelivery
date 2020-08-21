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

public class DeliveryAllAdapter extends FirestoreRecyclerAdapter<DeliveryModel, DeliveryAllAdapter.MyViewHolder> {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Map<String, Object> updateStatus = new HashMap<>();
    private Context context;
    int badgeCountUnder, monthlyPoints, dailyCounts;
    HashMap<String, Object> updateUnderCount = new HashMap<>();

    public DeliveryAllAdapter(@NonNull FirestoreRecyclerOptions<DeliveryModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull DeliveryModel model) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        holder.textViewUserName.setText(model.getUserName());
        holder.textViewUserAddress.setText(model.getUserAddress());
        holder.textViewOrderId.setText(model.getOrderNumberId());
        holder.textViewOrderDateTime.setText(model.getOrderDateTime());
        holder.textViewDeliveryStatus.setText(model.getOrderDeliveryStatus());
        String statusPick = "<u>" + model.getPickStatus() + "</u>";
        holder.textViewPickFrom.setText(Html.fromHtml(statusPick));
        holder.textViewAllDeliveryTo.setText(model.getDeliveredTo());
        Glide.with(context).load(model.getUserImage()).placeholder(R.drawable.loading_photo).into(holder.imageViewUserImage);
        Glide.with(context).load(model.getMapLocation()).placeholder(R.drawable.loading_photo).into(holder.imageViewMapLocation);

        holder.textViewPickFrom.setOnClickListener(v -> {
            ViewGroup viewGroup = holder.itemView.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.shop_details_dialog, viewGroup, false);
            openShopDialog(dialogView, model);
        });


        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).collection("Notifications").document(getSnapshots().getSnapshot(position).getId())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;
                if (documentSnapshot.exists()) {
                    if (Objects.equals(documentSnapshot.get("fragmentStatus"), "all")) {
                        holder.buttonMarkPicked.setVisibility(View.VISIBLE);
                    } else {
                        holder.buttonMarkPicked.setVisibility(View.GONE);
                    }
                }
            }
        });
        holder.buttonMarkPicked.setOnClickListener(v -> {
            updateStatusData(getSnapshots().getSnapshot(position).getId());
            notifyDataSetChanged();
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
        Glide.with(context).load(deliveryModel.getShopImage()).placeholder(R.drawable.loading_photo).into(imageViewShopImage);

        assert imageViewClose != null;
        imageViewClose.setImageResource(R.drawable.ic_baseline_close);
        imageViewClose.setOnClickListener(v -> alertDialog.dismiss());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_delivery_view, parent, false);
        return new MyViewHolder(view);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserName, textViewUserAddress, textViewOrderId, textViewOrderDateTime, textViewDeliveryStatus, textViewPickFrom, textViewAllDeliveryTo;
        private ImageView imageViewUserImage, imageViewMapLocation;
        private Button buttonMarkPicked;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.userName);
            textViewUserAddress = itemView.findViewById(R.id.userAddress);
            textViewOrderDateTime = itemView.findViewById(R.id.all_order_time);
            textViewOrderId = itemView.findViewById(R.id.id_order);
            imageViewUserImage = itemView.findViewById(R.id.userImage);
            buttonMarkPicked = itemView.findViewById(R.id.btn_markReady);
            imageViewMapLocation = itemView.findViewById(R.id.mapSnapshot);
            textViewDeliveryStatus = itemView.findViewById(R.id.delivery_all_status);
            textViewPickFrom = itemView.findViewById(R.id.text_picked_from);
            textViewAllDeliveryTo = itemView.findViewById(R.id.all_delivery_to);
        }
    }

    void updateStatusData(String documentId) {


        updateStatus.put("fragmentStatus", "uDelivery");
        updateStatus.put("orderDeliveryStatus", "Under delivery");
        updateStatus.put("pickStatus", "Picked from");
        updateStatus.put("deliveredTo", "Delivery to");

        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (Objects.requireNonNull(document.getData()).containsKey("badgeCountUnder")) {
                    badgeCountUnder = Integer.parseInt("" + document.get("badgeCountUnder"));
                } else {
                    badgeCountUnder = 0;
                }
                if (Objects.requireNonNull(document.getData()).containsKey("monthlyPoints")) {
                    monthlyPoints = Integer.parseInt("" + document.get("monthlyPoints"));
                } else {
                    monthlyPoints = 0;
                }
                if (Objects.requireNonNull(document.getData()).containsKey("dailyCounts")) {
                    dailyCounts = Integer.parseInt("" + document.get("dailyCounts"));
                } else {
                    dailyCounts = 0;
                }
            }
        });


        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).collection(Constants.notificationCollection).document(documentId)
                .update(updateStatus).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                badgeCountUnder += 1;
                dailyCounts += 1;
                monthlyPoints += 1;
                updateUnderCount.put("badgeCountUnder", badgeCountUnder);
                updateUnderCount.put("dailyCounts", dailyCounts);
                updateUnderCount.put("monthlyPoints", monthlyPoints);

                firebaseFirestore.collection("DeliveryBoy").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                        .update(updateUnderCount).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "Under counter updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "not updated", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(context, "Order is added under delivery...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Firestore error!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
