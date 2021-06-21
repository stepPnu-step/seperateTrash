package com.pnu.recycling;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Quiz3 extends AppCompatActivity {
    TextView text, score_check;
    //Button button, button02, button03;
    boolean i = true;
    private int score_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);
        text = (TextView)findViewById(R.id.text_quiz);

        Button btn1 = (Button)findViewById(R.id.btn_cha);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == true) {
                    text.setText("Find the Medicine!!");
                    i = false;
                }
                else{
                    text.setText("약을 찾아라!!");
                    i = true;
                }
            }
        });

        score_check = findViewById(R.id.score_check);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int score = bundle.getInt("Score");
        score_3 = score;
        score_check.setText(score+"");

        final ImageButton button01 = (ImageButton)findViewById((R.id.btn_can)); //캔
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_3--;
                score_check.setText(score_3+"");
                button01.setVisibility(View.INVISIBLE);
            }
        });
        final ImageButton button02 = (ImageButton)findViewById((R.id.btn_egg));    //계란
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_3--;
                score_check.setText(score_3+"");
                button02.setVisibility(View.INVISIBLE);
            }
        });
        final ImageButton button03 = (ImageButton)findViewById((R.id.btn_pla));    //플라스틱
        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_3--;
                score_check.setText(score_3+"");
                button03.setVisibility(View.INVISIBLE);
            }
        });

        final ImageButton button = (ImageButton)findViewById(R.id.btn_med);          //약
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_3+= 5;
                Intent intent = new Intent(getApplicationContext(), com.pnu.recycling.QuizResult.class);
                intent.putExtra("Score", score_3);
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