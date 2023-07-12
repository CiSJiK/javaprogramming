package com.example.walking;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Statistics extends Fragment {
    private LineChart chart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        String data[][] = new String[1000][1000];
        try
        {
            FileInputStream is = getActivity().openFileInput("steps.txt");
            Scanner sc = new Scanner(is);
            for(int i = 0; i < 1000; i++)
            {
                if(sc.hasNextLine())
                {
                    data[i][0] = sc.nextLine();
                    data[i][1] = sc.nextLine();
                }
                else
                    data[i] = null;
            }
            sc.close();
            chart = getActivity().findViewById(R.id.chart);
            List<Entry> entries = new ArrayList<> ();
            int j = 0;
            for(j = 0; data[j] != null; j++)
            {
                entries.add(new Entry(Float.parseFloat(data[j][0]), Long.parseLong(data[j][1])));
            }
            LineDataSet dataSet = new LineDataSet(entries, "날짜별 걸음수");
            dataSet.setColor(Color.BLUE);
            dataSet.setValueTextColor(Color.GRAY);
            dataSet.setCircleRadius((float)10);
            dataSet.setCircleHoleRadius((float)10);
            dataSet.setValueTextSize((float) 15);
            dataSet.setLineWidth((float)5);
            LineData linedata = new LineData(dataSet);
            chart.setData(linedata);
            chart.invalidate();
        }
        catch(IOException e)
        {
            Toast.makeText(getContext(), "파일을 불러오는데 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    }
}

