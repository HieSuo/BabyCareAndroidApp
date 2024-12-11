package com.example.babycareapp.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.babycareapp.data.model.Device;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeviceRepository {
    private DatabaseReference databaseReference;
    private DatabaseReference usersReference;
    private DatabaseReference userDevicesRef;
    private final String FIREBASE_DB_URL = "https://babycarefirebase-default-rtdb.asia-southeast1.firebasedatabase.app";
    public DeviceRepository() {
        // Tham chiếu tới node "devices" trong Realtime Database
        databaseReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices");
        usersReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("users");
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        userDevicesRef = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("users").child(userId).child("connectedDevices");

    }

    // Interface callback để trả về kết quả kiểm tra DeviceID
    public interface DeviceExistsCallback {
        void onDeviceExists(boolean exists);
        void onError(DatabaseError error);
    }

    // Hàm kiểm tra thiết bị có tồn tại không bằng DeviceID
    public void checkDeviceExists(String deviceId, final DeviceExistsCallback callback) {
        databaseReference.child(deviceId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Gọi callback trả về true nếu tồn tại, ngược lại false
                callback.onDeviceExists(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gọi callback báo lỗi khi có sự cố
                callback.onError(error);
            }
        });
    }

    // Hàm lưu thiết bị đã kết nối vào node "users/{userId}/connectedDevices"
    public void linkDeviceToUser(String userId, String deviceId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("users").child(userId).child("connectedDevices");
        userRef.child(deviceId).setValue(0);
    }
    public void unlinkDevice(String deviceId, final DeviceCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("DVREPO: unlinkDevice", "unlinkDevice: "+userId);
        DatabaseReference userDevicesRef = FirebaseDatabase.getInstance(FIREBASE_DB_URL)
                .getReference("users")
                .child(userId)
                .child("connectedDevices")
                .child(deviceId);
        Log.d("DVREPO: userDevicesRef", "unlinkDevice: da layuusserdeviceref");
        userDevicesRef.removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DVREPO: unlinkDevice", "Device removed successfully.");
                        callback.onSuccess();
                    } else {
                        Log.e("DVREPO: unlinkDevice", "Error removing device: " + task.getException());
                        callback.onError(task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DVREPO: unlinkDevice", "Failure listener: " + e.getMessage());
                    callback.onError(e);
                });
        Log.d("DVREPO: userDevicesRef", "unlinkDevice: end repo");
    }
    public void getUserConnectedDevices(String userId, final DeviceCallback callback) {
        usersReference.child(userId).child("connectedDevices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Device> connectedDevices = new ArrayList<>(); // Danh sách đối tượng Device
                for (DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
                    String deviceId = deviceSnapshot.getKey(); // Lấy device ID
                    Long isMain = deviceSnapshot.getValue(Long.class); // Lấy giá trị (1 hoặc 0)

                    // Tạo đối tượng Device và thiết lập giá trị main
                    boolean isMainDevice = (isMain != null && isMain == 1);
                    Device device = new Device(deviceId, isMainDevice);

                    connectedDevices.add(device); // Thêm thiết bị vào danh sách
                }

                callback.onCallback(connectedDevices); // Gọi callback với danh sách thiết bị
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                callback.onError(databaseError.toException());
            }
        });
    }
    public void updateMainDevice(String selectedDeviceId, DeviceCallback callback) {
        userDevicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
                        String deviceId = deviceSnapshot.getKey();
                        // Nếu là thiết bị được chọn, đặt giá trị 1 (thiết bị chính)
                        if (deviceId.equals(selectedDeviceId)) {
                            userDevicesRef.child(deviceId).setValue(1);
                        } else {
                            // Các thiết bị khác đặt giá trị 0
                            userDevicesRef.child(deviceId).setValue(0);
                        }
                    }
                    callback.onSuccess();
                } else {
                    callback.onFailure("No devices found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    public void getMainDevice(String userId, final DeviceRepository.MainDeviceCallback callback) {
        usersReference.child(userId).child("connectedDevices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Device mainDevice = new Device(); // Danh sách đối tượng Device
                for (DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
                    String deviceId = deviceSnapshot.getKey(); // Lấy device ID
                    Long isMain = deviceSnapshot.getValue(Long.class); // Lấy giá trị (1 hoặc 0)

                    // Tạo đối tượng Device và thiết lập giá trị main
                    boolean isMainDevice = (isMain != null && isMain == 1);
                    if(isMainDevice){
                        mainDevice = new Device(deviceId, isMainDevice); // lưu thiết bị main
                    }
                }

                callback.onCallback(mainDevice); // Gọi callback với thiết bị chisnh
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                callback.onError(databaseError.toException());
                Log.e("Lôi ở DV REPO", databaseError.toException().toString());
            }
        });
    }
    
    public interface MainDeviceCallback{
        void onSuccess();
        void onCallback(Device device);
        void onError(Exception e);
    }
    public interface DeviceCallback {
        void onSuccess();  // Hàm sẽ được gọi khi thao tác thành công
        void onFailure(String errorMessage);
        void onCallback(List<Device> connectedDevices);
        void onError(Exception e);
    }
}
