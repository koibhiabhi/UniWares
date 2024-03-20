package com.example.uniwares.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.Adapters.AdapterImagesPicked;
import com.example.uniwares.Models.ModelImagePicked;
import com.example.uniwares.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class transact_frag extends Fragment {

    private static final String TAG = "AD-CREATE-TAG";
    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    public Uri imageUri = null;
    private ArrayList<ModelImagePicked> imagePickedArrayList;
    private AdapterImagesPicked adapterImagesPicked;
    RecyclerView imagesRv;
    EditText brandEt, priceEt, titleEt, descriptionEt;
    AutoCompleteTextView categoryAct, conditionAct, locationAct;
    ImageView adimgbutton;
    Button postAdbtn;

    public transact_frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transact_frag, container, false);

        adimgbutton = view.findViewById(R.id.adimgbutton);
        imagesRv = view.findViewById(R.id.imagesRv);
        categoryAct = view.findViewById(R.id.categoryAct);
        conditionAct = view.findViewById(R.id.conditionAct);
//        locationAct = view.findViewById(R.id.locationAct);
        priceEt = view.findViewById(R.id.priceEt);
        titleEt = view.findViewById(R.id.titleEt);
        descriptionEt =  view.findViewById(R.id.descriptionEt);
        postAdbtn = view.findViewById(R.id.postAdbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        brandEt = view.findViewById(R.id.brandEt);
        adimgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickOption();
            }
        });

        postAdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        ArrayAdapter<String> adapterCateory = new ArrayAdapter<>(getContext(), R.layout.row_category_act, categories);
        categoryAct.setAdapter(adapterCateory);

        ArrayAdapter<String> adapterCondition = new ArrayAdapter<>(getContext(),R.layout.row_condition_act, conditions);
        conditionAct.setAdapter(adapterCondition);

        imagePickedArrayList = new ArrayList<>();
        loadImages();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        return view;
    }

    private void loadImages(){

        Log.d(TAG, "loadImages: ");
        adapterImagesPicked = new AdapterImagesPicked(getContext(), imagePickedArrayList);
        imagesRv.setAdapter(adapterImagesPicked);
    }
    
    private void showImagePickOption() {
        Log.d(TAG, "loadImages: ");

        PopupMenu popupMenu = new PopupMenu(getContext(), adimgbutton);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Camera");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Gallery");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == 1) {
                    // Handle camera option
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                        String cameraPermission[] = new String[]{Manifest.permission.CAMERA};
                        requestCameraPermission.launch(cameraPermission);
                    } else {
                        String cameraPermission[] = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestCameraPermission.launch(cameraPermission);
                    }
                } else if (itemId == 2) {
                    // Handle gallery option
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                        pickImageGallery();
                    } else {
                        String storagePermission[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
                return true;
            }
        });
    }
    private ActivityResultLauncher<String> requestStoragePermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    Log.d(TAG, "onActivityResult: isGranted: "+ isGranted);
                    if (isGranted){
                        pickImageGallery();
                    }else {
                        Toast.makeText(getContext(), "Storage permission denied..", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );
    
    private final ActivityResultLauncher<String[]> requestCameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    boolean areGranted = true;
                    for (Boolean isGranted : result.values()) {
                        areGranted = areGranted && isGranted;
                    }
                    if (areGranted) {
                        pickImagesCamera();
                        // Camera permission granted, no need to launch pickImagesCamera() here
                    } else {
                        Toast.makeText(getContext(), " Camera or Storage or both permission denied..", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );
    
    private void pickImagesCamera() {
        Log.d(TAG, "pickImageCamera: ");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "TEMPORARY_IMAGE");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "TEMPORARY_IMAGE_DESCRIPTION");
        imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityLauncher.launch(intent);
    }
    
    private void pickImageGallery() {
        Log.d(TAG, "pickImageGallery: ");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Check if data.getData() is null
                            imageUri = data.getData();
                            Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
                            String timestamp = " " + System.currentTimeMillis();
                            ModelImagePicked modelImagePicked = new ModelImagePicked(timestamp, imageUri, null, false);
                            imagePickedArrayList.add(modelImagePicked);
                            loadImages();
                        } else {
                            Toast.makeText(getContext(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    private final ActivityResultLauncher<Intent> cameraActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Log.d(TAG, "onActivityResult: imageUri: "+ imageUri);
                        String timestamp = " "+System.currentTimeMillis();
                        ModelImagePicked modelImagePicked = new ModelImagePicked(timestamp, imageUri, null, false);
                        imagePickedArrayList.add(modelImagePicked);
                        loadImages();
                    } else {
                        Toast.makeText(getContext(), "Cancelled...!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    public static final String[] categories = {
            "Books",
            "Mobiles",
            "Clothes",
            "Sports",
            "Electronics & Appliances",
            "Computer/Laptop",
            "Others"
    };

    public  static  final String[] conditions = {
            "New",
            "Used",
            "Refurbished",
    };

    private String brand = "";
    private String category = "";
//    private String address = "";
    private String condition = "";
    private String price = "";
    private String title = "";
    private String description = "";
//    private int latitude = 0;
//    private int longitude = 0;


    private void validateData(){

        Log.d(TAG, "validateData: " );
        brand = brandEt.getText().toString().trim();
        category = categoryAct.getText().toString().trim();
        condition = conditionAct.getText().toString().trim();
//        address = locationAct.getText().toString().trim();
        price = priceEt.getText().toString().trim();
        title = titleEt.getText().toString().trim();
        description = descriptionEt.getText().toString().trim();

        if (brand.isEmpty()){
            brandEt.setError("Enter Brand!");
            brandEt.requestFocus();

        } else if (category.isEmpty()){
            categoryAct.setError("Choose Category!");
            categoryAct.requestFocus();

        }  else if (condition.isEmpty()){
            conditionAct.setError("Choose Condition!");
            conditionAct.requestFocus();

//        } else if (address.isEmpty()){
//            locationAct.setError("Choose Loaction!");
//            locationAct.requestFocus();

        } else if (title.isEmpty()){
            titleEt.setError("Enter Title!");
            titleEt.requestFocus();

        } else if (description.isEmpty()){
            descriptionEt.setError("Enter Description!");
            descriptionEt.requestFocus();

        } else if (imagePickedArrayList.isEmpty()){
            Toast.makeText(getContext(), "Pick at least one image", Toast.LENGTH_SHORT).show();

        } else {
            postAd();
        }
    }
    public static final String AD_STATUS_AVAILABLE= "AVAILABLE";
    public static final String AD_STATUS_SOLD= "SOLD OUT";
    private void postAd(){
        progressDialog.setMessage("Publishing Ad");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        DatabaseReference userAdsRef = FirebaseDatabase.getInstance().getReference("Ads").child(firebaseAuth.getUid());
        String adId = userAdsRef.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", firebaseAuth.getUid());
        hashMap.put("brand", ""+brand);
        hashMap.put("category", ""+category);
        hashMap.put("condition", ""+condition);
//        hashMap.put("address", ""+address);
        hashMap.put("price", ""+price);
        hashMap.put("title", ""+title);
        hashMap.put("description", ""+description);
        hashMap.put("status", ""+AD_STATUS_AVAILABLE);
        hashMap.put("timestamp", timestamp);
//        hashMap.put("latitude", latitude);
//        hashMap.put("longitude", longitude);

        userAdsRef.child(adId).setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "onSuccess: Ad Published");
                    uploadImagesStorage(adId);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: " + e);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed to Publish" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImagesStorage(String adId) {
        int totalImagesToUpload = imagePickedArrayList.size();
        AtomicInteger uploadedImagesCounter = new AtomicInteger(0);

        for (int i = 0; i < imagePickedArrayList.size(); i++) {
            ModelImagePicked modelImagePicked = imagePickedArrayList.get(i);
            String imageName = modelImagePicked.getId();
            String filePathAndName = "Ads/" + firebaseAuth.getUid() + "/" + adId + "/" + imageName;
            int imageIndexForProgress = i + 1;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);

            storageReference.putFile(modelImagePicked.getImageUri())
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();

                        String message = "Uploading Image " + imageIndexForProgress + " of " + imagePickedArrayList.size() + " images...\nProgress " + (int) progress + "%";

                        Log.d(TAG, "onProgress: message" + message);

                        progressDialog.setMessage(message);
                        progressDialog.show();
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d(TAG, "onSuccess: ");

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri uploadedImageUrl = uriTask.getResult();

                        if (uriTask.isSuccessful()) {
                            HashMap<String, Object> imageHashMap = new HashMap<>();
                            imageHashMap.put("id", imageName);
                            imageHashMap.put("image", uploadedImageUrl.toString());

                            DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference()
                                    .child("Ads").child(firebaseAuth.getUid()).child(adId).child("Images").child(imageName);
                            imagesRef.setValue(imageHashMap);
                        }

                        if (uploadedImagesCounter.incrementAndGet() == totalImagesToUpload) {
                            progressDialog.dismiss();
                            showAdPublishedDialog();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "onFailure: " + e);
                        progressDialog.dismiss();
                    });
        }
    }

    private void resetFields() {
        brandEt.getText().clear();
        categoryAct.getText().clear();
        conditionAct.getText().clear();
//        locationAct.getText().clear();
        priceEt.getText().clear();
        titleEt.getText().clear();
        descriptionEt.getText().clear();
        imagePickedArrayList.clear();
        loadImages();
    }
    private void showAdPublishedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ad Published");
        builder.setMessage("Your ad has been published successfully!");
        builder.setPositiveButton("OK", (dialog, which) -> {
            resetFields(); // Reset the fields after the dialog is dismissed
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

