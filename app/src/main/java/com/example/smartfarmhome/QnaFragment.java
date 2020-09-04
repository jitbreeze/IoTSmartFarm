package com.example.smartfarmhome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class QnaFragment extends Fragment {
    ListView qnaList; String searchItem; QuestionAdapter adapter;
    String apiKey = "20200831DU8INU1KP081UELDKBKMA";
    String queryUrl = "http://api.nongsaro.go.kr/service/elctrnCvpl/elctrnCvplList";
    public ArrayList<String> wrterNm;
    public ArrayList<String> cntntsSj;
    public ArrayList<String> cntntsNos;
    public ArrayList<QuestionItem> items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            searchItem = bundle.getString("searchItem");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qna, container, false);
        qnaList = view.findViewById(R.id.qnaList);
        items = new ArrayList<>();
        queryUrl += "?apiKey="+apiKey+"&pageNo=null&searchtype=1&searchword="+searchItem;
        new Thread(new Runnable() {
            @Override
            public void run() {
                cntntsSj = new ArrayList<String>();
                wrterNm = new ArrayList<String>();
                cntntsNos = new ArrayList<String>();
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
                                    cntntsSj.add(xpp.getText());
                                }
                                else if(tag.equals("wrterNm")){
                                    xpp.next();
                                    wrterNm.add(xpp.getText());
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<cntntsNos.size();i++){
                            items.add(new QuestionItem(cntntsNos.get(i), cntntsSj.get(i) ,wrterNm.get(i)));
                        }
                        adapter = new QuestionAdapter(getActivity(),items);
                        qnaList.setAdapter(adapter);
                    }
                });
            }
        }).start();
        return view;
    }

    public class QuestionItem{
        String cntntsNos;
        String cntntsSj;
        String wrterNm;

        public QuestionItem(String cntntsNos, String cntntsSj, String wrterNm) {
            this.cntntsNos = cntntsNos;
            this.cntntsSj = cntntsSj;
            this.wrterNm = wrterNm;
        }

        public String getCntntsNos() {
            return cntntsNos;
        }

        public void setCntntsNos(String cntntsNos) {
            this.cntntsNos = cntntsNos;
        }

        public String getCntntsSj() {
            return cntntsSj;
        }

        public void setCntntsSj(String cntntsSj) {
            this.cntntsSj = cntntsSj;
        }

        public String getWrterNm() {
            return wrterNm;
        }

        public void setWrterNm(String wrterNm) {
            this.wrterNm = wrterNm;
        }
    }
    public class QuestionAdapter extends BaseAdapter {
        private Context mContext;
        public ArrayList<QuestionItem> items;

        public QuestionAdapter(Context mContext, ArrayList<QuestionItem> items) {
            this.mContext = mContext;
            this.items = items;
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
                convertView = inflater.inflate(R.layout.question_item, parent, false);
            }
            TextView cntntsSj = convertView.findViewById(R.id.cntntsSj);
            TextView cntntsNo = convertView.findViewById(R.id.cntntsNo);
            TextView wrterNm = convertView.findViewById(R.id.wrterNm);
            LinearLayout questionArea = convertView.findViewById(R.id.questionArea);
            questionArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text = v.findViewById(R.id.cntntsNo);
                    Intent intent = new Intent(getActivity(), QnaDetail.class);
                    intent.putExtra("contentNm", text.getText());
                    startActivity(intent);
                }
            });
            cntntsNo.setText(items.get(position).getCntntsNos());
            cntntsSj.setText(items.get(position).getCntntsSj());
            wrterNm.setText(items.get(position).getWrterNm());
            cntntsNo.setVisibility(TextView.INVISIBLE);
            return convertView;
        }
    }
}
