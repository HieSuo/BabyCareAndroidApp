package com.example.babycareapp.data.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.babycareapp.data.model.Device;
import com.example.babycareapp.data.model.Enviroment;
import com.example.babycareapp.data.model.StatusDevice;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainDeviceRepository {
    private DatabaseReference mainDeviceReference;
    private DatabaseReference usersReference;
    private final String FIREBASE_DB_URL = "https://babycarefirebase-default-rtdb.asia-southeast1.firebasedatabase.app";
    public MainDeviceRepository() {
        //get userid
        String userId = FirebaseAuth.getInstance().getUid();
        // Tham chiếu đến nhánh "users" để lấy Main Device
        usersReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("users").child(userId).child("connectedDevices");

        // Lấy ra Main Device ID (Thiết bị có giá trị 1)
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mainDeviceId = null;
                for (DataSnapshot deviceSnapshot : snapshot.getChildren()) {
                    Long isMainDevice = deviceSnapshot.getValue(Long.class);
                    if (isMainDevice != null && isMainDevice == 1) {

                        mainDeviceId = deviceSnapshot.getKey();  // Lấy deviceId của thiết bị chính
                        Log.e("MainDevRepo", "main id:"+mainDeviceId);
                        break;
                    }
                }

                // Nếu tìm thấy Main Device, tham chiếu đến nhánh devices/mainDeviceId
                if (mainDeviceId != null) {
                    Log.e("MainDevRepo", "main is not null!");
                    mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(mainDeviceId);
                } else {
                    Log.e("MainDevRepo", "main is null!");
                    // Xử lý khi không có thiết bị chính
                    mainDeviceReference = null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
                Log.e("MainDevRepo", "Error fetching main device", error.toException());
            }
        });
    }
    // Phương thức để bật/tắt đèn (led)
    public void toggleLed(final MainDeviceCallback callback) {
        if(mainDeviceReference!=null){
            Log.d("MainDevRepo", "toggleLed: maindev is not null");
            mainDeviceReference.child("led").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Log.d("MainDevRepo:toogleLed", "onDataChange: snapshot is exist");
                        Long currentState = snapshot.getValue(Long.class);
                        if(currentState!=null){
                            Long newLedState = currentState ==1 ? 0L :1L;
                            mainDeviceReference.child("led").setValue(newLedState).addOnCompleteListener(task->{
                               if(task.isSuccessful()){
                                   callback.onSuccess(newLedState==1?"Bật":"Tắt");
                               }else{
                                   callback.onError(task.getException());
                               }
                            });
                        }
                    }else{
                        Log.e("MainDevRepo:toogleLed", "onDataChange: snapshot is not exist!!!!!!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("MainDevRepo:toogleLed", "onCancelled: fail");
                }
            });
        }else{
            Log.e("MainDeviceRepository", "toggleLed: maindev is null!!");
        }
    }
    public void setTemperatureLimit(String deviceId,double minTemp, double maxTemp, final GetEnviromentCallback callback){
        DatabaseReference temperatureRef;
        if(mainDeviceReference==null){
            mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        }
        temperatureRef = mainDeviceReference.child("temperature");
        // Tạo map để cập nhật đồng thời cả minTemp và maxTemp
        Map<String, Object> tempLimits = new HashMap<>();
        tempLimits.put("min", minTemp);
        tempLimits.put("max", maxTemp);

        // Cập nhật dữ liệu vào Firebase
        temperatureRef.updateChildren(tempLimits)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Gọi callback nếu cập nhật thành công
                        if (callback != null) {
                            callback.onSuccess(null); // Truyền giá trị null nếu không cần trả về dữ liệu gì
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gọi callback nếu có lỗi xảy ra
                        if (callback != null) {
                            callback.onError(e);
                        }
                    }
                });

    }
    public void setHumidityLimit(String deviceId,double minHum, double maxHum, final GetEnviromentCallback callback){
        DatabaseReference humidityRef;
        if(mainDeviceReference==null){
            mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        }
        humidityRef = mainDeviceReference.child("humidity");
        // Tạo map để cập nhật đồng thời cả minTemp và maxTemp
        Map<String, Object> humLimits = new HashMap<>();
        humLimits.put("min", minHum);
        humLimits.put("max", maxHum);

        // Cập nhật dữ liệu vào Firebase
        humidityRef.updateChildren(humLimits)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Gọi callback nếu cập nhật thành công
                        if (callback != null) {
                            callback.onSuccess(null); // Truyền giá trị null nếu không cần trả về dữ liệu gì
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gọi callback nếu có lỗi xảy ra
                        if (callback != null) {
                            callback.onError(e);
                        }
                    }
                });

    }
    public void setSwingLevel(String deviceId, int level,final MainDeviceCallback callback){
        DatabaseReference humidityRef;
        if(mainDeviceReference==null){
            mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        }
        mainDeviceReference.child("swingLevel").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d("MainDevRepo:toggleSwing", "onDataChange: snapshot is exist");
                    Long currentState = snapshot.getValue(Long.class);
                    if(currentState!=null){
                        mainDeviceReference.child("swingLevel").setValue(level).addOnCompleteListener(task->{
                            if(task.isSuccessful()){
                                callback.onSuccess(level==1 ?"Nhẹ nhàng":"Mạnh mẽ");
                            }else{
                                callback.onError(task.getException());
                            }
                        });
                    }
                }else{
                    Log.e("MainDevRepo:toggleSwing", "onDataChange: snapshot is not exist!!!!!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MainDevRepo:toogleLed", "onCancelled: fail");
            }
        });

    }
    public void fetchEnviroment(String deviceId,final GetEnviromentCallback callback){
        if(mainDeviceReference==null){
            mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        }
        if (mainDeviceReference != null) {
            mainDeviceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Enviroment enviroment = new Enviroment();

                        // Lấy thông tin độ ẩm
                        DataSnapshot humiditySnapshot = snapshot.child("humidity");
                        if (humiditySnapshot.exists()) {
                            enviroment.setCurrentHumidity(humiditySnapshot.child("current").getValue(Double.class));
                            enviroment.setMaxHumidity(humiditySnapshot.child("max").getValue(Double.class));
                            enviroment.setMinHumidity(humiditySnapshot.child("min").getValue(Double.class));
                        }

                        // Lấy thông tin nhiệt độ (giả định có nhánh temperature tương tự humidity)
                        DataSnapshot temperatureSnapshot = snapshot.child("temperature");
                        if (temperatureSnapshot.exists()) {
                            enviroment.setCurrentTemp(temperatureSnapshot.child("current").getValue(Double.class));
                            enviroment.setMaxTemp(temperatureSnapshot.child("max").getValue(Double.class));
                            enviroment.setMinTemp(temperatureSnapshot.child("min").getValue(Double.class));
                        }

                        // Trả về thông tin Enviroment
                        callback.onSuccess(enviroment);
                    } else {
                        callback.onError(new Exception("Device data not found"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onError(error.toException());
                }
            });
        } else {
            callback.onError(new Exception("Main device not set"));
        }
    }
    // Phương thức để bật/tắt RUNG NÔI (led)

    public void toggleSwing(int x,final MainDeviceCallback callback) {
        if(mainDeviceReference!=null){
            Log.d("MainDevRepo", "toggleSwing: maindev is not null");
            mainDeviceReference.child("swing").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Log.d("MainDevRepo:toggleSwing", "onDataChange: snapshot is exist");
                        Long currentState = snapshot.getValue(Long.class);
                        if(currentState!=null){
                            Long newSwingState = currentState ==1 ? 0L :1L;
                            mainDeviceReference.child("swing").setValue(newSwingState).addOnCompleteListener(task->{
                                if(task.isSuccessful()){
                                    callback.onSuccess(newSwingState==1?"Bật":"Tắt");
                                }else{
                                    callback.onError(task.getException());
                                }
                            });
                        }
                    }else{
                        Log.e("MainDevRepo:toggleSwing", "onDataChange: snapshot is not exist!!!!!!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("MainDevRepo:toogleLed", "onCancelled: fail");
                }
            });
        }else{
            Log.e("MainDeviceRepository", "toggleLed: maindev is null!!");
        }
    }
    public void fetchDeviceStatus(String deviceId,final GetStatusCallback callback){
        if(mainDeviceReference==null){
            mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        }
        if (mainDeviceReference != null) {
            mainDeviceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        StatusDevice statusDevice = new StatusDevice();
                        // Lấy thông tin độ ẩm
                        DataSnapshot swingSnapshot = snapshot.child("swing");
                        if (swingSnapshot.exists()) {
                            statusDevice.setSwingStatus(swingSnapshot.getValue(Integer.class));
                        }
                        DataSnapshot fanSnapshot = snapshot.child("fan");
                        if (swingSnapshot.exists()) {
                            statusDevice.setFanStatus(fanSnapshot.getValue(Integer.class));
                        }
                        DataSnapshot ledSnapshot = snapshot.child("led");
                        if (swingSnapshot.exists()) {
                            statusDevice.setLedStatus(ledSnapshot.getValue(Integer.class));
                        }
                        // Trả về thông tin Enviroment
                        callback.onSuccess(statusDevice);
                    } else {
                        callback.onError(new Exception("Device data not found"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onError(error.toException());
                }
            });
        } else {
            callback.onError(new Exception("Main device not set"));
        }
    }
    // Phương thức Bật tắt quạt
    public void toggleFan(final MainDeviceCallback callback) {
        if(mainDeviceReference!=null){
            Log.d("MainDevRepo", "toggleFan: maindev is not null");
            mainDeviceReference.child("fan").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Log.d("MainDevRepo:toggleFan", "onDataChange: snapshot is exist");
                        Long currentState = snapshot.getValue(Long.class);
                        if(currentState!=null){
                            Long newSwingState = currentState ==1 ? 0L :1L;
                            mainDeviceReference.child("fan").setValue(newSwingState).addOnCompleteListener(task->{
                                if(task.isSuccessful()){
                                    callback.onSuccess(newSwingState==1?"Bật":"Tắt");
                                }else{
                                    callback.onError(task.getException());
                                }
                            });
                        }
                    }else{
                        Log.e("MainDevRepo:toggleFan", "onDataChange: snapshot is not exist!!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("MainDevRepo:toogleLed", "onCancelled: fail");
                }
            });
        }else{
            Log.e("MainDeviceRepository", "toggleLed: maindev is null!!");
        }
    }
    public void listenForSoundDetection(String deviceId,final SoundDetectionCallback callback) {
        mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        if(mainDeviceReference!=null){
            DatabaseReference soundDetectedRef = mainDeviceReference.child("soundDetected");
            if(soundDetectedRef!=null){
                soundDetectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int soundDetected = dataSnapshot.getValue(Integer.class);
                            if (soundDetected == 1) {
                                Log.d("MainDevRepo", "onDataChange: soundeteced = 1");
                                callback.onSoundDetected(); // Thông báo khi phát hiện âm thanh
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.toException());
                    }
                });
            }else{
                Log.e("MainDevRepo", "listenForSoundDetection: soundDetectedRef = null" );
            }

        }else{
            Log.e("MainDevRepo", "listenForSoundDetection: mainDeviceReference=null" );
        }
    }
    public void listenForOverTemperatureLimit(String deviceId, final OverTemperatureLimitCallback callback){
        mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        DatabaseReference temperatureRef = mainDeviceReference.child("temperature");
        if(temperatureRef!=null){
            temperatureRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double currentTemp = snapshot.child("current").getValue(Double.class);
                    double minTemp = snapshot.child("min").getValue(Double.class);
                    double maxTemp = snapshot.child("max").getValue(Double.class);

                    if(currentTemp<minTemp || currentTemp>maxTemp){
                        Log.d("Listen Overheat", "onDataChange: "+currentTemp+"-"+minTemp+"-"+maxTemp);
                        callback.onOverTemperatureLimit("Nhiet do vuot nguong an toan");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onError(error.toException());
                }
            });
        }
    }
    public void listenForOverHumidityLimit(String deviceId, final OverTemperatureLimitCallback callback){
        mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        DatabaseReference humidityRef = mainDeviceReference.child("humidity");
        if(humidityRef!=null){
            humidityRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double currentHum = snapshot.child("current").getValue(Double.class);
                    double minHum = snapshot.child("min").getValue(Double.class);
                    double maxHum = snapshot.child("max").getValue(Double.class);

                    if(currentHum<minHum || currentHum>maxHum){
                        Log.d("Listen Overheat", "onDataChange: "+currentHum+"-"+minHum+"-"+maxHum);
                        callback.onOverTemperatureLimit("Do am vuot nguong an toan");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onError(error.toException());
                }
            });
        }
    }
    public void fetchIpCamera(String deviceId, final GetIpCameraCallback callback){
        if(mainDeviceReference==null){
            mainDeviceReference = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("devices").child(deviceId);
        }
        if (mainDeviceReference != null) {
            mainDeviceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String ipCamera = "";
                        // Lấy thông tin độ ẩm
                        DataSnapshot swingSnapshot = snapshot.child("ipcamera");
                        if (swingSnapshot.exists()) {
                            ipCamera =swingSnapshot.getValue(String.class);
                            callback.onSuccess(ipCamera);
                        }
                    } else {
                        callback.onError(new Exception("Device data not found"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onError(error.toException());
                }
            });
        } else {
            callback.onError(new Exception("Main device not set"));
        }
    }
    public interface GetStatusCallback{
        void onSuccess(StatusDevice statusDevice);  // Trả về trạng thái
        void onError(Exception e);
    }
    public interface  GetIpCameraCallback{
        void onSuccess(String ipCamera);  // Trả về ip camera
        void onError(Exception e);
    }
    public interface OverTemperatureLimitCallback {
        void onOverTemperatureLimit(String msg);
        void onError(Exception e);
    }
    public interface OverHumidityLimitCallback {
        void onOverHumidityLimit(String msg);
        void onError(Exception e);
    }
    public interface SoundDetectionCallback {
        void onSoundDetected();
        void onError(Exception e);
    }
    public interface MainDeviceCallback {
        void onSuccess(String deviceId);  // Trả về Main Device ID
        void onError(Exception e);        // Xử lý khi có lỗi
    }


    public interface GetEnviromentCallback{
        void onSuccess(Enviroment enviroment);  // Trả về thong tin nhiet do
        void onError(Exception e);
    }

}
