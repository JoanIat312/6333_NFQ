package com.example.wongying.newnfq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Button Searchbtn = (Button)findViewById(R.id.search);
        Searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,SearchPage.class);
                startActivity(intent);
            }
        });

        Button Userbtn = (Button)findViewById(R.id.user);
        Userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomePage.this,UserInfo.class);
                startActivity(intent1);
            }
        });

        Button View = (Button)findViewById(R.id.view);
        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomePage.this,ViewActivity.class);
                startActivity(intent1);
            }
        });


    }
}
