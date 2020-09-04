package com.example.smartfarmhome.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartfarmhome.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class HomeFragment extends Fragment {

    public Button btn_cam_on; public Button btn_temp; public Button btn_hum;
    public Button btn_light_on; public Button btn_light_off;
    public TextView temperature; public TextView humidity;
    Context mContext = null; EditText temedit; EditText humedit;
    private MqttAndroidClient mqttAndroidClient;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btn_temp = root.findViewById(R.id.btn_temp);
        btn_hum = root.findViewById(R.id.btn_hum);
        btn_light_on = root.findViewById(R.id.btn_light_on);
        btn_light_off = root.findViewById(R.id.btn_light_off);
        temperature = root.findViewById(R.id.temperature);
        humidity = root.findViewById(R.id.humidity);
        btn_cam_on = root.findViewById(R.id.btn_cam_on);
        humedit = root.findViewById(R.id.humedit);
        temedit = root.findViewById(R.id.temedit);
        mqttAndroidClient = new MqttAndroidClient(mContext,  "tcp://" + "192.168.137.88" + ":1883", MqttClient.generateClientId());
        //Toast.makeText(mContext,"아이디!"+ MqttClient.generateClientId(), Toast.LENGTH_SHORT).show();

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        mqttConnectOptions.setCleanSession(true);

        try {
            IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOption());    //mqtttoken 이라는것을 만들어 connect option을 달아줌
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());    //연결에 성공한경우
                    Toast.makeText(getActivity(),"Connected!!!", Toast.LENGTH_SHORT).show();
                    try {
                        mqttAndroidClient.subscribe("light", 0 );//연결에 성공하면 jmlee 라는 토픽으로 subscribe함
                        mqttAndroidClient.subscribe("temperature", 0 );//연결에 성공하면 jmlee 라는 토픽으로 subscribe함
                        mqttAndroidClient.subscribe("humidity", 0 );//연결에 성공하면 jmlee 라는 토픽으로 subscribe함
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {   //연결에 실패한경우
                    Toast.makeText(getActivity(),"Failed....", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        btn_light_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mqttAndroidClient.publish("light2", "456".getBytes(), 0 , false );
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_light_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mqttAndroidClient.publish("light2", "456".getBytes(), 0 , false );
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mqttAndroidClient.publish("temperature2", temedit.getText().toString().getBytes(), 0 , false );
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_hum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mqttAndroidClient.publish("humidity2", humedit.getText().toString().getBytes(), 0 , false );
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_cam_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.137.88:8091/?action=stream"));
                startActivity(intent);
            }
        });

        mqttAndroidClient.setCallback(new MqttCallback() {  //클라이언트의 콜백을 처리하는부분
            @Override
            public void connectionLost(Throwable cause) {
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {    //모든 메시지가 올때 Callback method
                if(topic.equals("temperature")){
                    String msg = new String(message.getPayload());
                    temperature.setText(msg);
                }
                else if(topic.equals("humidity")){
                    String msg = new String(message.getPayload());
                    humidity.setText(msg);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
        return root;
    }


    private DisconnectedBufferOptions getDisconnectedBufferOptions() {

        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(true);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setWill("aaa", "I am going offline".getBytes(), 1, true);
        return mqttConnectOptions;
    }

}