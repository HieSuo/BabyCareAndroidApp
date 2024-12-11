package com.example.babycareapp.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.babycareapp.data.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    private MutableLiveData<Boolean> resetPasswordLiveData;
    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        userLiveData = new MutableLiveData<>();
        resetPasswordLiveData = new MutableLiveData<>();
    }
    public void logout(){
        authRepository.logout();
    }
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
    public LiveData<FirebaseUser> login(String email, String password) {
        return authRepository.login(email, password);
    }
    public LiveData<FirebaseUser> register(String email, String password) {
        return authRepository.register(email, password);
    }
    public LiveData<Boolean> resetPassword(String email) {
        return authRepository.resetPassword(email);
    }
    public LiveData<Boolean> getResetPasswordLiveData(){
        return resetPasswordLiveData;
    }
}
