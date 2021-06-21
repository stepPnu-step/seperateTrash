package com.pnu.recycling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResult extends AppCompatActivity {
    TextView score_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        score_check = findViewById(R.id.score_check);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int score = bundle.getInt("Score");
        score_check.setText(score+"");

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
