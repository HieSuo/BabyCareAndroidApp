package com.example.babycareapp.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;

    public AuthRepository(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
    }

    // Đăng ký tài khoản mới
    public LiveData<FirebaseUser> register(String email, String password) {
        MutableLiveData<FirebaseUser> userMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userMutableLiveData.setValue(firebaseAuth.getCurrentUser());
                        // Lưu trạng thái đăng nhập vào SharedPreferences
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                    } else {
                        userMutableLiveData.setValue(null);
                    }
                });
        return userMutableLiveData;
    }

    // Đăng nhập
    public LiveData<FirebaseUser> login(String email, String password) {
        MutableLiveData<FirebaseUser> userMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userMutableLiveData.setValue(firebaseAuth.getCurrentUser());
                        // Lưu trạng thái đăng nhập vào SharedPreferences
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                    } else {
                        userMutableLiveData.setValue(null);
                    }
                });
        return userMutableLiveData;
    }

    // Đăng xuất
    public void logout() {
        firebaseAuth.signOut();
        // Xóa trạng thái đăng nhập khỏi SharedPreferences
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
    }
    // Phương thức quên mật khẩu
    public LiveData<Boolean> resetPassword(String email) {
        MutableLiveData<Boolean> resetPasswordLiveData = new MutableLiveData<>();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        resetPasswordLiveData.setValue(true);
                    } else {
                        resetPasswordLiveData.setValue(false);
                    }
                });
        return resetPasswordLiveData;
    }
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}
