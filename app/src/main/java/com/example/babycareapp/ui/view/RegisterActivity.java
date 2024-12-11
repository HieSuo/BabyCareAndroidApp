package com.example.babycareapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.ActivityRegisterBinding;
import com.example.babycareapp.ui.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.buttonRegister.setOnClickListener(v->{
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String confirmPW = binding.editTextConfirmPassword.getText().toString();
            if(TextUtils.isEmpty(email)){
                binding.editTextEmail.setError("Email is required");
                return;
            }
            if(TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPW)){
                binding.editTextPassword.setError("Password is required");
                return;
            }
            if(!password.equals(confirmPW)){
                binding.editTextConfirmPassword.setError("Confirm password and password is not same");
                return;
            }
            if(password.length() <8){
                binding.editTextPassword.setError("Password must be at least 8 characters");
                return;
            }
            authViewModel.register(email, password).observe(this, user->{
                if(user!=null){
                    Toast.makeText(this, "Đăng kí thành công.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, "Đăng kí thất bại.", Toast.LENGTH_SHORT).show();
                }
            });

        });
        binding.tvLoginNow.setOnClickListener(v->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}