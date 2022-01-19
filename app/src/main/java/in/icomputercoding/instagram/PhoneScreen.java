package in.icomputercoding.instagram;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


import java.util.Objects;

import in.icomputercoding.instagram.databinding.ActivityPhoneScreenBinding;

public class PhoneScreen extends AppCompatActivity {

    ActivityPhoneScreenBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        binding.buttongetotp.setOnClickListener(v -> {

            String phone = Objects.requireNonNull(binding.signupPhoneNumber.getEditText()).getText().toString().trim();


            if (phone.isEmpty() || phone.length() < 10) {
                Toast.makeText(PhoneScreen.this, "Valid number is required", Toast.LENGTH_SHORT).show();
            }
            String phoneNo = "+" + binding.countryCodePicker.getSelectedCountryCode() + phone;
            Intent intent = new Intent(PhoneScreen.this, OtpVerifyScreen.class);
            intent.putExtra("phoneNumber", phoneNo);
            startActivity(intent);


        });
    }
}