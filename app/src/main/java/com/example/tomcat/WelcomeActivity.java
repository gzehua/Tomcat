package com.example.tomcat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    //延迟3秒
    private static final long WELCOME_DELAY_MILLIS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);

        // 使用Handler的postDelayed方法，3秒后执行跳转到HomeActivity
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goHome();
            }
        }, WELCOME_DELAY_MILLIS);
    }

    private void goHome() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
