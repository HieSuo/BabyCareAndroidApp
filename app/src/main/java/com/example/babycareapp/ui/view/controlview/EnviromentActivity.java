package com.example.babycareapp.ui.view.controlview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.data.model.Enviroment;
import com.example.babycareapp.databinding.ActivityEnviromentBinding;
import com.example.babycareapp.databinding.ActivityLightControlBinding;
import com.example.babycareapp.ui.viewmodel.MainDeviceViewModel;

public class EnviromentActivity extends AppCompatActivity {
    ActivityEnviromentBinding binding;
    MainDeviceViewModel mainDeviceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainDeviceViewModel = new ViewModelProvider(this).get(MainDeviceViewModel.class);
        binding = ActivityEnviromentBinding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("deviceId");
        binding.tvDeviceId.setText("Thiết bị hiện tại: "+deviceId);
        setContentView(binding.getRoot());
//        binding.buttonGet.setOnClickListener(v -> {
//            mainDeviceViewModel.fetchEnviroment();
//        });
        binding.btnSetTemp.setOnClickListener(v -> {
            double minTemp= Double.parseDouble(binding.edtMinTemp.getText().toString());
            double maxTemp = Double.parseDouble(binding.edtMaxTemp.getText().toString());
            if(minTemp>=maxTemp){
                Toast.makeText(this, "Giới hạn nhiệt độ không hợp lệ.", Toast.LENGTH_SHORT).show();
            }else{
                mainDeviceViewModel.setTemperatureLimit(minTemp, maxTemp, deviceId);
                Toast.makeText(this, "Đã cập nhật giới hạn nhiệt độ mới.", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnSetHum.setOnClickListener(v->{
            double minHum = Double.parseDouble(binding.edtMinHum.getText().toString());
            double maxHum = Double.parseDouble(binding.edtMaxHum.getText().toString());
            
            if(minHum>maxHum){
                Toast.makeText(this, "Giới hạn độ ẩm không hợp lệ", Toast.LENGTH_SHORT).show();;
            }else{
                mainDeviceViewModel.setHumidityLimit(minHum, maxHum, deviceId);
                Toast.makeText(this, "Đã cập nhật giới hạn độ ẩm mới.", Toast.LENGTH_SHORT).show();
            }

        });
        mainDeviceViewModel.fetchEnviroment(deviceId);
        mainDeviceViewModel.getEnviromentLiveData().observe(this, new Observer<Enviroment>() {
            @Override
            public void onChanged(Enviroment enviroment) {
                if(enviroment!=null){
                    if(enviroment.getCurrentTemp()>enviroment.getMaxTemp()){
                        binding.tvTemperature.setTextColor(getResources().getColor(R.color.danger_high));
                    }else if(enviroment.getCurrentTemp()<enviroment.getMinTemp()){
                        binding.tvTemperature.setTextColor(getResources().getColor(R.color.danger_low));
                    }else{
                        binding.tvTemperature.setTextColor(getResources().getColor(R.color.safe));
                    }
                    if(enviroment.getCurrentHumidity()>enviroment.getMaxHumidity()){
                        binding.tvHumidity.setTextColor(getResources().getColor(R.color.danger_high));
                    }else if(enviroment.getCurrentHumidity()<enviroment.getMinHumidity()){
                        binding.tvHumidity.setTextColor(getResources().getColor(R.color.danger_low));
                    }else{
                        binding.tvHumidity.setTextColor(getResources().getColor(R.color.safe));
                    }
                    binding.tvTemperature.setText("Nhiệt độ hiện tại: "+ enviroment.getCurrentTemp());
                    binding.tvHumidity.setText("Độ ẩm hiện tại: "+enviroment.getCurrentHumidity());

                    binding.edtMinTemp.setText(enviroment.getMinTemp()+"");
                    binding.edtMaxTemp.setText(enviroment.getMaxTemp()+"");
                    binding.edtMinHum.setText(enviroment.getMinHumidity()+"");
                    binding.edtMaxHum.setText(enviroment.getMaxHumidity()+"");
                    Toast.makeText(EnviromentActivity.this, "Đã cập nhật môi trường hiện tại", Toast.LENGTH_SHORT).show();


                }
            }
        });




    }
}