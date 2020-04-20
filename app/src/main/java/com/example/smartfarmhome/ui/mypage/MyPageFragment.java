package com.example.smartfarmhome.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartfarmhome.R;
import com.example.smartfarmhome.SmartDiagnose;

public class MyPageFragment extends Fragment {
    private MyPageViewModel myPageViewModel;

    Button mp_History; Button mp_SmartDiagnose; Button mp_Info;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPageViewModel =
                ViewModelProviders.of(this).get(com.example.smartfarmhome.ui.mypage.MyPageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        mp_History = root.findViewById(R.id.mp_History);
        mp_SmartDiagnose = root.findViewById(R.id.mp_SmartDiagnose);
        mp_Info = root.findViewById(R.id.mp_Info);

        mp_SmartDiagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SmartDiagnose.class);
                startActivity(intent);
            }
        });
        return root;
    }
}