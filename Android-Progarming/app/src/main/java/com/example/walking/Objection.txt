package com.example.walking;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static android.app.Activity.RESULT_OK;


public class Objection extends Fragment {

    static int goal;
    static float rate;
    private ImageView img;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_objection, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        img = activity.findViewById(R.id.img);
        selection();
        Button usr_btn = activity.findViewById(R.id.btn_user);
        Button default_btn = activity.findViewById(R.id.btn_basis);

        usr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Customizing.class);
                startActivity(intent);
            }
        });

        default_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    ImageView iv = activity.findViewById(R.id.img);
                    for(int i = 0; i <= 4; i++)
                    {
                        switch(i) {
                            case 0: iv.setImageResource(R.drawable.basis_emticon1);break;
                            case 1: iv.setImageResource(R.drawable.basis_emticon2);break;
                            case 2: iv.setImageResource(R.drawable.basis_emticon3);break;
                            case 3: iv.setImageResource(R.drawable.basis_emticon4);break;
                            case 4: iv.setImageResource(R.drawable.basis_emticon5);break;
                        }
                        Bitmap bmp = ((BitmapDrawable)iv.getDrawable()).getBitmap();
                        FileOutputStream fos = getActivity().openFileOutput("default" + i +".png", Activity.MODE_PRIVATE);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    }
                }
                catch(IOException e)
                {
                    Toast.makeText(getContext(), "저장에 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
                selection();
            }
        });

        try {
            FileInputStream fis = activity.openFileInput("save2.txt");
            Scanner sc = new Scanner(fis);

            EditText et = activity.findViewById(R.id.goal);
            goal = Integer.parseInt(sc.nextLine());
            et.setText(goal + "");


            TextView tv = activity.findViewById(R.id.rate);
            rate = Float.parseFloat(sc.nextLine());
            tv.setText(rate + "%");

            sc.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), "값이 설정되어 있지 않습니다.", Toast.LENGTH_LONG).show();
        }
        Button btn = getActivity().findViewById(R.id.btn_set);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText et = activity.findViewById(R.id.goal);
                    goal = Integer.parseInt(et.getText().toString());

                    TextView tv = activity.findViewById(R.id.rate);
                    rate = (float) (100.0 * MainActivity.step / goal);
                    tv.setText((rate) + "%");

                    String s_goal, s_rate;
                    s_goal = goal + "\n";
                    s_rate = rate + "\n";

                    FileOutputStream fileOutputStream = activity.openFileOutput("save2.txt", Activity.MODE_PRIVATE);
                    fileOutputStream.write(s_goal.getBytes());
                    fileOutputStream.write(s_rate.getBytes());
                    fileOutputStream.close();
                    Toast.makeText(activity, "저장완료", Toast.LENGTH_LONG).show();
                    selection();
                } catch (Exception e) {
                    Toast.makeText(activity, "저장실패", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button btn_walk = getActivity().findViewById(R.id.btn_walk);
        btn_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://youtu.be/KzGmjwCJTXo");
                intent.setData(uri);
                startActivity(intent);

            }
        });
    }
    public void selection()
    {
        Activity activity = getActivity();
        if(rate<25){
            try{
                FileInputStream is = activity.openFileInput("default" + 0 + ".png");
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                img.setImageBitmap(bmp);
            }catch (IOException e)
            {
                Toast.makeText(getContext(), "오류발생",Toast.LENGTH_LONG).show();
                img.setImageResource(R.drawable.basis_emticon1);
            }

        }

        else if(rate>=25 && rate < 50){
            try{
                FileInputStream is = activity.openFileInput("default" + 1 + ".png");
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                img.setImageBitmap(bmp);
            }catch (IOException e)
            {
                Toast.makeText(getContext(), "오류발생",Toast.LENGTH_LONG).show();
                img.setImageResource(R.drawable.basis_emticon2);
            }
        }

        else if(rate>=50 && rate<75){
            try{
                FileInputStream is = activity.openFileInput("default" + 2 + ".png");
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                img.setImageBitmap(bmp);
            }catch (IOException e)
            {
                Toast.makeText(getContext(), "오류발생",Toast.LENGTH_LONG).show();
                img.setImageResource(R.drawable.basis_emticon3);
            }
        }

        else if(rate>=75 && rate<100){
            try{
                FileInputStream is = activity.openFileInput("default" + 3 + ".png");
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                img.setImageBitmap(bmp);
            }catch (IOException e)
            {
                Toast.makeText(getContext(), "오류발생",Toast.LENGTH_LONG).show();
                img.setImageResource(R.drawable.basis_emticon4);
            }
        }

        else{
            try{
                FileInputStream is = activity.openFileInput("default" + 4 + ".png");
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                img.setImageBitmap(bmp);
            }catch (IOException e)
            {
                Toast.makeText(getContext(), "오류발생",Toast.LENGTH_LONG).show();
                img.setImageResource(R.drawable.basis_emticon5);
            }
        }
    }

}

