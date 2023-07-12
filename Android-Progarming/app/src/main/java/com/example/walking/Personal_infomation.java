package com.example.walking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Personal_infomation extends Fragment {
    ImageView iv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_infomation, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iv = getActivity().findViewById(R.id.personal_image);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
        loadPI();

        Button btn = getActivity().findViewById(R.id.apply);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    FileOutputStream fos = getActivity().openFileOutput("save.txt", Activity.MODE_PRIVATE);
                    EditText et = getActivity().findViewById(R.id.name);
                    fos.write((et.getText().toString()+"\n").getBytes());
                    et = getActivity().findViewById(R.id.height);
                    Double height = Double.parseDouble(et.getText().toString());
                    fos.write((et.getText().toString()+"\n").getBytes());
                    et = getActivity().findViewById(R.id.weight);
                    Double weight = Double.parseDouble(et.getText().toString());
                    fos.write((et.getText().toString()+"\n").getBytes());
                    et = getActivity().findViewById(R.id.step);
                    MainActivity.distance = Float.parseFloat(et.getText().toString());
                    fos.write((et.getText().toString()+"\n").getBytes());
                    float bmi = (float)((weight*10000)/Math.pow(height, 2));
                    fos.write((bmi+"\n").getBytes());
                    TextView tv = getActivity().findViewById(R.id.bmi);
                    tv.setText("BMI지수: "+bmi);
                    Toast.makeText(getContext(), "저장이 완료되었습니다.", Toast.LENGTH_LONG).show();
                }catch(IOException e)
                {
                    Toast.makeText(getContext(), "파일을 저장하는 중 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 0)
        {
            if(resultCode == RESULT_OK)
            {
                try
                {
                    InputStream is = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    iv.setImageBitmap(bmp);
                }catch(IOException e)
                {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
                try
                {
                    File file = new File("default.png");
                    FileOutputStream fos = getActivity().openFileOutput("default.png", Activity.MODE_PRIVATE);
                    Bitmap bmp = ((BitmapDrawable)iv.getDrawable()).getBitmap();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                }catch(IOException e)
                {
                    Toast.makeText(getContext(), "Can't save image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void loadPI()
    {
        try
        {
            FileInputStream is = getActivity().openFileInput("default.png");
            Bitmap bmp = BitmapFactory.decodeStream(is);
            iv.setImageBitmap(bmp);
        }catch(FileNotFoundException e)
        {
            iv.setImageResource(R.drawable.gomplayer);
        }
        try
        {
            FileInputStream is = getActivity().openFileInput("save.txt");
            Scanner sc = new Scanner(is);
            EditText et = getActivity().findViewById(R.id.name);
            et.setText(sc.nextLine());
            et = getActivity().findViewById(R.id.height);
            et.setText(sc.nextLine());
            et = getActivity().findViewById(R.id.weight);
            et.setText(sc.nextLine());
            et = getActivity().findViewById(R.id.step);
            et.setText(sc.nextLine());
            TextView tv = getActivity().findViewById(R.id.bmi);
            tv.setText("BMI지수: " + sc.nextLine());
            sc.close();
        }catch(IOException e)
        {
            Toast.makeText(getContext(), "개인정보를 불러오는데 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    }
}