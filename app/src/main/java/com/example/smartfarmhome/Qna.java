package com.example.smartfarmhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.smartfarmhome.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Qna extends AppCompatActivity {
    EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        search_bar = findViewById(R.id.search_bar);
        Button search_button = findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QnaFragment fragment = new QnaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("searchItem", search_bar.getText().toString());
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.qnacontainer, fragment).commit();
            }
        });
    }

}
