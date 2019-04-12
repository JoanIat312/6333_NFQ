package com.example.wongying.newnfq;

public class UserData {

    private String userName;
    private String userPwd;
    private int userId;

    public String getUserName(){
        return userName;
    }

    public  void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserPwd()
    {
        return userPwd;
    }

    public void setUserPwd(String userPwd){
        this.userPwd = userPwd;
    }

    public int getUserId() {
        return userId;
    }

    public UserData(String userName, String userPwd){
        super();
        this.userName = userName;
        this.userPwd = userPwd;
    }
}
