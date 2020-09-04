package com.example.smartfarmhome.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.smartfarmhome.Addpost;
import com.example.smartfarmhome.FoodFlipper;
import com.example.smartfarmhome.Post_item;
import com.example.smartfarmhome.Post_item_view;
import com.example.smartfarmhome.Qna;
import com.example.smartfarmhome.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import android.widget.ViewFlipper;

//레이아웃 작성: 오현지, 데이터베이스 연동 작성: 김지수
public class DashboardFragment extends Fragment {
    private DatabaseReference mDatabaseRef; private ValueEventListener mDBListener;
    private DashboardViewModel dashboardViewModel; private FirebaseStorage mStorage;
    PostAdapter adapter; private ArrayList<Post_item> items;
    ViewFlipper flipper;
    ListView postlist;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("post");
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Button button = root.findViewById(R.id.wrbutton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getActivity(), Addpost.class);
                startActivity(intent);
            }
        });

        flipper = root.findViewById(R.id.flipper);
        int recipes[] = {
                R.drawable.food,
                R.drawable.food2,
        };

        for(int image : recipes) {
            ImageView recipeView = new ImageView(getActivity());
            recipeView.setBackgroundResource(image);

            flipper.addView(recipeView);      // 이미지 추가
            flipper.setFlipInterval(4000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
            flipper.setAutoStart(true);          // 자동 시작 유무 설정
            flipper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flipper.getDisplayedChild() == 0){
                        Intent intent = new Intent(getActivity(), FoodFlipper.class);
                        startActivity(intent);
                    }else if(flipper.getDisplayedChild()==1){
                        Intent intent = new Intent(getActivity(), Qna.class);
                        startActivity(intent);
                    }
                }
            });
            // animation
            flipper.setInAnimation(getActivity(),android.R.anim.slide_in_left);
            flipper.setOutAnimation(getActivity(),android.R.anim.slide_out_right);
        }

        items = new ArrayList<>();
        adapter = new PostAdapter(items);
        postlist = root.findViewById(R.id.postlist);
        postlist.setAdapter(adapter);
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    Post_item item = postSnapShot.getValue(Post_item.class);
                    item.setMkey(postSnapShot.getKey());
                    items.add(item);
                }
                //progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);

    }
    class PostAdapter extends BaseAdapter {
        ArrayList<Post_item> items = new ArrayList<Post_item>();

        public PostAdapter(ArrayList<Post_item> items) {
            this.items = items;
        }
        @Override
        public int getCount() {
            return items.size();
        }
        public void addItem(Post_item item){
            items.add(item);
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
            Post_item_view view = null;
            if(convertView==null){
                view = new Post_item_view(getActivity());
            }else{
                view= (Post_item_view) convertView;
            }
            Post_item item= items.get(position);
            view.setPostId(item.getPostId());
            view.setCaption(item.getCaption());
            view.setpicId(item.getPicId());
            return view;
        }
    }


}