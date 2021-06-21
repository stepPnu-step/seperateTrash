package com.pnu.recycling;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Quiz2 extends AppCompatActivity {
    TextView text, score_check;
    //Button button, button02, button03;
    boolean i = true;
    private int score_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);
        text = (TextView)findViewById(R.id.text_quiz);

        score_check = findViewById(R.id.score_check);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int score = bundle.getInt("Score");
        score_2 = score;
        score_check.setText(score+"");

        Button btn1 = (Button)findViewById(R.id.btn_cha);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == true) {
                    text.setText("Find the Can!!");
                    i = false;
                }
                else{
                    text.setText("캔을 찾아라!!");
                    i = true;
                }
            }
        });

        final ImageButton button01 = (ImageButton)findViewById((R.id.btn_med)); //약
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_2--;
                score_check.setText(score_2+"");
                button01.setVisibility(View.INVISIBLE);
            }
        });
        final ImageButton button02 = (ImageButton)findViewById((R.id.btn_pla));    //플라스틱
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_2--;
                score_check.setText(score_2+"");
                button02.setVisibility(View.INVISIBLE);
            }
        });
        final ImageButton button03 = (ImageButton)findViewById((R.id.btn_vin));    //비닐
        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_2--;
                score_check.setText(score_2+"");
                button03.setVisibility(View.INVISIBLE);
            }
        });

        final ImageButton button = (ImageButton)findViewById(R.id.btn_can);          //캔
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_2+= 5;
                Intent intent = new Intent(getApplicationContext(), Quiz3.class);
                intent.putExtra("Score", score_2);
                startActivity(intent);
                finish();
            }
        });

        Button btn_home= (Button) findViewById(R.id.btn_home);      //Home버튼 클릭시 처음 메인화면으로 이동
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}