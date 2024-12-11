package com.example.babycareapp.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.babycareapp.data.model.Device;
import com.example.babycareapp.data.repository.DeviceRepository;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {
    private DeviceRepository deviceRepository;
    private MutableLiveData<Boolean> deviceExistsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Device>> connectedDevices = new MutableLiveData<>();
    private MutableLiveData<Device> mainDevice = new MutableLiveData<>();
    public DeviceViewModel(@NonNull Application application) {
        super(application);
        deviceRepository = new DeviceRepository();
    }
    public void checkDeviceExists(String deviceId) {
        deviceRepository.checkDeviceExists(deviceId, new DeviceRepository.DeviceExistsCallback() {
            @Override
            public void onDeviceExists(boolean exists) {
                // Cập nhật giá trị live data sau khi kiểm tra
                deviceExistsLiveData.setValue(exists);
            }

            @Override
            public void onError(DatabaseError error) {
                // Xử lý lỗi nếu có, có thể log hoặc hiển thị thông báo
                Log.e("DeviceViewModel", "Error checking device existence", error.toException());
            }
        });
    }
    public LiveData<Boolean> getDeviceExistsLiveData() {
        return deviceExistsLiveData;
    }
    // Hàm liên kết thiết bị với người dùng
    public void linkDeviceToUser(String userId, String deviceId) {
        deviceRepository.linkDeviceToUser(userId, deviceId);
    }
    public void unlinkDevice(String deviceId, Context context){
        Log.d("unlinkDevice", "unlinkDevice: "+deviceId);
        deviceRepository.unlinkDevice(deviceId, new DeviceRepository.DeviceCallback() {
            @Override
            public void onSuccess() {
                Log.d("Unlink Device", "onSuccess: "+deviceId);
                Toast.makeText(context, "Đã hủy liên kết với thiết bị "+deviceId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Unlink Device", "onSuccess: ");
            }

            @Override
            public void onCallback(List<Device> connectedDevices) {
                Log.e("Unlink Device", "onCallback: ");
            }

            @Override
            public void onError(Exception e) {
                Log.e("Unlink Device", "onError: ");
            }
        });
    }
    public LiveData<List<Device>> getConnectedDevices() {
        return connectedDevices;
    }
    // Phương thức để lấy thiết bị của người dùng từ Firebase
    public LiveData<Device> getMainDevice(){return mainDevice;}
    public void fetchMainDevice(String userId){
        deviceRepository.getMainDevice(userId, new DeviceRepository.MainDeviceCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onCallback(Device device) {
                mainDevice.setValue(device);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    public void fetchConnectedDevices(String userId) {
        deviceRepository.getUserConnectedDevices(userId, new DeviceRepository.DeviceCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String errorMessage) {

            }

            @Override
            public void onCallback(List<Device> devices) {

                connectedDevices.setValue(devices); // Cập nhật danh sách thiết bị vào LiveData
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi nếu cần
            }
        });
    }
    // Phương thức cập nhật thiết bị chính
    public void setMainDevice(String deviceId, Context context) {
        deviceRepository.updateMainDevice(deviceId, new DeviceRepository.DeviceCallback() {
            @Override
            public void onSuccess() {
                // Cập nhật thành công, có thể hiển thị thông báo hoặc cập nhật UI
                Toast.makeText(context, "Cập nhật thiết bị chính thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Xử lý lỗi nếu cần
                Toast.makeText(context, "Lỗi khi cập nhật thiết bị chính.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCallback(List<Device> connectedDevices) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
