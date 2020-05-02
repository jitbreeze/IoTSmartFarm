package com.example.smartfarmhome.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.smartfarmhome.CalendarFragment;
import com.example.smartfarmhome.DiaryAdapter;
import com.example.smartfarmhome.DiaryFragment;
import com.example.smartfarmhome.R;
import com.google.android.material.tabs.TabLayout;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    TabLayout tabLayout; ViewPager viewPager; DiaryAdapter adapter;
    CalendarFragment calendarFragment; DiaryFragment diaryFragment;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        calendarFragment = new CalendarFragment(); diaryFragment = new DiaryFragment();
        viewPager = root.findViewById(R.id.diaryViewPager);
        tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Diary"));
        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));
        adapter = new DiaryAdapter(getFragmentManager());
        adapter.addItem(diaryFragment); adapter.addItem(calendarFragment);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;
    }
}