package com.example.nfq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ViewActivity extends AppCompatActivity {

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        currentUser = (User) getIntent().getSerializableExtra("current_user");
        if(currentUser == null){
            Toast.makeText(this,"Opps, something is wrong. Please log in again.",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewActivity.this, Login.class);
            startActivity(intent);
        }
        Button Return = (Button)findViewById(R.id.back);
        Return.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ViewActivity.this,HomePage.class).putExtra("current_user", currentUser);
                startActivity(intent1);
            }
        });
    }
}
