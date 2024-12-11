package com.example.babycareapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.ActivityResetPasswordBinding;
import com.example.babycareapp.ui.viewmodel.AuthViewModel;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.buttonResetPassword.setOnClickListener(v->{
            String email = binding.editTextEmail.getText().toString();
            if(!email.isEmpty()){
                authViewModel.resetPassword(email).observe(this, isSuccess -> {
                    if(isSuccess){
                        Toast.makeText(this, "Kiểm tra email để thay đổi mật khẩu.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Lỗi khi gửi email, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.tvRegister.setOnClickListener(v->{
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        binding.tvLoginNow.setOnClickListener(v->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}