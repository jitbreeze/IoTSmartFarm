package com.example.smartfarmhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

//https://riucc.tistory.com/366
public class FoodFlipper extends AppCompatActivity {
    ListView foodList;
    public FoodAdapter adapter;
    public ArrayList<String> codeItems;
    public ArrayList<String> contentItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_flipper);
        foodList = findViewById(R.id.foodList);
        adapter = new FoodAdapter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                contentItems = new ArrayList<String>();
                codeItems = new ArrayList<String>();
                String apiKey = "20200810INQ7MT5YJBNBXD9JYA82W";
                String queryUrl="http://api.nongsaro.go.kr/service/recomendDiet/mainCategoryList?apiKey=" + apiKey;
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
                                else if(tag.equals("dietSeCode")){
                                    xpp.next();
                                    codeItems.add(xpp.getText());
                                }
                                else if(tag.equals("dietSeName")){
                                    xpp.next();
                                    contentItems.add(xpp.getText());
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
                         adapter.addItem(codeItems,contentItems);
                         adapter.notifyDataSetChanged();
                     }
                 });
            }
        }).start();

        foodList.setAdapter(adapter);
    }


    public class FoodAdapter extends BaseAdapter{
        public ArrayList<String> items = new ArrayList<String>();
        public ArrayList<String> codes = new ArrayList<String>();

        public void addItem(ArrayList<String> code, ArrayList<String> content){
            items = content;
            codes = code;
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
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.food_item, parent, false);
            }
            TextView contentType = convertView.findViewById(R.id.contentType);
            TextView codeType = convertView.findViewById(R.id.contentCode);
            codeType.setVisibility(TextView.INVISIBLE);
            contentType.setText(items.get(position));
            codeType.setText(codes.get(position));
            LinearLayout foodArea = convertView.findViewById(R.id.foodArea);
            foodArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text = v.findViewById(R.id.contentCode);
                    Intent intent = new Intent(getApplicationContext(), Diet.class);
                    intent.putExtra("dietSeCode", text.getText());
                    startActivity(intent);
//                    Toast.makeText(v.getContext(), text.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
    }
}
