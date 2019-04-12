package com.example.wongying.newnfq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);


        Button Searchbtn = (Button) findViewById(R.id.search);
        Searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInfo.this,SearchPage.class);
                startActivity(i);
            }
        });

        Button GoHome = (Button) findViewById(R.id.home);
        GoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInfo.this,HomePage.class);
                startActivity(i);
            }
        });
    }
}
