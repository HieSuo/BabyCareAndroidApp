package com.example.babycareapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.ActivityLoginBinding;
import com.example.babycareapp.ui.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (authViewModel.isLoggedIn()) {
            // Nếu đã đăng nhập, chuyển sang màn hình chính
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.buttonLogin.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            authViewModel.login(email, password).observe(this, user->{
                if(user!=null){
                    Toast.makeText(this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "Dang nhap that bai", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.tvRegister.setOnClickListener(v->{
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        binding.tvForgotPassword.setOnClickListener(v->{
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
        });

    }
}