package com.example.nfq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfo extends AppCompatActivity {

    User currentUser;
    TextView mEtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        currentUser = (User) getIntent().getSerializableExtra("current_user");
        mEtUsername = (TextView) findViewById(R.id.current_user_name);

        if(currentUser == null){
            Toast.makeText(this,"Opps, something is wrong. Please log in again.",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserInfo.this, Login.class);
            startActivity(intent);
        }else{
            mEtUsername.setText(currentUser.getUserName());
        }

        Button Searchbtn = (Button)findViewById(R.id.search);
        Searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfo.this,SearchPage.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });

        Button Notebtn = (Button)findViewById(R.id.note);
        Notebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(UserInfo.this,NoteListActivity.class).putExtra("current_user", currentUser);
                startActivity(intent1);
            }
        });
        Button Homebtn = (Button)findViewById(R.id.home);
        Homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfo.this,HomePage.class).putExtra("current_user", currentUser);
                startActivity(intent);
            }
        });



    }
}
