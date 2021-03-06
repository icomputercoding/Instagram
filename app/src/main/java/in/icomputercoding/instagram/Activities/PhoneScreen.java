package in.icomputercoding.instagram.Activities;


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


import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.icomputercoding.instagram.databinding.ActivityPhoneScreenBinding;

public class PhoneScreen extends AppCompatActivity {

    ActivityPhoneScreenBinding binding;
    FirebaseAuth auth;
    String phone;
    String MobilePattern = "[0-9]{10}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();



        binding.NextBtn.setOnClickListener(v -> {

            phone = Objects.requireNonNull(binding.phoneNumber.getEditText()).getText().toString();
            String phoneNo = "+" + binding.countryCode.getSelectedCountryCode() + phone;
            if (phone.isEmpty()  || !phone.matches(MobilePattern)) {
                binding.phoneNumber.setError("Enter a valid mobile number");
            } else {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNo)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(PhoneScreen.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(PhoneScreen.this,e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String CodeOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(CodeOTP, forceResendingToken);
                                Intent intent = new Intent(PhoneScreen.this, OtpVerifyScreen.class);
                                intent.putExtra("phoneNumber", phoneNo);
                                intent.putExtra("CodeOTP",CodeOTP);
                                startActivity(intent);
                                finishAffinity();
                            }

                        }).build();

                PhoneAuthProvider.verifyPhoneNumber(options);


            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
