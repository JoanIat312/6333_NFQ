package com.example.nfq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮

    private SharedPreferences login_sp;
    private User user_in_session;

    private NFQDatabase myDB;
    //用户数据管理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //通过id找到相应的控件
        mAccount = (EditText) findViewById(R.id.username);
        mPwd = (EditText) findViewById(R.id.password);
        mRegisterButton = (Button) findViewById(R.id.register_btn);
        mLoginButton = (Button) findViewById(R.id.login_btn);

       /* mCancleButton = (Button) findViewById(R.id.login_btn_cancle);
        loginView=findViewById(R.id.login_view);
        loginSuccessView=findViewById(R.id.login_success_view);
        loginSuccessShow=(TextView) findViewById(R.id.login_success_show);

        mChangepwdText = (TextView) findViewById(R.id.login_text_change_pwd);

        mRememberCheck = (CheckBox) findViewById(R.id.Login_Remember); */

        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mAccount.setText(name);
            mPwd.setText(pwd);
            //mRememberCheck.setChecked(true);
        }

        mRegisterButton.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(mListener);
        /*mCancleButton.setOnClickListener(mListener);
        mChangepwdText.setOnClickListener(mListener);

        ImageView image = (ImageView) findViewById(R.id.logo);             //使用ImageView显示logo
        image.setImageResource(R.drawable.logo);*/

        myDB = NFQDatabase.getInstance(Login.this);
    }

    View.OnClickListener mListener = new View.OnClickListener() {                  //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn:                            //登录界面的注册按钮
                    Intent intent_Login_to_Register = new Intent(Login.this,Register.class);    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login_btn:                              //登录界面的登录按钮
                    login();
                    break;
                /*case R.id.login_btn_cancle:                             //登录界面的注销按钮
                    cancel();
                    break;
                case R.id.login_text_change_pwd:                             //登录界面的注销按钮
                    Intent intent_Login_to_reset = new Intent(Login.this,Resetpwd.class) ;    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_reset);
                    finish();
                    break;*/
            }
        }
    };

    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            SharedPreferences.Editor editor =login_sp.edit();
            user_in_session = myDB.getUserDao().getUser(userName, userPwd);
            if(user_in_session != null){
                //返回1说明用户名和密码均正确
                //保存用户名和密码
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);

                //是否记住密码
                /*if(mRememberCheck.isChecked()){
                    editor.putBoolean("mRememberCheck", true);
                }else{
                    editor.putBoolean("mRememberCheck", false);
                }
                editor.commit();*/

                Intent intent = new Intent(Login.this,HomePage.class).putExtra("current_user", user_in_session);    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(),"login success",Toast.LENGTH_SHORT).show();//登录成功提示
            }else{
                Toast.makeText(getApplicationContext(),"login fail",Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }
    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),"username empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),"Password empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }


}
