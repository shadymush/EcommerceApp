// AddArticleFragment.java
package com.example.myecommerce;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddArticleFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private ImageView imgArticleImage;
    private EditText etArticleName, etArticleDescription;
    private Button btnSelectImage, btnUploadArticle;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_article, container, false);

        imgArticleImage = view.findViewById(R.id.imgArticleImage);
        etArticleName = view.findViewById(R.id.etArticleName);
        etArticleDescription = view.findViewById(R.id.etArticleDescription);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnUploadArticle = view.findViewById(R.id.btnUploadArticle);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("articles");

        btnSelectImage.setOnClickListener(v -> chooseImage());

        btnUploadArticle.setOnClickListener(v -> uploadArticle());

        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imgArticleImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadArticle() {
        String name = etArticleName.getText().toString().trim();
        String description = etArticleDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etArticleName.setError("Article name is required.");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            etArticleDescription.setError("Article description is required.");
            return;
        }

        if (filePath != null) {
            StorageReference ref = storageReference.child("images/articles/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        String id = databaseReference.push().getKey();
                        Map<String, String> articleData = new HashMap<>();
                        articleData.put("id", id);
                        articleData.put("name", name);
                        articleData.put("description", description);
                        articleData.put("imageUrl", imageUrl);

                        databaseReference.child(id).setValue(articleData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getActivity(), "Article uploaded", Toast.LENGTH_SHORT).show();
                                    etArticleName.setText("");
                                    etArticleDescription.setText("");
                                    imgArticleImage.setImageResource(0);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Failed to upload article", Toast.LENGTH_SHORT).show();
                                });
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
}
