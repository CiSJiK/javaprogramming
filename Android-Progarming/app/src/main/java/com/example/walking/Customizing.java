package com.example.walking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Customizing extends AppCompatActivity {
    ImageView iv[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customizing);
        Intent intent = getIntent();
        iv = new ImageView[5];
        for(int i = 0; i < 5; i++)
        {
            switch(i) {
                case 0: iv[i] = findViewById(R.id.image1); break;
                case 1: iv[i] = findViewById(R.id.image2); break;
                case 2: iv[i] = findViewById(R.id.image3); break;
                case 3: iv[i] = findViewById(R.id.image4); break;
                case 4: iv[i] = findViewById(R.id.image5); break;
            }
            try
            {
                FileInputStream is = openFileInput("default" + i + ".png");
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                iv[i].setImageBitmap(bmp);
            }
            catch (IOException e)
            {
                Toast.makeText(this, "불러오기 실패", Toast.LENGTH_LONG).show();
                iv[i].setImageResource(R.drawable.gomplayer);
            }
            saveImage(iv[i], i+1000);
        }
    }
    public void saveImage(ImageView iv, int requestCode) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, requestCode);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        int i = requestCode - 1000;
        if(resultCode == RESULT_OK)
        {
            Bitmap bmp = null;
            try
            {
                InputStream is = getContentResolver().openInputStream(data.getData());
                bmp = BitmapFactory.decodeStream(is);
                is.close();
                FileOutputStream fos = openFileOutput("default" + i + ".png", Activity.MODE_PRIVATE);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
            catch(IOException e)
            {
                Toast.makeText(this, "파일을 저장하는데 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
            }
            iv[i].setImageBitmap(bmp);
        }
    }
}