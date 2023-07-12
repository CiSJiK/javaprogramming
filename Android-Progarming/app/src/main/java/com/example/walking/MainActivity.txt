package com.example.walking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    static long step;
    static float distance = 100;
    Toolbar tb;
    Summary summary;
    Statistics statistics;
    Personal_infomation pi;
    Objection obj;
    SensorManager sm;
    Sensor sensor;
    BackgroundThread bgt;
    boolean sum_selected = true, obj_selected = false;

    public ThreadHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            FileInputStream is = openFileInput("laststate.txt");
            Scanner sc = new Scanner(is);
            distance = Float.parseFloat(sc.nextLine());
            if(sc.hasNextLine())
                step = Long.parseLong(sc.nextLine());
            sc.close();
        }catch(IOException e)
        {
            Toast.makeText(this, "설정을 가져오지 못했습니다.", Toast.LENGTH_LONG);
        }
        createTabs();
        startThread();
    }
    public void startThread()
    {
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        handler = new ThreadHandler();
        bgt = new BackgroundThread(sm, sensor);
        bgt.start();
        TimeLoading tl = new TimeLoading();
        tl.start();
    }
    public void createTabs()
    {
        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        summary = new Summary();
        statistics = new Statistics();
        pi = new Personal_infomation();
        obj = new Objection();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, summary).commit();

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("요약"));//setIcon 사용 가능
        tabs.addTab(tabs.newTab().setText("통계"));
        tabs.addTab(tabs.newTab().setText("개인정보"));
        tabs.addTab(tabs.newTab().setText("걸음 수"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                Fragment selected = null;
                switch (pos) {
                    case 0:
                        selected = summary;
                        sum_selected = true;
                        break;
                    case 1:
                        selected = statistics;
                        break;
                    case 2:
                        selected = pi;
                        break;
                    case 3:
                        selected = obj;
                        obj_selected = true;
                        break;
                    default:
                        selected = summary;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos == 0)
                    sum_selected = false;
                if(pos == 3)
                    obj_selected = false;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }
    class BackgroundThread extends Thread
    {
        private SensorManager sm;
        private Sensor sensor;
        public BackgroundThread(SensorManager sm, Sensor sensor)
        {
            this.sm = sm;
            this.sensor = sensor;
        }
        public void run()
        {
            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    step++;
                    if(sum_selected)
                        setData(step);
                    if(obj_selected)
                        setObjection();
                }
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            sm.registerListener(listener, sensor, Sensor.TYPE_STEP_DETECTOR);
        }
    }
    class TimeLoading extends Thread
    {
        private Calendar calendar;
        private Calendar saved;
        public TimeLoading()
        {
            saved = Calendar.getInstance();
            calendar = Calendar.getInstance();
        }
        @Override
        public void run()
        {
            try
            {
                while(this.isAlive())
                {
                    Thread.sleep(1000);
                    calendar = Calendar.getInstance();
                    if(calendar.get(Calendar.DAY_OF_MONTH) != saved.get(Calendar.DAY_OF_MONTH))
                    {
                        String date = (saved.get(Calendar.MONTH)+1) + "." + saved.get(Calendar.DATE);
                        try
                        {
                            FileInputStream is = openFileInput("steps.txt");
                            String data = "";
                            Scanner sc = new Scanner(is);
                            while(sc.hasNextLine())
                            {
                                data += sc.nextLine() + "\n";
                            }
                            sc.close();
                            is.close();
                            FileOutputStream fos = openFileOutput("steps.txt", Activity.MODE_PRIVATE);
                            PrintWriter pw = new PrintWriter(fos);
                            pw.append(data);
                            pw.append(date).println();
                            pw.append(step+"").println();
                            pw.flush();
                            pw.close();
                            fos.flush();
                            fos.close();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        saved = Calendar.getInstance();
                        step = 0;
                    }
                }
            }catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void setData(long step)
    {
        TextView steps = findViewById(R.id.feets);
        steps.setText(step + " 걸음");

        float result_kcal, result_distance;
        long result_time;

        result_kcal = (float) (Math.round((step * 0.03)*100)/100.0);
        result_distance = (float) (Math.round((step * (distance/100000))*100)/100.0);
        result_time = (long)(step * (3600/(500000/distance)));

        TextView kacl = findViewById(R.id.kcal);
        kacl.setText("소비 Kcal : " + result_kcal + "Kcal");

        TextView time = findViewById(R.id.time);
        time.setText("운동 시간 : " + result_time + "초");

        TextView dist = findViewById(R.id.distance);
        dist.setText("이동거리 : " + result_distance + "km");
    }
    public void setObjection()
    {
        TextView tv = findViewById(R.id.rate);
        float rate = (float) (100.0 * step / Objection.goal);
        tv.setText((rate) + "%");
    }
    class ThreadHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            step = bundle.getLong("step");
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try
        {
            FileOutputStream fos = openFileOutput("laststate.txt", Activity.MODE_PRIVATE);
            fos.write((distance +"\n").getBytes());
            fos.write((step + "\n").getBytes());
            fos.close();
        }catch (IOException e)
        {
            Toast.makeText(this, "보폭을 저장하는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
        }
        startThread();
    }
}

