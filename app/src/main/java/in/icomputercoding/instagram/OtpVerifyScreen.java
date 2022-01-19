package in.icomputercoding.instagram;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;


import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.icomputercoding.instagram.databinding.ActivityOtpVerifyScreenBinding;

public class OtpVerifyScreen extends AppCompatActivity {

    ActivityOtpVerifyScreenBinding binding;
    FirebaseAuth auth;
    private String verificationId = "";
    String phoneNumber, code;
    private boolean otpSent = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerifyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();

        if (otpSent) {
            final String getOtp = binding.pinView.getText().toString();

            if (verificationId.isEmpty()) {
                Toast.makeText(OtpVerifyScreen.this, "Unable to verify OTP", Toast.LENGTH_SHORT).show();
            } else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, getOtp);

                auth.signInWithCredential(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        Toast.makeText(OtpVerifyScreen.this, "Verified", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OtpVerifyScreen.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            phoneNumber = getIntent().getStringExtra("phoneNumber");
            binding.otpDescriptionText.setText("Enter One Time Password Sent On " + phoneNumber);
            sendVerificationCode(phoneNumber);
        }


        binding.buttongetotp.setOnClickListener(v -> {

            code = Objects.requireNonNull(binding.pinView.getText()).toString();

            if (code.isEmpty() || code.length() < 6) {
                Toast.makeText(OtpVerifyScreen.this, "Enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                signInWithCredential(credential);
            }
        });
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
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OtpVerifyScreen.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                            signInWithCredential(credential);
                            Toast.makeText(OtpVerifyScreen.this, "OTP Sent...", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OtpVerifyScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        otpSent = true;
                    }

                }).build();


        PhoneAuthProvider.verifyPhoneNumber(options);

    }

}







