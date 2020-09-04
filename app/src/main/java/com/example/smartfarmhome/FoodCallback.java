package com.example.smartfarmhome;


import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;

public interface FoodCallback {
    void OnFinished(String rthImageDc,
                    String cntntSj,
                    String dietCn,
                    String dietNtrsmallInfo,
                    String matrlInfo,
                    String ckngMthInfo);
}
