package com.example.babycareapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.ActivityCameraBinding;
import com.example.babycareapp.ui.viewmodel.MainDeviceViewModel;

public class CameraActivity extends AppCompatActivity {

    private ActivityCameraBinding binding;
    private MainDeviceViewModel mainDeviceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        mainDeviceViewModel = new ViewModelProvider(this).get(MainDeviceViewModel.class);

        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("deviceId");

        setContentView(binding.getRoot());
        binding.webview.setWebViewClient(new WebViewClient());
        WebSettings webSettings = binding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        mainDeviceViewModel.fetchIpCamera(deviceId, binding.webview);
    }
    @Override
    public void onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}