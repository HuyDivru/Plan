package com.example.plantext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {
    public static final int IMAGE_REQUEST=1;
    public static final int RESULT_OK=-1;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ImageView imageView;
    CircleImageView editImage;
    TextInputEditText editDescription;
    AppCompatButton editbtn;
    String key;
    String description;
    String image;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().hide();

        key = getIntent().getStringExtra("key");
        description = getIntent().getStringExtra("description");
        image = getIntent().getStringExtra("image");

        editDescription=findViewById(R.id.edit_description);
        editImage=findViewById(R.id.profile_image);
        editbtn=findViewById(R.id.edit_upload);


        imageView=findViewById(R.id.ic_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        editDescription.setText(description);
        Glide.with(this).load(image).into(editImage);
        databaseReference=FirebaseDatabase.getInstance().getReference("uploads");
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUpload();
            }
        });
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private void editUpload() {
        String editedDescription=editDescription.getText().toString().trim();
        if(imageUri!=null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String newImageUri = uri.toString();
                            // Update the item with the new description and image
                            updateItemInFirebase(editedDescription, newImageUri);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateItemInFirebase(String updatedDescription, String updatedImage) {
        if (key != null) {
            databaseReference.child(key).child("itemDescription").setValue(updatedDescription);
            databaseReference.child(key).child("itemImage").setValue(updatedImage);
            Toast.makeText(EditActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditActivity.this, "Không thể cập nhật vì key là null.", Toast.LENGTH_SHORT).show();
        }

    }

//    private boolean isDescription(){
//        if(!descriptionEdit.equals(editDescription.getText().toString())){
//            databaseReference.child(key).child("itemImage").setValue(editDescription.getText().toString());
//            descriptionEdit=editDescription.getText().toString();
//            return true;
//        }
//        else{
//            return false;
//        }
//    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
              imageUri=data.getData();
              Glide.with(this).load(imageUri).into(editImage);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}