package com.bStudio.petcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UploadPost extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE1 = 101;
    private static final int REQUEST_CODE_IMAGE2 = 102;
    private ImageView imageViewAdd1;
    private ImageView imageViewAdd2;
    private EditText inputName;
    private EditText inputBreed;
    private EditText about;
    private Spinner spinnerPetType;
    private EditText inputContactNumber;

    ProgressDialog dialog;
    private Button uploadBtn;

    Uri image1Uri;
    Uri image2Uri;
    boolean isImage1Added = false;
    boolean isImage2Added = false;

    DatabaseReference DataRef;
    StorageReference StorageRef;

    private StorageTask mUploadTask;


    private double latitude;
    private double longitude;
    private String address;

    private String district;


    Spinner spinner;

    private SimpleDateFormat dateFormat;
    String[] pet_type = {"Dog", "Cat", "Bird", "Other"};

    private RadioGroup radioGroupGender;
    private  RadioGroup radioGroupSize;
    private RadioGroup radioGroupAge;

    private static final int REQUEST_CODE_MAP = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);


        // spinner
        spinner = findViewById(R.id.pettype);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadPost.this, android.R.layout.simple_spinner_item, pet_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroupGender = findViewById(R.id.gendergroup);
        radioGroupSize = findViewById(R.id.sizegroup);
        radioGroupAge = findViewById(R.id.agegroup);

        imageViewAdd1 = findViewById(R.id.image1);
        imageViewAdd2 = findViewById(R.id.image2);
        inputName = findViewById(R.id.inputName);
        inputBreed = findViewById(R.id.inputBreed);
        about = findViewById(R.id.inputAbout);
        spinnerPetType = findViewById(R.id.pettype);
        inputContactNumber = findViewById(R.id.inputcontactnumber);
        uploadBtn = findViewById(R.id.uploadBtn);



        //uploading dialog box
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setTitle("Post Uploading");
        dialog.setCanceledOnTouchOutside(false);



        DataRef = FirebaseDatabase.getInstance().getReference().child("Pets");
        StorageRef = FirebaseStorage.getInstance().getReference().child("PetImage");

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        imageViewAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE1);
            }
        });

        imageViewAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();;

                String name = inputName.getText().toString().trim();
                String breed = inputBreed.getText().toString().trim();
                String aboutText = about.getText().toString().trim();
                String petType = spinnerPetType.getSelectedItem().toString();
                String contactNumber = inputContactNumber.getText().toString().trim();


                // Get the selected gender from the radio group
                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                String gender;
                if (selectedGenderId == R.id.male) {
                    gender = "Male";
                } else if (selectedGenderId == R.id.female) {
                    gender = "Female";
                } else {
                    gender = "Unknown";
                }

                // Get the selected size from the radio group
                int selectSizeId = radioGroupSize.getCheckedRadioButtonId();
                String size;
                if (selectSizeId == R.id.small){
                    size = "Small";
                } else if (selectSizeId == R.id.medium) {
                    size = "Medium";
                } else if (selectSizeId == R.id.large) {
                    size = "Large";
                } else {
                    size =  "Unknown";
                }

                // Get the selected size from the radio group
                int selectAgeId = radioGroupAge.getCheckedRadioButtonId();
                String age;
                if (selectAgeId == R.id.baby){
                    age = "Baby";
                } else if (selectAgeId == R.id.young) {
                    age = "Young";
                } else if (selectAgeId == R.id.adult) {
                    age = "Adult";
                } else {
                    age =  "Unknown";
                }


                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadPost.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                } else if (isImage1Added && isImage2Added && !name.isEmpty() && !gender.isEmpty() && !size.isEmpty() && !aboutText.isEmpty() && !contactNumber.isEmpty()) {
                    handleLocationUpdates();
                    uploadImage(name, petType, contactNumber,aboutText,gender,breed,size,age);
                } else {
                    Toast.makeText(UploadPost.this, "Please fill all fields and add images", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });


        //Map Start
        Button addLocationBtn = findViewById(R.id.addLocationBtn);
        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(UploadPost.this, MapActivity.class);
                startActivityForResult(mapIntent, REQUEST_CODE_MAP);
            }
        });



    }

    private void uploadImage(final String name, final String petType,final String aboutText, final String contactNumber,final String gender,final String breed,final String size, final String age) {
        final String postId = DataRef.push().getKey();
        final StorageReference imageRef1 = StorageRef.child(postId).child("image1.jpg");
        final StorageReference imageRef2 = StorageRef.child(postId).child("image2.jpg");

        UploadTask uploadTask1 = imageRef1.putFile(image1Uri);
        UploadTask uploadTask2 = imageRef2.putFile(image2Uri);

        uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    final Uri imageUri1 = task.getResult();
                    uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imageRef2.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri imageUri2 = task.getResult();

                                HashMap<String, Object> postMap = new HashMap<>();
                                //contactNumber and About change
                                postMap.put("name", name);
                                postMap.put("pettype", petType);
                                postMap.put("gender", gender);
                                postMap.put("breed",breed);
                                postMap.put("size",size);
                                postMap.put("age",age);
                                postMap.put("contactNumber", aboutText);
                                postMap.put("About",contactNumber);
                                postMap.put("image1", imageUri1.toString());
                                postMap.put("image2", imageUri2.toString());
                                postMap.put("PublishDate", getCurrentDate());
                                postMap.put("address", address);
                                postMap.put("latitude", latitude);
                                postMap.put("longitude", longitude);
                                postMap.put("district",district);

                                DataRef.child(postId).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();

                                        if (task.isSuccessful()) {
                                            Toast.makeText(UploadPost.this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(UploadPost.this, "Failed to upload post", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(UploadPost.this, "Failed to retrieve image URLs", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(UploadPost.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getCurrentDate() {
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private void handleLocationUpdates() {
        // You can implement the code for handling location updates here
        // For example, you can use the FusedLocationProviderClient to get the current device location
        // and save it along with the other pet details in the database
        HashMap<String, Object> locationMap = new HashMap<>();
        locationMap.put("address", address);
        locationMap.put("latitude", latitude);
        locationMap.put("longitude", longitude);
        locationMap.put("district",district);
        // Save the location to the database
        //DataRef.child(postId).child("location").setValue(locationMap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image1Uri = data.getData();
            isImage1Added = true;
            imageViewAdd1.setImageURI(image1Uri);
        }

        if (requestCode == REQUEST_CODE_IMAGE2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image2Uri = data.getData();
            isImage2Added = true;
            imageViewAdd2.setImageURI(image2Uri);
        }

        if (requestCode == REQUEST_CODE_MAP && resultCode == RESULT_OK && data != null) {
            latitude = data.getDoubleExtra("latitude", 0.0);
            longitude = data.getDoubleExtra("longitude", 0.0);
            address = data.getStringExtra("address");
            district = data.getStringExtra("district");

            // Update your UI with the latitude, longitude, and address
            // For example, you can display the address in a TextView
//            TextView addressTextView = findViewById(R.id.addressTextView);
//            addressTextView.setText(address);
        }

        if (requestCode == REQUEST_CODE_MAP && resultCode == RESULT_OK && data != null) {
            latitude = data.getDoubleExtra("latitude", 0.0);
            longitude = data.getDoubleExtra("longitude", 0.0);
            address = data.getStringExtra("address");
            district = data.getStringExtra("district");
        }
    }
}
