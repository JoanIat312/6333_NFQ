package com.example.nfq;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome_page);
        handler.sendEmptyMessageDelayed(0,3000);

    }
    private Handler handler = new Handler(){
        public  void handleMessage(Message msg)
        {
            getHome();
            super.handleMessage(msg);
        }
    };

    public void getHome() {

        Intent intent = new Intent(WelcomePage.this,Login.class);
        startActivity(intent);
        finish();
    }
}

