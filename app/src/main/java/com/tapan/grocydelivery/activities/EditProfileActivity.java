package com.tapan.grocydelivery.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.tapan.grocydelivery.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class EditProfileActivity extends BaseActivity {

    EditText editTextName, editTextCity, editTextEmail;
    ImageView imageViewDelImage;
    Button buttonSave;
    CardView cardViewImageEdit;
    String userId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Uri mainImageURI;
    private Bitmap compressedImageFile;
    private boolean isChanged = false;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageViewDelImage = findViewById(R.id.userProfileEditImage);
        editTextName = findViewById(R.id.edit_name);
        editTextCity = findViewById(R.id.edit_city);
        editTextEmail = findViewById(R.id.edit_email);
        buttonSave = findViewById(R.id.btn_save_changes);
        cardViewImageEdit = findViewById(R.id.cardEditProf);

        loadData();

        userId = Objects.requireNonNull(MainActivity.profile_activity_data.get("userId")).toString();
        String del_name = Objects.requireNonNull(MainActivity.profile_activity_data.get("delName")).toString();

        cardViewImageEdit.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditProfileActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    BringImagePicker();
                }

            } else {
                BringImagePicker();
            }
        });

        buttonSave.setOnClickListener(v -> {
            showProgress(this);
            final String delName = editTextName.getText().toString().trim();
            final String delEmail = editTextEmail.getText().toString().trim();
            final String delCity = editTextCity.getText().toString().trim();

            if (!TextUtils.isEmpty(delName) && !TextUtils.isEmpty(delEmail) && !TextUtils.isEmpty(delCity) && mainImageURI != null) {
                if (isChanged) {
                    File newImageFile = new File(Objects.requireNonNull(mainImageURI.getPath()));
                    try {
                        compressedImageFile = new Compressor(this)
                                .setMaxHeight(150)
                                .setMaxWidth(150)
                                .setQuality(70)
                                .compressToBitmap(newImageFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] thumbData = baos.toByteArray();

                    UploadTask image_path = storageReference.child("profile_images/").child(userId + "_" + del_name + "_" + ".jpg").putBytes(thumbData);

                    image_path.addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> task = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();
                        task.addOnSuccessListener(uri -> {
                            String file = uri.toString();
                            storeFirestore(file, delName, delEmail, delCity);
                        });
                    });
                } else {
                    String oldImage = mainImageURI.toString();
                    storeFirestore(oldImage, delName, delEmail, delCity);
                }
            }
        });
    }

    void loadData() {
        if (MainActivity.profile_activity_data.size() != 0) {

            String name = Objects.requireNonNull(MainActivity.profile_activity_data.get("delName")).toString();
            String city = Objects.requireNonNull(MainActivity.profile_activity_data.get("delCity")).toString();
            String email = Objects.requireNonNull(MainActivity.profile_activity_data.get("delEmail")).toString();
            editTextName.setText(name);
            editTextCity.setText(city);
            editTextEmail.setText(email);
            if (MainActivity.profile_activity_data.containsKey("delProfileImage")) {
                String image = Objects.requireNonNull(MainActivity.profile_activity_data.get("delProfileImage")).toString();
                mainImageURI = Uri.parse(image);
                Glide.with(imageViewDelImage.getContext())
                        .load(image)
                        .into(imageViewDelImage);
            }

            editTextName.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    String textName = editTextName.getText().toString().trim();
                    String textEmail = editTextEmail.getText().toString().trim();
                    String textCity = editTextCity.getText().toString().trim();
                    if (textName.equals(name) && textEmail.equals(email) && textCity.equals(city)) {
                        buttonSave.setEnabled(false);
                    } else {
                        buttonSave.setEnabled(true);
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });

            editTextEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!editTextEmail.getText().toString().trim().matches(emailPattern)) {
                        editTextEmail.setBackgroundResource(R.drawable.round_corner_error);
                        editTextEmail.setError("Invalid email address");
                    } else {
                        editTextEmail.setBackgroundResource(R.drawable.round_corner);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String textName = editTextName.getText().toString().trim();
                    String textEmail = editTextEmail.getText().toString().trim();
                    String textCity = editTextCity.getText().toString().trim();
                    if (textName.equals(name) && textEmail.equals(email) && textCity.equals(city)) {
                        buttonSave.setEnabled(false);
                    } else {
                        buttonSave.setEnabled(true);
                    }
                }
            });

            editTextCity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String textName = editTextName.getText().toString().trim();
                    String textEmail = editTextEmail.getText().toString().trim();
                    String textCity = editTextCity.getText().toString().trim();
                    if (textName.equals(name) && textEmail.equals(email) && textCity.equals(city)) {
                        buttonSave.setEnabled(false);
                    } else {
                        buttonSave.setEnabled(true);
                    }
                }
            });
        }
    }


    private void storeFirestore(String generatedFilePath, String user_name, String user_email, String user_city) {

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("delName", user_name);
        userMap.put("delEmail", user_email);
        userMap.put("delCity", user_city);
        userMap.put("delProfileImage", generatedFilePath);

        firebaseFirestore.collection("DeliveryBoy").document(userId).update(userMap).addOnCompleteListener(task1 -> {

            if (task1.isSuccessful()) {
                hideProgressDialog();
                Toast.makeText(EditProfileActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                MainActivity.profile_activity_data.put("delName", user_name);
                MainActivity.profile_activity_data.put("delProfileImage", generatedFilePath);
                MainActivity.profile_activity_data.put("delEmail", user_email);
                MainActivity.profile_activity_data.put("delCity", user_city);
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                hideProgressDialog();
                String error = Objects.requireNonNull(task1.getException()).getMessage();
                Toast.makeText(EditProfileActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(EditProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                imageViewDelImage.setImageURI(mainImageURI);
                buttonSave.setEnabled(true);
                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        }
    }
}