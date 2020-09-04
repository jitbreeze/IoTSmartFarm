package com.example.smartfarmhome;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
//김지수 작성
public class DiaryPicZoom extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diray_pic_zoom);
        getSupportActionBar().hide();
        PhotoView photoView =  findViewById(R.id.photoview);
        String url = getIntent().getStringExtra("img");
        Glide.with(getApplicationContext()).load(url).into(photoView);
    }

}
