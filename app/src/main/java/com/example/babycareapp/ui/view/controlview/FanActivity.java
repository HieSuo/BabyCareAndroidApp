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
import com.example.babycareapp.data.model.StatusDevice;
import com.example.babycareapp.databinding.ActivityFanBinding;
import com.example.babycareapp.ui.viewmodel.MainDeviceViewModel;

public class FanActivity extends AppCompatActivity {

    private ActivityFanBinding binding;
    private MainDeviceViewModel mainDeviceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainDeviceViewModel = new ViewModelProvider(this).get(MainDeviceViewModel.class);

        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("deviceId");
        binding.tvDeviceId.setText("Thiết bị hiện tại: "+deviceId);

        mainDeviceViewModel.fetchDeviceStatus(deviceId);

        mainDeviceViewModel.getStatusLiveData().observe(this, new Observer<StatusDevice>() {
            @Override
            public void onChanged(StatusDevice status) {
                if(status==null) return;
                if(status.getSwingStatus()!=0){
                    binding.tvFanStatus.setText("Quạt đang bật chế đố "+status.getSwingStatus());
                    binding.btnFan.setText("Tắt quạt");
                }else{
                    binding.tvFanStatus.setText("Nôi đang tắt");
                    binding.btnFan.setText("Bật quạt");
                }
            }
        });

        // Dữ liệu cho drop-down
        String[] options = {"Nhẹ nhàng", "Mạnh mẽ"};
        // Tạo adapter cho AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_select_items, options);

        binding.autoCompleteFanOption.setAdapter(adapter);
        binding.autoCompleteFanOption.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(FanActivity.this, "Chế độ: "+ item, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnFan.setOnClickListener(v->{
            mainDeviceViewModel.toggleFan(this.getApplicationContext(), binding.btnFan, binding.tvFanStatus);
        });

    }
}