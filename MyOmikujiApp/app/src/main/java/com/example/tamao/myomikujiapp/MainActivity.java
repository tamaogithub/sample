package com.example.tamao.myomikujiapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    // 追加
    private Button buttonPushMe;
    private ImageView imageAndroid;

//    static final int mImageButtons[] = {
//            R.id.image_button1, R.id.image_button2, R.id.image_button3,
//            R.id.image_button4, R.id.image_button5, R.id.image_button6,
//            R.id.image_button7, R.id.image_button8, R.id.image_button9
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }





    public void getOmikuji(View view) {

        /*
        Color.RED
        Color.BLACK
        Color.rgb(255,0,0)
        Color.argb(127,255,0,0)
        Color.parseColor("#ff0000")
         */



        buttonPushMe = (Button) findViewById(R.id.btn_pushme);
        buttonPushMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TextViewの取得
                TextView tv = (TextView) findViewById(R.id.myTextView);
                String[] results = {
                        "大吉",
                        "末吉",
                        "吉",
                        "凶"
                };

                // 乱数の生成
                Random randomGenerator = new Random();
                int num = randomGenerator.nextInt(results.length);   // 0-3


                if (num == 0) {
                    tv.setTextColor(Color.RED);
                    //追加
                    imageAndroid.setImageResource(R.drawable.daikihi);

                } else if (num == 1) {
                    tv.setTextColor(Color.rgb(114, 43, 43));
                    //追加
                    imageAndroid.setImageResource(R.drawable.duekichi);

                } else if (num == 2){
                    //追加
                    tv.setTextColor(Color.rgb(0, 0, 0));
                    imageAndroid.setImageResource(R.drawable.kichi);
                } else {
                    //追加
                    tv.setTextColor(Color.rgb(0, 0, 0));
                    imageAndroid.setImageResource(R.drawable.kyou);
                }
                tv.setText(results[num]);
                buttonPushMe.setText("もう一度占う！");

            }
        });
        imageAndroid = (ImageView) findViewById(R.id.iv_android);
    }

}
