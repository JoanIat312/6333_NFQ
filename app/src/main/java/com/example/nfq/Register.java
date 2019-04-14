package com.example.nfq;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

import java.lang.ref.WeakReference;

public class Register extends AppCompatActivity {

    private EditText mAccount;
    private EditText mPwd;
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;
     //用户数据
    private NFQDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAccount = (EditText) findViewById(R.id.username_input);
        mPwd = (EditText) findViewById(R.id.password1);
        mPwdCheck = (EditText) findViewById(R.id.password2);
        mSureButton = (Button) findViewById(R.id.confirm_btn);
        mCancelButton = (Button) findViewById(R.id.cancel_btn);
        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);

        myDB = NFQDatabase.getInstance(Register.this);

    }
    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.confirm_btn:                       //确认按钮的监听事件
                    register_check();
                    break;
                case R.id.cancel_btn:                     //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换Register Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }
    };


    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();
            //检查用户是否存在
            int count=myDB.getUserDao().getByUsername(userName).size();
            //用户已经存在时返回，给出提示文字
            if(count>0){
                Toast.makeText(getApplicationContext(),"User already exist",Toast.LENGTH_SHORT).show();
                return ;
            }
            if(userPwd.equals(userPwdCheck)==false){     //两次密码输入不一样
                Toast.makeText(getApplicationContext(),"Password not the same" ,Toast.LENGTH_SHORT).show();
                return ;
            } else {
                User user = new User(userName, userPwd);
                new InsertUser(Register.this, user).execute();
                //新建用户信息
                }
            }
        }
    private static class InsertUser extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<Register> activityReference;
        private User user;
        // only retain a weak reference to the activity
        InsertUser(Register context, User user) {
            activityReference = new WeakReference<>(context);
            this.user = user;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().myDB.getUserDao().insertAll(user);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            activityReference.get().setResult(bool);
        }
    }

    private void setResult(Boolean registerSuccess) {
        if(registerSuccess){
            Toast.makeText( this,"Register success",Toast.LENGTH_SHORT).show();
            Intent intent_Register_to_Login = new Intent( Register.this,Login.class) ;    //切换User Activity至Login Activity
            startActivity(intent_Register_to_Login);
            finish();
        }else{
            Toast.makeText(this,"Register fail",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),"User empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),"Password empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),"Password check empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



}
