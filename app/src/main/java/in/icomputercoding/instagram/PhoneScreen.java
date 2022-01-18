package in.icomputercoding.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import in.icomputercoding.instagram.databinding.ActivityPhoneScreenBinding;

public class PhoneScreen extends AppCompatActivity {

    ActivityPhoneScreenBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.signupPhoneNumber.requestFocus();

        binding.signupNextButton.setOnClickListener(v -> {
            Intent intent = new Intent(PhoneScreen.this, OtpVerifyScreen.class);
            intent.putExtra("phoneNumber", Objects.requireNonNull(binding.signupPhoneNumber.getEditText()).getText().toString());
            startActivity(intent);
        });

    }
}