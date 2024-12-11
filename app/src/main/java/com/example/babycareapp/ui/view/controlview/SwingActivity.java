package com.example.babycareapp.ui.view.controlview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.data.model.Enviroment;
import com.example.babycareapp.data.model.StatusDevice;
import com.example.babycareapp.databinding.ActivityLightControlBinding;
import com.example.babycareapp.databinding.ActivitySwingBinding;
import com.example.babycareapp.ui.viewmodel.MainDeviceViewModel;

public class SwingActivity extends AppCompatActivity {
    private ActivitySwingBinding binding;
    private MainDeviceViewModel mainDeviceViewModel;
    private int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("deviceId");
        binding.tvDeviceId.setText("Thiết bị hiện tại: "+deviceId);

        String[] options = {"Rung nhẹ nhàng", "Rung nhanh"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_select_items, options);

        binding.autoCompleteFanOption.setAdapter(adapter);
        binding.autoCompleteFanOption.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(SwingActivity.this, "Chế độ: "+ item, Toast.LENGTH_SHORT).show();
                if(item.equals("Rung nhẹ nhàng")){
                    x=1;
                }else{
                    x=2;
                }
                mainDeviceViewModel.setSwingLevel(getApplicationContext(), deviceId, x);

            }
        });
        mainDeviceViewModel = new ViewModelProvider(this).get(MainDeviceViewModel.class);

        mainDeviceViewModel.fetchDeviceStatus(deviceId);
        mainDeviceViewModel.getStatusLiveData().observe(this, new Observer<StatusDevice>() {
            @Override
            public void onChanged(StatusDevice status) {
                if(status==null) return;
                if(status.getSwingStatus()!=0){
                    binding.tvSwingStatus.setText("Nôi đang bật chế đố "+status.getSwingStatus());
                    binding.buttonSwing.setText("Tắt rung nôi");
                }else{
                    binding.tvSwingStatus.setText("Nôi đang tắt");
                    binding.buttonSwing.setText("Bật rung nôi");
                }
            }
        });
        binding.buttonSwing.setOnClickListener(v -> {
            mainDeviceViewModel.toggleSwing(this.getApplicationContext(), binding.buttonSwing, binding.tvSwingStatus, x);
        });
    }
}