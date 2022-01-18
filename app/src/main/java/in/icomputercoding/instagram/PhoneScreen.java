package in.icomputercoding.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.concurrent.TimeUnit;

import in.icomputercoding.instagram.databinding.ActivityPhoneScreenBinding;

public class PhoneScreen extends AppCompatActivity {

    ActivityPhoneScreenBinding binding;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        String phone = binding.signupPhoneNumber.getEditText().getText().toString().trim();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.signupNextButton.setOnClickListener(v -> {

            if (!phone.isEmpty()) {
                if (phone.length() == 10) {

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phone)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(PhoneScreen.this)
                            .setCallbacks(mCallbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(PhoneScreen.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            String phoneNo = "+" + binding.countryCodePicker.getSelectedCountryCode() + phone;
                            Intent intent = new Intent(getApplicationContext(), OtpVerifyScreen.class);
                            intent.putExtra("phoneNumber", phoneNo);
                            startActivity(intent);


                        }
                    };
                }
            }
        });


    }
}