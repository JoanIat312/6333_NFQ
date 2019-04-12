package com.example.wongying.newnfq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Button GoUser = (Button) findViewById(R.id.user);
        GoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchPage.this,UserInfo.class);
                startActivity(i);
            }
        });

        Button GoHome = (Button) findViewById(R.id.home);
        GoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchPage.this,HomePage.class);
                startActivity(i);
            }
        });
    }
}
