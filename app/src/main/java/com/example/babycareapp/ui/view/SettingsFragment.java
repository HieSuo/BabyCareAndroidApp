package com.example.babycareapp.ui.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.babycareapp.R;
import com.example.babycareapp.databinding.FragmentSettingsBinding;
import com.example.babycareapp.ui.view.settingview.ProfileActivity;
import com.example.babycareapp.ui.viewmodel.AuthViewModel;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private AuthViewModel authViewModel;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Inflate the layout for this fragment

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnLogout.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Đăng xuất");
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");

            //đồng ý
            builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    authViewModel.logout();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();

        });

        binding.layoutListSettings.setOnClickListener(v->{
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8.0 trở lên
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
            } else { // Dành cho các phiên bản Android thấp hơn
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
            }
            startActivity(intent);
        });

        binding.settingAccount.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

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