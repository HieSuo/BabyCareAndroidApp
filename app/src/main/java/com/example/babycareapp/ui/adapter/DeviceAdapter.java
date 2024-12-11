package com.example.babycareapp.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babycareapp.R;
import com.example.babycareapp.data.model.Device;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> deviceList;
    private OnDeviceClickListener onDeviceClickListener;
    private OnMainDeviceClickListener onMainDeviceClickListener;
    private OnUnlinkDeviceClickListener onUnlinkDeviceClickListener;
    public DeviceAdapter(List<Device> deviceList, OnDeviceClickListener onDeviceClickListener, OnMainDeviceClickListener onMainDeviceClickListener, OnUnlinkDeviceClickListener onUnlinkDeviceClickListener) {
        this.deviceList = deviceList;
        this.onDeviceClickListener = onDeviceClickListener;
        this.onMainDeviceClickListener = onMainDeviceClickListener;
        this.onUnlinkDeviceClickListener = onUnlinkDeviceClickListener;
    }
    @NonNull
    @Override
    public DeviceAdapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);
        if(device.isMain()){
            holder.deviceIdTextView.setText(device.getDeviceId()+"-Thiết bị hiện tại");
            holder.setMainDeviceButton.setVisibility(View.INVISIBLE);
        }else{
            holder.deviceIdTextView.setText(device.getDeviceId());
            holder.setMainDeviceButton.setVisibility(View.VISIBLE);
        }
        // Có thể thêm thông tin hoặc chức năng khác nếu cần
        holder.itemView.setOnClickListener(v -> onDeviceClickListener.onDeviceClick(device.getDeviceId()));
        // Xử lý sự kiện chọn thiết bị chính
        holder.setMainDeviceButton.setOnClickListener(v -> onMainDeviceClickListener.onMainDeviceClick(device.getDeviceId()));
        //Xử lý ựu kiện xóa tbi
        holder.unlinkDevcieButton.setOnClickListener(v -> onUnlinkDeviceClickListener.onUnlinkDeviceClick(device.getDeviceId()));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateDevices(List<Device> newDevices){
        deviceList = newDevices;
        notifyDataSetChanged(); // Cập nhật dữ liệu trong RecyclerView
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView deviceIdTextView;
        Button setMainDeviceButton;
        Button unlinkDevcieButton;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceIdTextView = itemView.findViewById(R.id.text_device_id);
            setMainDeviceButton = itemView.findViewById(R.id.btn_set_main_device);
            unlinkDevcieButton = itemView.findViewById(R.id.btn_unlink_device);
        }
    }

    public interface OnDeviceClickListener {
        void onDeviceClick(String deviceId);
    }
    public interface OnUnlinkDeviceClickListener{
        void onUnlinkDeviceClick(String deviceId);
    }
    public interface OnMainDeviceClickListener {
        void onMainDeviceClick(String deviceId);
    }
}
