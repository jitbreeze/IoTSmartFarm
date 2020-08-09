package com.example.smartfarmhome;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//김지수 작성
public class CalendarFragment extends Fragment {
    public MaterialCalendarView calendarView;
    ListView todoListView; todoAdapter todoAdapter;
    private DatabaseReference mDatabaseRef; private ValueEventListener mDBListener;
    private FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        todoListView = rootview.findViewById(R.id.todoList);
        todoAdapter = new todoAdapter();
        todoListView.setAdapter(todoAdapter);
        calendarView = rootview.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                String path = date.getYear() +". "+(date.getMonth()+1)+". "+date.getDay()+".";
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("diary/"+firebaseAuth.getCurrentUser().getUid());
                mDBListener = mDatabaseRef.orderByChild("date").equalTo(path).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        todoAdapter.clear();
                        if(dataSnapshot.exists()){
                            todoAdapter.addItem(new TodoItem("diary"));
                        }else{
                            Log.d("check","같은 데이터가 존재하지 않습니다.");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return rootview;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }


    class todoAdapter extends BaseAdapter {
        ArrayList<TodoItem> items = new ArrayList<TodoItem>();
        Context context;
        TextView todoText; ImageView todoIcon;

        public void addItem(TodoItem item){
            items.add(item);
            notifyDataSetChanged();
        }

        public void clear(){
            items.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            context = parent.getContext();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.todoitem_view, parent, false);
            }
            todoIcon = convertView.findViewById(R.id.todoIcon);
            todoText = convertView.findViewById(R.id.todoText);
            if(items.get(position).getIconType() == "diary"){
                todoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_diary));
                todoText.setText("식물일기 작성한 날");
            }else if(items.get(position).getIconType() == "nutrition"){
                todoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_nutrition));
                todoText.setText("영양제 준 날");
            }else if(items.get(position).getIconType() == "start"){
                todoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_harvest));
                todoText.setText("모종 심은 날");
            }else{
                todoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_harvest));
                todoText.setText("수확한 날");
            }
            return convertView;
        }
    }
}
