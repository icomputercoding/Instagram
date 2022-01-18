package in.icomputercoding.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import com.google.firebase.FirebaseException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class OtpVerifyScreen extends AppCompatActivity {

    PinView pinFromUser;
    String codeBySystem;
    TextView otpDescriptionText;
    String phoneNumber;

    // [NEWLY Updated code  --  START declare_mAuth]
    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_verify_screen);

        //hooks
        pinFromUser = findViewById(R.id.pin_view);
        otpDescriptionText = findViewById(R.id.otp_description_text);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //Get all the data from Intent
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        otpDescriptionText.setText("Enter One Time Password Sent On " + phoneNumber);

        sendVerificationCodeToUser(phoneNumber);

    }

    private void sendVerificationCodeToUser(String phoneNo) {
        // [START start_phone_auth]
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth) //mAuth is defined on top
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(OtpVerifyScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(OtpVerifyScreen.this, SetUpProfileScreen.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OtpVerifyScreen.this, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}