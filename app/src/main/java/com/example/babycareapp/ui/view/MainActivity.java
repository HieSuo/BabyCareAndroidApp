package com.example.babycareapp.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.babycareapp.R;
import com.example.babycareapp.data.repository.MainDeviceRepository;
import com.example.babycareapp.databinding.ActivityMainBinding;
import com.example.babycareapp.ui.viewmodel.MainDeviceViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    MainDeviceViewModel mainDeviceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainDeviceViewModel = new ViewModelProvider(this).get(MainDeviceViewModel.class);
        Log.d("MainActivity", "onCreate: create actvt");
        mainDeviceViewModel.listenForSoundDetection("dv001",new MainDeviceRepository.SoundDetectionCallback() {
            @Override
            public void onSoundDetected() {
                Log.d("MainAcitivity Done", "onSoundDetected: notificantion send ");
                createNotification();
            }
            @Override
            public void onError(Exception e) {
                Log.e("MainAcitivity FirebaseError", e.getMessage());
            }
        });

        mainDeviceViewModel.listenForOverTemperatureLimit("dv001", new MainDeviceRepository.OverTemperatureLimitCallback() {
            @Override
            public void onOverTemperatureLimit(String msg) {
                Log.d("MainActivity", "onOverTemperatureLimit: ");
                createTemperatureNotification();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        mainDeviceViewModel.listenForOverHumidityLimit("dv001", new MainDeviceRepository.OverTemperatureLimitCallback() {
            @Override
            public void onOverTemperatureLimit(String msg) {
                Log.d("MainActivity", "listenForOverHumidityLimit: ");
                createHumidityNotification();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_fragment_container, new HomeFragment())
                    .commit();
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    ListDevicesFragment listDevicesFragment = new ListDevicesFragment();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.navhome){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment_container, homeFragment).commit();
            return true;
        }
        if(item.getItemId()==R.id.navsetting){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment_container, settingsFragment).commit();
            return true;
        }
        if(item.getItemId()==R.id.navlist){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment_container, listDevicesFragment).commit();
            return true;
        }
        return false;
    }
    private void createNotification() {
        String channelId = "sound_detection_channel";
        String channelName = "Phát hiện em bé đang khóc!";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_crip_pink)
                .setContentTitle("Phát hiện âm thanh lớn!")
                .setContentText("Phát hiện em bé đang khóc, truy cập ứn dụng giám sát ngay.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }
    private void createTemperatureNotification(){
        String channelId = "over_temperature_limit_channel";
        String channelName = "Cảnh báo nhiệt độ an toàn";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_crip_pink)
                .setContentTitle("Cảnh báo nhiệt độ!")
                .setContentText("Phát hiện nhiệt độ không an toàn, truy cập ứn dụng giám sát ngay.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }
    private void createHumidityNotification(){
        String channelId = "over_humidity_limit_channel";
        String channelName = "Cảnh báo độ ẩm an toàn";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_crip_pink)
                .setContentTitle("Cảnh báo độ ẩm!")
                .setContentText("Phát hiện độ ẩm không an toàn, truy cập ứn dụng giám sát ngay.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }
}