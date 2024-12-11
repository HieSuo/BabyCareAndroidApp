package com.example.babycareapp.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.babycareapp.R;
import com.example.babycareapp.data.model.Enviroment;
import com.example.babycareapp.data.model.StatusDevice;
import com.example.babycareapp.data.repository.MainDeviceRepository;

public class MainDeviceViewModel extends AndroidViewModel {
    MainDeviceRepository mainDeviceRepository = new MainDeviceRepository();
    private MutableLiveData<Enviroment> enviromentLiveData = new MutableLiveData<>();
    private MutableLiveData<StatusDevice> statusLiveData = new MutableLiveData<>();

    public MainDeviceViewModel(@NonNull Application application) {
        super(application);
        mainDeviceRepository = new MainDeviceRepository();
    }
    // Hàm để lấy LiveData cho Enviroment
    public LiveData<Enviroment> getEnviromentLiveData() {
        return enviromentLiveData;
    }

    public MutableLiveData<StatusDevice> getStatusLiveData() {
        return statusLiveData;
    }

    public void toggleLed(Context context, Button button, ImageView imageView) {
        mainDeviceRepository.toggleLed(new MainDeviceRepository.MainDeviceCallback() {
            @Override
            public void onSuccess(String msg) {
                Log.d("Success toogleLed:", "Đèn đang"+msg);
                Toast.makeText(context, "Đèn đang "+msg, Toast.LENGTH_SHORT).show();
                String msgBtn = msg;
                if(msgBtn.equals("Tắt")){
                    msgBtn ="Bật";
                    imageView.setImageResource(R.drawable.ic_sun);
                }else{
                    msgBtn = "Tắt";
                    imageView.setImageResource(R.drawable.ic_sun_light);
                }
                button.setText(msgBtn+" đèn");
            }

            @Override
            public void onError(Exception e) {
                Log.e("MainDeviceViewModel", "Error toggling led state", e);
            }
        });
    }
    public void setSwingLevel(Context context,String deviceId, int x){
        mainDeviceRepository.setSwingLevel(deviceId, x, new MainDeviceRepository.MainDeviceCallback(){

            @Override
            public void onSuccess(String deviceId) {
                Log.d("succ", "onSuccess: ");
            }

            @Override
            public void onError(Exception e) {
                Log.e("Error setswing", "onError: ");
            }
        });
    }
    public void toggleSwing(Context context, Button button, TextView textView, int x) {
        mainDeviceRepository.toggleSwing(x,new MainDeviceRepository.MainDeviceCallback() {
            @Override
            public void onSuccess(String msg) {
                Log.d("Success toggleSwing:", "Nôi đang "+msg);
                Toast.makeText(context, "Nôi đang "+msg, Toast.LENGTH_SHORT).show();
                String msgBtn = msg;
                if(msgBtn.equals("Tắt")){
                    msgBtn ="Bật";
                    textView.setText("Nôi đang tắt");
                }else{
                    textView.setText("Nôi đang bật");
                    msgBtn = "Tắt";
                }
                button.setText(msgBtn+" nôi");
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "Lỗi 400, kiểm tra kết nối với thiết bị!", Toast.LENGTH_SHORT).show();
                Log.e("MainDeviceViewModel", "Error toggling led state", e);
            }
        });
    }
    public void toggleFan(Context context, Button button, TextView textView) {
        mainDeviceRepository.toggleFan(new MainDeviceRepository.MainDeviceCallback() {
            @Override
            public void onSuccess(String msg) {
                Log.d("Success toggleSwing:", "Quạt đang "+msg);
                Toast.makeText(context, "Quạt đang "+msg, Toast.LENGTH_SHORT).show();
                String msgBtn = msg;
                if(msgBtn.equals("Tắt")){
                    msgBtn ="Bật";
                    textView.setText("Quạt đang tắt");
                }else{
                    msgBtn = "Tắt";
                    textView.setText("Quạt đang bật");
                }
                button.setText(msgBtn+" quạt");
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "Lỗi 400, kiểm tra kết nối với thiết bị!", Toast.LENGTH_SHORT).show();
                Log.e("MainDeviceViewModel", "Error toggling fan state", e);
            }
        });
    }
    public void setTemperatureLimit(double minTemp, double maxTemp, String deviceId){
        mainDeviceRepository.setTemperatureLimit(deviceId, minTemp, maxTemp, new MainDeviceRepository.GetEnviromentCallback() {
            @Override
            public void onSuccess(Enviroment enviroment) {
                fetchEnviroment(deviceId);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    public void setHumidityLimit(double minHum, double maxHum, String deviceId){
        mainDeviceRepository.setHumidityLimit(deviceId, minHum, maxHum, new MainDeviceRepository.GetEnviromentCallback() {
            @Override
            public void onSuccess(Enviroment enviroment) {
                fetchEnviroment(deviceId);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    public void fetchDeviceStatus(String deviceId){
        mainDeviceRepository.fetchDeviceStatus(deviceId, new MainDeviceRepository.GetStatusCallback() {
            @Override
            public void onSuccess(StatusDevice status) {
                statusLiveData.setValue(status);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    public void fetchIpCamera(String deviceId, WebView webView){
        mainDeviceRepository.fetchIpCamera(deviceId, new MainDeviceRepository.GetIpCameraCallback() {
            @Override
            public void onSuccess(String ipCamera) {
                webView.loadUrl(ipCamera);
                Log.d("FetchIpCam.ViewModel", "onSuccess: "+ipCamera);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    public void fetchEnviroment(String deviceId) {
        mainDeviceRepository.fetchEnviroment(deviceId,new MainDeviceRepository.GetEnviromentCallback() {
            @Override
            public void onSuccess(Enviroment enviroment) {
                // Cập nhật LiveData khi có dữ liệu mới
                enviromentLiveData.setValue(enviroment);
                // Xử lý khi lấy được thông tin thành công
                Log.d("MainDeviceViewModel", "Current Temp: " + enviroment.getCurrentTemp());
                Log.d("MainDeviceViewModel", "Current Humidity: " + enviroment.getCurrentHumidity());
            }
            @Override
            public void onError(Exception e) {
                // Xử lý lỗi
                Log.e("MainDeviceViewModel", "Error fetching environment data", e);
            }
        });
    }
    public void listenForSoundDetection(String deviceId,MainDeviceRepository.SoundDetectionCallback callback) {
        mainDeviceRepository.listenForSoundDetection(deviceId,callback);
        Log.d("MainDevViewModel: ", "listenForSoundDetection: ");
    }
    public void listenForOverTemperatureLimit(String deviceId, MainDeviceRepository.OverTemperatureLimitCallback callback){
        mainDeviceRepository.listenForOverTemperatureLimit(deviceId, callback);
        Log.d("MainDeviceViewModel", "listenForOverTemperatureLimit: ");
    }
    public void listenForOverHumidityLimit(String deviceId, MainDeviceRepository.OverTemperatureLimitCallback callback){
        mainDeviceRepository.listenForOverHumidityLimit(deviceId, callback);
        Log.d("MainDeviceViewModel", "listenForOverHumidtyLimit: ");
    }
}
