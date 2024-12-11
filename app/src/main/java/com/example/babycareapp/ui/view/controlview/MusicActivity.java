package com.example.babycareapp.ui.view.controlview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.ActivityFanBinding;
import com.example.babycareapp.databinding.ActivityMusicBinding;
import com.example.babycareapp.ui.viewmodel.MainDeviceViewModel;

public class MusicActivity extends AppCompatActivity {
    private ActivityMusicBinding binding;
    private MainDeviceViewModel mainDeviceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicBinding.inflate(getLayoutInflater());

        // Dữ liệu cho drop-down
        String[] options = {"Nhẹ nhàng", "Mạnh mẽ"};
        // Tạo adapter cho AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_select_items, options);

        binding.autoCompleteFanOption.setAdapter(adapter);
        binding.autoCompleteFanOption.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(MusicActivity.this, "Bài hát hiện tại: "+ item, Toast.LENGTH_SHORT).show();
            }
        });

        setContentView(R.layout.activity_music);
    }
}