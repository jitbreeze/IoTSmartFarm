package com.example.smartfarmhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Diet extends AppCompatActivity {
    ViewPager dietList; DietAdapter adapter;
    public ArrayList<String> rtnImageDcs;
    public ArrayList<String> cntntsSjs;
    public ArrayList<String> cntntsNos;
    public ArrayList<DietItem> items;
    public String dietSeCode;
    Integer[] colors=null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        dietSeCode = getIntent().getStringExtra("dietSeCode");
        Toast.makeText(getApplicationContext(), dietSeCode, Toast.LENGTH_SHORT).show();
        dietList = findViewById(R.id.dietList);
        items = new ArrayList<>();

        final Integer[] colorItems = {
                getResources().getColor(R.color.colorFirst),
                getResources().getColor(R.color.colorSecond),
                getResources().getColor(R.color.colorThrid),
                getResources().getColor(R.color.colorFourth),
                getResources().getColor(R.color.colorLast)
        };
        colors = colorItems;
        new Thread(new Runnable() {
            @Override
            public void run() {
                rtnImageDcs = new ArrayList<String>();
                cntntsSjs = new ArrayList<String>();
                cntntsNos = new ArrayList<String>();
                String apiKey = "20200810INQ7MT5YJBNBXD9JYA82W";
                String queryUrl="http://api.nongsaro.go.kr/service/recomendDiet/recomendDietList?apiKey=" +apiKey+ "&dietSeCode=" + dietSeCode;
                try {
                    URL url= new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
                    InputStream is= url.openStream(); // url 위치로 인풋스트림 연결
                    XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                    XmlPullParser xpp= factory.newPullParser();
                    xpp.setInput( new InputStreamReader(is, "UTF-8") );
                    String tag;
                    xpp.next();
                    int eventType= xpp.getEventType();
                    while( eventType != XmlPullParser.END_DOCUMENT ){
                        switch( eventType ){
                            case XmlPullParser.START_DOCUMENT:
                                // 파싱시작
                                break;
                            case XmlPullParser.START_TAG:
                                tag= xpp.getName(); // 태그 이름 얻어오기
                                if(tag.equals("item")) ;
                                else if(tag.equals("cntntsNo")){
                                    xpp.next();
                                    cntntsNos.add(xpp.getText());
                                }
                                else if(tag.equals("cntntsSj")){
                                    xpp.next();
                                    cntntsSjs.add(xpp.getText());
                                }
                                else if(tag.equals("rtnImageDc")){
                                    xpp.next();
                                    rtnImageDcs.add(xpp.getText());
                                }
                            case XmlPullParser.TEXT:
                                break;
                            case XmlPullParser.END_TAG:
                                //tag= xpp.getName(); // 태그 이름 얻어오기
                                //if(tag.equals("item")) buffer.append("\n"); // 첫번째 검색결과종료 후 줄바꿈
                                break;
                        }
                        eventType= xpp.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<cntntsNos.size();i++){
                            items.add(new DietItem(cntntsNos.get(i), rtnImageDcs.get(i) ,cntntsSjs.get(i)));
                        }
                        adapter = new DietAdapter(items,getApplicationContext());
                        dietList.setAdapter(adapter);
                    }
                });
            }
        }).start();

        dietList.setPadding(130,0,130,0);
        dietList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position < ((adapter).getCount()-1) && position < (colors.length-1) ){
                    dietList.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position+1]
                            )
                    );
                }else{
                    dietList.setBackgroundColor(colors[colors.length-1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class DietItem{
        private String contentNo;
        private String contentImage;
        private String contentName;

        public DietItem(String contentNo, String contentImage, String contentName) {
            this.contentNo = contentNo;
            this.contentImage = contentImage;
            this.contentName = contentName;
        }

        public String getContentNo() {
            return contentNo;
        }

        public void setContentNo(String contentNo) {
            this.contentNo = contentNo;
        }

        public String getContentImage() {
            return contentImage;
        }

        public void setContentImage(String contentImage) {
            this.contentImage = contentImage;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }
    }
    public class DietAdapter extends PagerAdapter {
        private ArrayList<DietItem> items;
        private Context mContext;

        public DietAdapter(ArrayList<DietItem> items, Context mContext) {
            this.items = items;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.diet_item, container, false);
            ImageView dietImage = view.findViewById(R.id.dietImage);
            TextView dietName = view.findViewById(R.id.dietName);
            TextView cntnum = view.findViewById(R.id.cntnum);

            cntnum.setText(items.get(position).getContentNo());
            Glide.with(mContext).load(items.get(position).getContentImage()).into(dietImage);
            dietName.setText(items.get(position).getContentName());

            LinearLayout dietArea = view.findViewById(R.id.dietArea);
            dietArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView cntnum = v.findViewById(R.id.cntnum);
                    String cntntsNo = cntnum.getText().toString();
                    FoodFragment fragment = new FoodFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("cntntsNo" ,cntntsNo);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            });
            container.addView(view,0);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }
}
