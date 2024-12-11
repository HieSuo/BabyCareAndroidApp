package com.example.babycareapp.ui.view.settingview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.ActivityProfileBinding;
import com.example.babycareapp.databinding.ActivityResetPasswordBinding;
import com.example.babycareapp.ui.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    AuthViewModel authViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.textinputEmail.setText(userId);
    }
}