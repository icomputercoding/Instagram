package in.icomputercoding.instagram.Activities;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.Objects;

import in.icomputercoding.instagram.Model.User;
import in.icomputercoding.instagram.databinding.ActivitySetUpProfileScreenBinding;

public class SetUpProfileScreen extends AppCompatActivity {

    ActivitySetUpProfileScreenBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;
    ActivityResultLauncher<String> getContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetUpProfileScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> binding.ProfileImage.setImageURI(result));

        binding.ProfileImage.setOnClickListener(v -> getContent.launch("image/*"));

        binding.SubmitBtn.setOnClickListener(v -> {
            String name = Objects.requireNonNull(binding.name.getEditText()).getText().toString();

            if (name.isEmpty()) {
                binding.name.setError("Please type a name");
                return;
            }

            dialog.show();
            if (selectedImage != null) {
                StorageReference reference = storage.getReference().child("Profiles").child(Objects.requireNonNull(auth.getUid()));
                reference.putFile(selectedImage).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            String uid = auth.getUid();
                            String phone = Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber();
                            String name1 = binding.name.getEditText().getText().toString();

                            User user = new User(uid, name1, phone, imageUrl);

                            database.getReference()
                                    .child("users")
                                    .child(uid)
                                    .setValue(user)
                                    .addOnSuccessListener(aVoid -> {
                                        dialog.dismiss();
                                        Intent intent = new Intent(SetUpProfileScreen.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                        });
                    }
                });
            } else {
                String uid = auth.getUid();
                String phone = Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber();

                User user = new User(uid, name, phone, "No Image");

                database.getReference()
                        .child("users")
                        .child(Objects.requireNonNull(uid))
                        .setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            dialog.dismiss();
                            Intent intent = new Intent(SetUpProfileScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
            }

        });
    }

}