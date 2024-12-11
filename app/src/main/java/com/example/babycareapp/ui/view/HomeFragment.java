package com.example.babycareapp.ui.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.data.model.Device;
import com.example.babycareapp.data.repository.MainDeviceRepository;
import com.example.babycareapp.databinding.FragmentHomeBinding;
import com.example.babycareapp.ui.view.controlview.EnviromentActivity;
import com.example.babycareapp.ui.view.controlview.FanActivity;
import com.example.babycareapp.ui.view.controlview.MusicActivity;
import com.example.babycareapp.ui.view.controlview.SwingActivity;
import com.example.babycareapp.ui.viewmodel.DeviceViewModel;
import com.example.babycareapp.ui.viewmodel.MainDeviceViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DeviceViewModel deviceViewModel;
    private Device mainDevice;


    public HomeFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("HomeFragment","UserID:" +userId);
        deviceViewModel.fetchMainDevice(userId);
        Log.e("HomeFragment","Done fetch Main DV" +userId);

        deviceViewModel.getMainDevice().observe(getViewLifecycleOwner(),devices->{
            Log.d("HomeFragment", "From get main device"+ devices.getDeviceId());
            mainDevice = devices;
            binding.tvDeviceCode.setText("Thiết bị hiện tại: "+mainDevice.getDeviceId());
        });
        //btn event
        binding.btncamera.setOnClickListener(v->{
            if(mainDevice!=null){
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra("deviceId",mainDevice.getDeviceId());
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Lỗi maindevice is null", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnlight.setOnClickListener(v->{
            if(mainDevice!=null){
                Intent intent = new Intent(getActivity(), LightControlActivity.class);
                intent.putExtra("deviceId",mainDevice.getDeviceId());
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Lỗi maindevice is null", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnTemperature.setOnClickListener(v->{
            if(mainDevice!=null){
                Intent intent = new Intent(getActivity(), EnviromentActivity.class);
                intent.putExtra("deviceId",mainDevice.getDeviceId());
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Lỗi maindevice is null", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnswing.setOnClickListener(v->{
            if(mainDevice!=null){
                Intent intent = new Intent(getActivity(), SwingActivity.class);
                intent.putExtra("deviceId",mainDevice.getDeviceId());
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Lỗi maindevice is null", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnwindy.setOnClickListener(v->{
            if(mainDevice!=null){
                Intent intent = new Intent(getActivity(), FanActivity.class);
                intent.putExtra("deviceId",mainDevice.getDeviceId());
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Lỗi maindevice is null", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnmusic.setOnClickListener(v->{
            if(mainDevice!=null){
                Intent intent = new Intent(getActivity(), MusicActivity.class);
                intent.putExtra("deviceId",mainDevice.getDeviceId());
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Lỗi maindevice is null", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Giải phóng binding để tránh leak memory
        binding = null;
    }
}