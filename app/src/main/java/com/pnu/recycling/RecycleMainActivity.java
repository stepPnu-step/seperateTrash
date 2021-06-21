package com.pnu.recycling;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecycleMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;//바텀 네비게이션뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private RecycleHomeFrag1 frag1;        //선택페이지
    private RecyclePlasticFrag2 frag2;        //플라스틱
    private RecycleCanFrag3 frag3;        //캔
    private RecycleEggFrag4 frag4;        //계란껍질
    private RecycleMedicineFrag5 frag5;        //약
    private RecyclePlasticBagFrag6 frag6;        //비닐


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopage);

        Button btn_home= (Button) findViewById(R.id.btn_home);      //Home버튼 클릭시 처음 메인화면으로 이동
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.action_info1:     //플라스틱
                        setFrag(1);
                        break;
                    case R.id.action_info2:     //캔
                        setFrag(2);
                        break;
                    case R.id.action_info3:     //계란껍질
                        setFrag(3);
                        break;
                    case R.id.action_info4:     //약
                        setFrag(4);
                        break;
                    case R.id.action_info5:     //비닐
                        setFrag(5);
                        break;


                }
                return true;
            }
        });

        frag1 = new RecycleHomeFrag1();        //시작
        frag2 = new RecyclePlasticFrag2();        //플라스틱
        frag3 = new RecycleCanFrag3();        //캔
        frag4 = new RecycleEggFrag4();        //계란껍질
        frag5 = new RecycleMedicineFrag5();        //약
        frag6 = new RecyclePlasticBagFrag6();        //비닐
        setFrag(0); //첫 화면 지정

    }
    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:     //선택 시작화면
                ft.replace(R.id.main_frame, frag1);
                ft.commit();
                break;
            case 1:     //플라스틱
                ft.replace(R.id.main_frame, frag2);
                ft.commit();
                break;
            case 2:     //캔
                ft.replace(R.id.main_frame, frag3);
                ft.commit();
                break;
            case 3:     //계란껍질
                ft.replace(R.id.main_frame, frag4);
                ft.commit();
                break;
            case 4:     //약
                ft.replace(R.id.main_frame, frag5);
                ft.commit();
                break;
            case 5:     //비닐
                ft.replace(R.id.main_frame, frag6);
                ft.commit();
                break;


        }
    }
}
