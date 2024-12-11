package com.example.babycareapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.ActivityAddDeviceBinding;
import com.example.babycareapp.ui.viewmodel.DeviceViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AddDeviceActivity extends AppCompatActivity {

    private ActivityAddDeviceBinding binding;
    private DeviceViewModel deviceViewModel;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        deviceViewModel.getDeviceExistsLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean exists) {
                if(exists){
                    String deviceId = Objects.requireNonNull(binding.editTextDeviceCode.getText()).toString();
                    deviceViewModel.linkDeviceToUser(userId, deviceId);
                    Toast.makeText(AddDeviceActivity.this, "Thiet bi da duoc lien ket", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddDeviceActivity.this, "Ma thiet bi khong hop le!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnAddDevice.setOnClickListener(v->{
            String deviceId = Objects.requireNonNull(binding.editTextDeviceCode.getText()).toString().trim();
            if(!deviceId.isEmpty()){
                deviceViewModel.checkDeviceExists(deviceId);
            }else{
                Toast.makeText(this, "Vui long nhap ma thiet bi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}