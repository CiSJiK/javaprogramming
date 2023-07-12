package com.example.walking;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Summary extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setData(MainActivity.step);
    }
    public void setData(long step)
    {
        TextView steps = getActivity().findViewById(R.id.feets);
        steps.setText(step + " 걸음");

        float result_kcal, result_distance;
        long result_time;

        result_kcal = (float) (Math.round((step * 0.03)*100)/100.0);
        result_distance = (float) (Math.round((step * (MainActivity.distance/100000))*100)/100.0);
        result_time = (long)(step * (3600/(500000/MainActivity.distance)));

        TextView kacl = getActivity().findViewById(R.id.kcal);
        kacl.setText("소비 Kcal : " + result_kcal + "Kcal");

        TextView time = getActivity().findViewById(R.id.time);
        time.setText("운동 시간 : " + result_time + "초");

        TextView dist = getActivity().findViewById(R.id.distance);
        dist.setText("이동거리 : " + result_distance + "km");
    }
}
