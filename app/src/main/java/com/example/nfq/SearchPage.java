package com.example.nfq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchPage extends AppCompatActivity {

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        currentUser = (User) getIntent().getSerializableExtra("current_user");
        if(currentUser == null){
            Toast.makeText(this,"Opps, something is wrong. Please log in again.",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SearchPage.this, Login.class);
            startActivity(intent);
        }

        Button Userbtn = (Button)findViewById(R.id.user);
        Userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchPage.this,UserInfo.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });

        Button Homebtn = (Button)findViewById(R.id.home);
        Homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchPage.this,HomePage.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });

        Button Notebtn = (Button)findViewById(R.id.note);
        Notebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchPage.this,NoteListActivity.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });

    }
}
