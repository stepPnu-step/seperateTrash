package com.pnu.recycling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {       //선택해서 하는거

    private BottomNavigationView bottomNavigationView;//바텀 네비게이션뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private MainCameraFrag1 main_frag1;        //카메라
    private MainUploadFrag2 main_frag2;        //업로드
    private MainRecycleFrag3 main_frag3;        //분리수거 방법
    private MainQuizFrag4 main_frag4;        //퀴즈
    private MainHomeFrag5 main_frag5;        // 홈화면 페이지

    private static final int REQUEST_USED_PERMISSION = 200;
    private static final int REQUEST_USED_INTERNET = 100;
    private static final int REQUEST_USED_READ_EXTERNAL_STORAGE = 300;


    private static final String[] needPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    // 카메라 권한확인
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permissionToCameraAccepted = true;

        switch (requestCode){
            case REQUEST_USED_PERMISSION:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        permissionToCameraAccepted = false;
                        break;
                    }
                }
                break;
            case REQUEST_USED_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_USED_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (permissionToCameraAccepted == false){
            Toast.makeText(MainActivity.this, "카메라 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check permissions
        for (String permission : needPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, needPermissions, REQUEST_USED_PERMISSION);
                break;
            }
        }
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.action_minfo1:     //카메라
                        setFrag(0);
                        break;
                    case R.id.action_minfo2:     //개별 업로드
                        setFrag(1);
                        break;
                    case R.id.action_minfo3:     //설명화면
                        Intent intent = new Intent(
                                getApplicationContext(),
                                RecycleMainActivity.class);
                        startActivity(intent);
                        finish();       //뒤로가기 중복된는걸 방지
                        break;
                    case R.id.action_minfo4:     //퀴즈
                        setFrag(3);
                        break;


                }
                return true;
            }
        });

        main_frag1 = new MainCameraFrag1();        //카메라
        main_frag2 = new MainUploadFrag2();        //업로드
        main_frag3 = new MainRecycleFrag3();        //설명페이지
        main_frag4 = new MainQuizFrag4();        //퀴즈페이지
        main_frag5 = new MainHomeFrag5();        //시작
        setFrag(4); //첫 화면 지정

    }
    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:     //카메라
                ft.replace(R.id.main_frame, main_frag1);
                ft.commit();
                break;
            case 1:     //업로드
                ft.replace(R.id.main_frame, main_frag2);
                ft.commit();
                break;
            case 2:     //설명 페이지
                ft.replace(R.id.main_frame, main_frag3);
                ft.commit();
                break;
            case 3:     //퀴즈 페이지
                ft.replace(R.id.main_frame, main_frag4);
                ft.commit();
                break;
            case 4:     //시작 페이지
                ft.replace(R.id.main_frame, main_frag5);
                ft.commit();
                break;



        }
    }
}
