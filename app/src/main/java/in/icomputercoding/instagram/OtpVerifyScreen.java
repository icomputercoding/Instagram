package in.icomputercoding.instagram;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;

import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.concurrent.TimeUnit;

import in.icomputercoding.instagram.databinding.ActivityOtpVerifyScreenBinding;

public class OtpVerifyScreen extends AppCompatActivity {

    ActivityOtpVerifyScreenBinding binding;
    FirebaseAuth auth;
    private String mVerificationId;
    ProgressDialog progress;
    PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerifyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.setCanceledOnTouchOutside(false);

        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.otpDescriptionText.setText("Enter One Time Password Sent On " + phoneNumber);


        binding.verifyCode.setOnClickListener(v -> {

            String code = binding.pinView.getText().toString();

            if (!code.isEmpty()) {
                if (code != null) {

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId,code);
                    auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(OtpVerifyScreen.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });
                }
            } else {
                Toast.makeText(OtpVerifyScreen.this,"Please enter all number",Toast.LENGTH_SHORT).show();
                verifyCode(mVerificationId, code);
            }

        });


    }

    private void verifyCode(String verificationId, String code) {
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        progress.setMessage("Verifying Phone Number");
        progress.show();


        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneCredential(credential);

    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {

        progress.setMessage("Logging In");

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Intent intent = new Intent(OtpVerifyScreen.this,PhoneScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(OtpVerifyScreen.this,"Enter the correct OTP",Toast.LENGTH_SHORT);
                }
            }
        });
    }

}



