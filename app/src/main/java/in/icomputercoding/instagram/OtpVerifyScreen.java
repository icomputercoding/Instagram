package in.icomputercoding.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import in.icomputercoding.instagram.databinding.ActivityOtpVerifyScreenBinding;

public class OtpVerifyScreen extends AppCompatActivity {

    ActivityOtpVerifyScreenBinding binding;
    FirebaseAuth auth;
    String verificationId;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify_screen);
    }
}