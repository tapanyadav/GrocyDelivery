package com.tapan.grocydelivery.messaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.activities.MainActivity;
import com.tapan.grocydelivery.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FirebaseMessaging extends FirebaseMessagingService {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();

        sendNotification(Objects.requireNonNull(notification), data);
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_icon);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notificationFragment", "Notification Fragment");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo(notification.getTitle())
                .setLargeIcon(icon)
                .setColor(getResources().getColor(R.color.colorPrimary, getTheme()))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_notification_icon);

        try {
            String picture_url = data.get("picture_url");

            HashMap<String, Object> notificationHashMap = new HashMap<>();
            notificationHashMap.put("userImage", data.get("userImage"));
            notificationHashMap.put("userName", data.get("userName"));
            notificationHashMap.put("userAddress", data.get("userAddress"));
            notificationHashMap.put("shopImage", data.get("shopImage"));
            notificationHashMap.put("shopName", data.get("shopName"));
            notificationHashMap.put("shopAddress", data.get("shopAddress"));
            notificationHashMap.put("orderDateTime", data.get("orderDateTime"));
            notificationHashMap.put("orderNumberId", data.get("orderNumberId"));
            notificationHashMap.put("mapLocation", data.get("mapLocation"));
            notificationHashMap.put("orderDate", data.get("orderDate"));
            notificationHashMap.put("orderDeliveryStatus", data.get("orderDeliveryStatus"));
            notificationHashMap.put("fragmentStatus", data.get("fragmentStatus"));
            notificationHashMap.put("pickStatus", data.get("pickStatus"));

            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();

            firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (Objects.equals(document.get("deliveryStatus"), true)) {
                        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                                .collection(Constants.notificationCollection).add(notificationHashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d(Constants.messagingTag, "Notification data stores successfully.");
                            } else {
                                Log.d(Constants.messagingTag, "Notification data not stores successfully.");
                            }
                        });
                    }
                }
            });

            Log.d(Constants.messagingTag, "shopN: " + data.get("shopName"));
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                                .bigLargeIcon(bigPicture)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("channel description");
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.describeContents();
            channel.enableLights(true);
            channel.setLightColor(R.color.colorPrimary);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 300, 200, 100});
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
