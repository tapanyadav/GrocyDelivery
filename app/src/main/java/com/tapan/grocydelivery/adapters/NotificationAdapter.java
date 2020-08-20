package com.tapan.grocydelivery.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.activities.MainActivity;
import com.tapan.grocydelivery.models.DeliveryModel;
import com.tapan.grocydelivery.utils.Constants;

import java.util.HashMap;
import java.util.Objects;

public class NotificationAdapter extends FirestoreRecyclerAdapter<DeliveryModel, NotificationAdapter.MyViewHolder> {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    HashMap<String, Object> updateStatus = new HashMap<>();
    private Context context;
    public ProgressDialog dialog;
    int badgeCountAll;
    HashMap<String, Object> updateAllCount = new HashMap<>();

    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<DeliveryModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull DeliveryModel model) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        holder.textViewShopName.setText(model.getShopName());
        holder.textViewShopAddress.setText(model.getShopAddress());
        holder.textViewOrderDateTime.setText(model.getOrderDateTime());
        Glide.with(context).load(model.getShopImage()).into(holder.imageViewShopImage);

        checkStatusData(holder.buttonAccept);

        holder.buttonReject.setOnClickListener(v -> showRejectDialog(v, getSnapshots().getSnapshot(position).getId()));
        holder.buttonAccept.setOnClickListener(v -> {
            updateStatusData(getSnapshots().getSnapshot(position).getId());
            showProgress(context);
        });
    }


    private void checkStatusData(Button buttonAcc) {

        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).collection(Constants.notificationCollection).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (document.exists()) {
                        if (Objects.equals(document.get("fragmentStatus"), "all") || Objects.equals(document.get("fragmentStatus"), "uDelivery")) {
                            Toast.makeText(context, "First, deliver the last added order...", Toast.LENGTH_LONG).show();
                            buttonAcc.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_noti_item, parent, false);
        return new MyViewHolder(view);
    }

    void updateStatusData(String documentId) {

        updateStatus.put("fragmentStatus", "all");
        updateStatus.put("orderDeliveryStatus", "Under packaging");
        updateStatus.put("allFragment", true);
        updateStatus.put("deliveryHistory", true);
        updateStatus.put("pickStatus", "Order pick from");
        updateStatus.put("deliveredTo", "Delivery to");

        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (Objects.requireNonNull(document.getData()).containsKey("badgeCountAll")) {
                    badgeCountAll = Integer.parseInt("" + document.get("badgeCountAll"));
                } else {
                    badgeCountAll = 0;
                }
            }
        });

        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).collection(Constants.notificationCollection).document(documentId)
                .update(updateStatus).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                badgeCountAll += 1;
                updateAllCount.put("badgeCountAll", badgeCountAll);
                firebaseFirestore.collection("DeliveryBoy").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                        .update(updateAllCount).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "All counter updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "not updated", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, "Order is added to all...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Firestore error!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showRejectDialog(View v, String id) {


        ViewGroup viewGroup = v.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.reject_dialog, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonCancel = alertDialog.findViewById(R.id.btn_cancel);
        Button buttonConfirm = alertDialog.findViewById(R.id.btn_confirm);

        assert buttonConfirm != null;
        buttonConfirm.setOnClickListener(v1 -> {
            updateStatus.put("fragmentStatus", "cancelled");
            firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).collection(Constants.notificationCollection).document(id)
                    .update(updateStatus).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Order is added to all...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Firestore error!!", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.dismiss();
        });
        assert buttonCancel != null;
        buttonCancel.setOnClickListener(v1 -> alertDialog.dismiss());
        ImageView imageViewClose = alertDialog.findViewById(R.id.warning_close);
        assert imageViewClose != null;
        imageViewClose.setOnClickListener(v1 -> alertDialog.dismiss());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewShopName, textViewShopAddress, textViewOrderDateTime;
        ImageView imageViewShopImage;
        Button buttonAccept, buttonReject;
        ViewGroup viewGroup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewShopName = itemView.findViewById(R.id.shopName);
            textViewShopAddress = itemView.findViewById(R.id.shopAddress);
            textViewOrderDateTime = itemView.findViewById(R.id.orderDateTime);
            imageViewShopImage = itemView.findViewById(R.id.shopImage);
            buttonAccept = itemView.findViewById(R.id.btn_accept);
            buttonReject = itemView.findViewById(R.id.btn_reject);
            viewGroup = itemView.findViewById(android.R.id.content);
        }
    }

    protected void showProgress(Context context) {
        dialog = new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.process_dialog);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }
}
