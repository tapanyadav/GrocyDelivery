package com.tapan.grocydelivery.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class UploadDocsActivity extends BaseActivity {

    Uri aadharFrontURI, aadharBackURI, dlURI, rcURI, panURI, passURI, chequeURI, marksheetUri;
    UploadTask image_front_path, image_back_path, image_rc_path, image_dl_path, image_pass_path, image_pan_path, image_cheque_path, image_marksheet_path;
    ImageView imageViewAadharFront, imageViewAadharBack, imageViewPan, imageViewDl, imageViewRc, imageViewPass, imageViewCancelledCheque, imageViewMarksheet;
    Button buttonSubmit;
    HashMap<String, Object> saveImage = new HashMap<>();
    byte[] imageAadharFront, imageAadharBack, imageRc, imageDl, imagePassbook, imagePanCard, imageMarksheet, imageCancelledCheque;
    private String image_name, delBoyName;
    private Bitmap compressedImageFile;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_upload_docs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.upload_docs_toolbar);
        imageViewAadharFront = findViewById(R.id.img_aadhar_front);
        imageViewAadharBack = findViewById(R.id.img_aadhar_back);
        imageViewPan = findViewById(R.id.img_pan);
        imageViewDl = findViewById(R.id.img_dl);
        imageViewRc = findViewById(R.id.img_rc);
        imageViewPass = findViewById(R.id.img_acc_details);
        imageViewCancelledCheque = findViewById(R.id.img_cancelled_check);
        imageViewMarksheet = findViewById(R.id.img_high_school_marksheet);
        buttonSubmit = findViewById(R.id.btn_submit);

        imageViewAadharFront.setOnClickListener(v -> {
            image_name = "image_aadhar_front";
            checkStoragePermission();
        });
        imageViewAadharBack.setOnClickListener(v -> {
            image_name = "image_aadhar_back";
            checkStoragePermission();
        });
        imageViewDl.setOnClickListener(v -> {
            image_name = "image_dl";
            checkStoragePermission();
        });
        imageViewRc.setOnClickListener(v -> {
            image_name = "image_rc";
            checkStoragePermission();
        });
        imageViewPan.setOnClickListener(v -> {
            image_name = "image_pan_card";
            checkStoragePermission();
        });
        imageViewPass.setOnClickListener(v -> {
            image_name = "image_acc_details";
            checkStoragePermission();
        });
        imageViewCancelledCheque.setOnClickListener(v -> {
            image_name = "image_cancelled_cheque";
            checkStoragePermission();
        });
        imageViewMarksheet.setOnClickListener(v -> {
            image_name = "image_marksheet";
            checkStoragePermission();
        });

        buttonSubmit.setOnClickListener(v -> {
            if (aadharFrontURI != null && aadharBackURI != null && dlURI != null && rcURI != null && chequeURI != null && marksheetUri != null && panURI != null && passURI != null) {
                showProgress(this);
                storeImage();
            }
        });
    }

    private void storeImage() {

        image_front_path = storageReference.child("Documents/").child("Aadhar/").child("Front/").child(getCurrentUserId() + "_" + delBoyName + "_" + UUID.randomUUID() + ".jpg").putBytes(imageAadharFront);
        image_back_path = storageReference.child("Documents/").child("Aadhar/").child("Back/").child(getCurrentUserId() + "_" + delBoyName + "_" + UUID.randomUUID() + ".jpg").putBytes(imageAadharBack);
        image_cheque_path = storageReference.child("Documents/").child("cancelledCheques/").child(getCurrentUserId() + "_" + delBoyName + "_" + UUID.randomUUID() + ".jpg").putBytes(imageCancelledCheque);
        image_dl_path = storageReference.child("Documents/").child("DL/").child(getCurrentUserId() + "_" + delBoyName + "_" + UUID.randomUUID() + ".jpg").putBytes(imageDl);
        image_pan_path = storageReference.child("Documents/").child("PAN/").child(getCurrentUserId() + "_" + delBoyName + "_" + UUID.randomUUID() + ".jpg").putBytes(imagePanCard);
        image_pass_path = storageReference.child("Documents/").child("accountDetails/").child(getCurrentUserId() + "_" + delBoyName + "_" + UUID.randomUUID() + ".jpg").putBytes(imagePassbook);
        image_rc_path = storageReference.child("Documents/").child("RC/").child(getCurrentUserId() + "_" + delBoyName + "_" + UUID.randomUUID() + ".jpg").putBytes(imageRc);
        image_marksheet_path = storageReference.child("Documents/").child("Marksheet/").child(getCurrentUserId() + "_" + delBoyName + "_" + UUID.randomUUID() + ".jpg").putBytes(imageMarksheet);

        uploadImage("image_aadhar_front", image_front_path);
        uploadImage("image_aadhar_back", image_back_path);
        uploadImage("image_cancelled_cheque", image_cheque_path);
        uploadImage("image_dl", image_dl_path);
        uploadImage("image_pan_card", image_pan_path);
        uploadImage("image_acc_details", image_pass_path);
        uploadImage("image_rc", image_rc_path);
        uploadImage("image_marksheet", image_marksheet_path);
    }

    void uploadImage(String imageName, UploadTask image_path) {
        image_path.addOnSuccessListener(taskSnapshot -> {
            Task<Uri> task = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();
            task.addOnSuccessListener(uri -> {
                String file = uri.toString();
                uploadDocuments(file, imageName);
            });
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Upload error", Toast.LENGTH_SHORT).show());
    }

    public void uploadDocuments(String generatedFilePath, String image_id) {

        Map<String, Object> addDoc = new HashMap<>();

        if ("image_aadhar_front".equals(image_id)) {
            addDoc.put("aadharFront", generatedFilePath);

        } else if ("image_aadhar_back".equals(image_id)) {
            addDoc.put("aadharBack", generatedFilePath);

        } else if ("image_rc".equals(image_id)) {
            addDoc.put("rc", generatedFilePath);

        } else if ("image_dl".equals(image_id)) {
            addDoc.put("dl", generatedFilePath);

        } else if ("image_acc_details".equals(image_id)) {
            addDoc.put("acc_details", generatedFilePath);

        } else if ("image_pan_card".equals(image_id)) {
            addDoc.put("panCard", generatedFilePath);

        } else if ("image_cancelled_cheque".equals(image_id)) {
            addDoc.put("cancelledCheque", generatedFilePath);

        } else if ("image_marksheet".equals(image_id)) {
            addDoc.put("marksheet", generatedFilePath);

        }

        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).collection("Documents").document("docs").update(addDoc).addOnCompleteListener(task1 -> {

            if ("image_aadhar_front".equals(image_id))
                Toast.makeText(this, "Please wait! your documents are uploading...", Toast.LENGTH_SHORT).show();

            HashMap<String, Object> updateStatus = new HashMap<>();
            if ("image_marksheet".equals(image_id)) {
                updateStatus.put("addDocumentStatus", true);
                firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).update(updateStatus);
                hideProgressDialog();
                Toast.makeText(this, "All documents are uploaded successfully...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            if (task1.isSuccessful()) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            } else {
                hideProgressDialog();
                String error = Objects.requireNonNull(task1.getException()).getMessage();
                Toast.makeText(this, "(Firestore Error) : " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(UploadDocsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadDocsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                bringImagePicker();
            }
        } else {
            bringImagePicker();
        }

    }

    private void bringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setMultiTouchEnabled(true)
                .start(UploadDocsActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                assert result != null;
                switch (image_name) {
                    case "image_aadhar_front":
                        aadharFrontURI = result.getUri();
                        imageViewAadharFront.setImageURI(aadharFrontURI);
                        compressImage(aadharFrontURI, image_name);
                        break;
                    case "image_aadhar_back":
                        aadharBackURI = result.getUri();
                        imageViewAadharBack.setImageURI(aadharBackURI);
                        compressImage(aadharBackURI, image_name);
                        break;
                    case "image_rc":
                        rcURI = result.getUri();
                        imageViewRc.setImageURI(rcURI);
                        compressImage(rcURI, image_name);
                        break;
                    case "image_dl":
                        dlURI = result.getUri();
                        imageViewDl.setImageURI(dlURI);
                        compressImage(dlURI, image_name);
                        break;
                    case "image_acc_details":
                        passURI = result.getUri();
                        imageViewPass.setImageURI(passURI);
                        compressImage(passURI, image_name);
                        break;
                    case "image_pan_card":
                        panURI = result.getUri();
                        imageViewPan.setImageURI(panURI);
                        compressImage(panURI, image_name);
                        break;
                    case "image_cancelled_cheque":
                        chequeURI = result.getUri();
                        imageViewCancelledCheque.setImageURI(chequeURI);
                        compressImage(chequeURI, image_name);
                        break;
                    case "image_marksheet":
                        marksheetUri = result.getUri();
                        imageViewMarksheet.setImageURI(marksheetUri);
                        compressImage(marksheetUri, image_name);
                        break;
                }

                if (aadharBackURI != null && aadharFrontURI != null && rcURI != null && panURI != null && passURI != null && dlURI != null && chequeURI != null && marksheetUri != null) {
                    buttonSubmit.setEnabled(true);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void compressImage(Uri getImageUri, String image_name) {

        File newImageFile = new File(Objects.requireNonNull(getImageUri.getPath()));
        firebaseFirestore.collection("DeliveryBoy").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(task.getResult()).exists()) {
                    delBoyName = task.getResult().getString("delName");
                }
            } else {
                String error = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
        try {
            compressedImageFile = new Compressor(this)
                    .setMaxHeight(500)
                    .setMaxWidth(500)
                    .setQuality(60)
                    .compressToBitmap(newImageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        if ("image_aadhar_front".equals(image_name)) {
            imageAadharFront = byteArrayOutputStream.toByteArray();
            saveImage.put("aadharFront", imageAadharFront);
        } else if ("image_aadhar_back".equals(image_name)) {
            imageAadharBack = byteArrayOutputStream.toByteArray();
            saveImage.put("aadharBack", imageAadharBack);
        } else if ("image_rc".equals(image_name)) {
            imageRc = byteArrayOutputStream.toByteArray();
            saveImage.put("rc", imageRc);
        } else if ("image_dl".equals(image_name)) {
            imageDl = byteArrayOutputStream.toByteArray();
            saveImage.put("dl", imageDl);
        } else if ("image_acc_details".equals(image_name)) {
            imagePassbook = byteArrayOutputStream.toByteArray();
            saveImage.put("acc_details", imagePassbook);
        } else if ("image_pan_card".equals(image_name)) {
            imagePanCard = byteArrayOutputStream.toByteArray();
            saveImage.put("panCard", imagePanCard);
        } else if ("image_cancelled_cheque".equals(image_name)) {
            imageCancelledCheque = byteArrayOutputStream.toByteArray();
            saveImage.put("cancelledCheque", imageCancelledCheque);
        } else if ("image_marksheet".equals(image_name)) {
            imageMarksheet = byteArrayOutputStream.toByteArray();
            saveImage.put("aadharFront", imageMarksheet);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(UploadDocsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}