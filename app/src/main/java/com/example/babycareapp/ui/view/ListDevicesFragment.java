package com.example.babycareapp.ui.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.FragmentListDevicesBinding;
import com.example.babycareapp.ui.adapter.DeviceAdapter;
import com.example.babycareapp.ui.viewmodel.DeviceViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class ListDevicesFragment extends Fragment {

    private DeviceViewModel deviceViewModel;
    private DeviceAdapter deviceAdapter;

    private FragmentListDevicesBinding binding;
    public ListDevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListDevicesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerViewDevices.setLayoutManager(new LinearLayoutManager(getContext()));
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        deviceAdapter = new DeviceAdapter(new ArrayList<>(), deviceId -> {
            // Điều hướng đến DeviceDetailFragment khi click vào thiết bị
            Bundle bundle = new Bundle();
            bundle.putString("deviceId", deviceId);
            Toast.makeText(getActivity(), "Chi tiet "+bundle.getString("deviceId"), Toast.LENGTH_SHORT).show();
        },deviceId -> {
            // Chọn thiết bị chính
            deviceViewModel.setMainDevice(deviceId, getContext());
            deviceViewModel.fetchConnectedDevices(userId);
            deviceViewModel.getConnectedDevices().observe(getViewLifecycleOwner(),devices->{
                Log.d("ListdeviceFragment", "get connected devices");
                deviceAdapter.updateDevices(devices);
            });
        }, deviceId -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Hủy liên kết");
            builder.setMessage("Bạn có chắc chắn muốn hủy liên kết thiết bị không?");

            //đồng ý
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deviceViewModel.unlinkDevice(deviceId, getContext());
                    deviceViewModel.fetchConnectedDevices(userId);
                    deviceViewModel.getConnectedDevices().observe(getViewLifecycleOwner(),devices->{
                        Log.d("ListdeviceFragment", "get connected devices");
                        deviceAdapter.updateDevices(devices);
                    });
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();


        });
        binding.recyclerViewDevices.setAdapter(deviceAdapter);

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        //Lay danh sach thiet bi da ket noi
        deviceViewModel.fetchConnectedDevices(userId);

        //Lang nghe thay doi tu db
        deviceViewModel.getConnectedDevices().observe(getViewLifecycleOwner(),devices->{
            Log.d("ListdeviceFragment", "get connected devices");
            deviceAdapter.updateDevices(devices);
        });

        binding.btnadd.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
            startActivity(intent);
        });

        // Inflate the layout for this fragment
        return view;
    }
    public void RefreshFragment(){
        Fragment currentFragment = getParentFragmentManager().findFragmentById(R.id.nav_fragment_container); // ID container chứa Fragment
        if (currentFragment != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.detach(currentFragment); // Tách Fragment ra
            transaction.attach(currentFragment); // Đính lại Fragment
            transaction.commit(); // Áp dụng thay đổi
        }
    }
}