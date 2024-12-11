package com.example.babycareapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.data.model.StatusDevice;
import com.example.babycareapp.databinding.ActivityLightControlBinding;
import com.example.babycareapp.ui.viewmodel.DeviceViewModel;
import com.example.babycareapp.ui.viewmodel.MainDeviceViewModel;

public class LightControlActivity extends AppCompatActivity {

    private ActivityLightControlBinding binding;
    private MainDeviceViewModel mainDeviceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainDeviceViewModel = new ViewModelProvider(this).get(MainDeviceViewModel.class);
        binding = ActivityLightControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("deviceId");
        binding.tvDeviceId.setText("Thiết bị hiện tại: "+deviceId);

        mainDeviceViewModel.fetchDeviceStatus(deviceId);
        mainDeviceViewModel.getStatusLiveData().observe(this, new Observer<StatusDevice>() {
            @Override
            public void onChanged(StatusDevice status) {
                if(status==null) return;
                if(status.getLedStatus()!=0){
                    binding.imageViewLight.setImageResource(R.drawable.ic_sun_light);
                    binding.buttonLed.setText("Tắt đèn");
                }else{
                    binding.imageViewLight.setImageResource(R.drawable.ic_sun);
                    binding.buttonLed.setText("Bật đèn");
                }
            }
        });

        binding.buttonLed.setOnClickListener(v -> {
            mainDeviceViewModel.toggleLed(this.getApplicationContext(), binding.buttonLed, binding.imageViewLight);
        });
    }
}