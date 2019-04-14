package com.example.nfq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        currentUser = (User) getIntent().getSerializableExtra("current_user");
        if(currentUser == null){
            Toast.makeText(this,"Opps, something is wrong. Please log in again.",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomePage.this, Login.class);
            startActivity(intent);
        }
        Button Searchbtn = (Button)findViewById(R.id.search);
        Searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,SearchPage.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });

        Button Userbtn = (Button)findViewById(R.id.user);
        Userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,UserInfo.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });

        Button Notebtn = (Button)findViewById(R.id.note);
        Notebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,NoteListActivity.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });

        Button View = (Button)findViewById(R.id.view);
        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,ViewActivity.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });


    }
}
