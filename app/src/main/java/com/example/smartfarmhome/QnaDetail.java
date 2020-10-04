package com.example.smartfarmhome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class QnaDetail extends AppCompatActivity {
    public String queryUrl ="http://api.nongsaro.go.kr/service/elctrnCvpl/elctrnCvplView?apiKey=20200831DU8INU1KP081UELDKBKMA&cntntsNo=";
    String cntntNm;  public String questDtl; String regDt; String wrterNm;
    String answerDtl; ImageView image; String cntntsSj;
    TextView tquestDtl; TextView tregDt;  TextView twrterNm;
    TextView tcntntsSj; public String imageUrl; TextView tanswerDtl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_detail);
        cntntNm = getIntent().getStringExtra("contentNm");
        tcntntsSj = findViewById(R.id.cntntsSj);
        tquestDtl = findViewById(R.id.questDtl);
        twrterNm = findViewById(R.id.wrterNm);
        tregDt = findViewById(R.id.regDt);
        tanswerDtl = findViewById(R.id.answerDtl);
        queryUrl+= cntntNm;
        image = findViewById(R.id.quesImage);

        new Thread(new Runnable() {
            @Override
            public void run() {
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
                                else if(tag.equals("cntntsSj")){
                                    xpp.next();
                                    cntntsSj = xpp.getText();
                                }
                                else if(tag.equals("wrterNm")){
                                    xpp.next();
                                    wrterNm = xpp.getText();
                                }
                                else if(tag.equals("regDt")){
                                    xpp.next();
                                    regDt = xpp.getText();
                                }
                                else if(tag.equals("questDtl")){
                                    xpp.next();
                                    questDtl = xpp.getText();
                                }
                                else if(tag.equals("answerDtl")){
                                    xpp.next();
                                    answerDtl = xpp.getText();
                                }
                            case XmlPullParser.TEXT:
                                break;
                            case XmlPullParser.END_TAG:
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
                        tcntntsSj.setText(cntntsSj);
                        twrterNm.setText(wrterNm);
                        tregDt.setText(regDt);
                        tquestDtl.setText(htmlToArticle(questDtl));
                        tanswerDtl.setText(htmlToArticle(answerDtl));
                        if(imageParser(questDtl) == null) image.setVisibility(View.INVISIBLE);
                        else Glide.with(getApplicationContext()).load(imageParser(questDtl)).into(image);
                    }
                });
            }
        }).start();
    }

    public String htmlToArticle(String xml){
        Document doc = Jsoup.parse(xml);
        return doc.text();
    }

    public String imageParser(String xml){
        Document doc = Jsoup.parse(xml);
        Elements imgs = doc.getElementsByTag("img");
        if(imgs.size() > 0) {
            imageUrl = imgs.get(0).attr("src");
        }
        return imageUrl;
    }


}
