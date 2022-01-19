package in.icomputercoding.instagram;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;



import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;


import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.icomputercoding.instagram.databinding.ActivityOtpVerifyScreenBinding;

public class OtpVerifyScreen extends AppCompatActivity {

    ActivityOtpVerifyScreenBinding binding;
    FirebaseAuth auth;
    String verificationId;
    String phoneNumber, code;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerifyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.otpDescriptionText.setText("Enter One Time Password Sent On " + phoneNumber);
        sendVerificationCode(phoneNumber);


        binding.buttongetotp.setOnClickListener(v -> {

            code = Objects.requireNonNull(binding.pinView.getText()).toString();

            if (code.isEmpty() || code.length() < 6) {

                Toast.makeText(OtpVerifyScreen.this, "Enter OTP", Toast.LENGTH_SHORT).show();
            }
            verifyCode(code);
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Intent intent = new Intent(OtpVerifyScreen.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                    } else {
                        Toast.makeText(OtpVerifyScreen.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendVerificationCode(String number) {
        binding.progressbarVerifyOtp.setVisibility(View.VISIBLE);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OtpVerifyScreen.this)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                Toast.makeText(OtpVerifyScreen.this, "Verification Done...", Toast.LENGTH_SHORT).show();
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpVerifyScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}







