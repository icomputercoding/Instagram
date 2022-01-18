package in.icomputercoding.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import in.icomputercoding.instagram.databinding.ActivityWelcomeScreenBinding;

public class WelcomeScreen extends AppCompatActivity {

    ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.agree.setOnClickListener(v -> {
            Intent i = new Intent(WelcomeScreen.this,PhoneScreen.class);
            startActivity(i);
            finish();
        });
    }
}