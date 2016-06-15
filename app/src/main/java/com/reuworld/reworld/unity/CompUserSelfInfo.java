package com.reuworld.reworld.unity;

/**
 * Created by Arvin.X on 16/2/24.
 * Data Unity V1.0
 */
public class CompUserSelfInfo extends CompUserInfo {
    //邮箱
    private String email;
    //手机号
    private String phoneNumber;
    //加密过的密码
    private String passwd;
    //正在进行的任务列表
    private int[] runningTaskList;

    public CompUserSelfInfo(int id,int level,int sex, String username,String headPortrait,String selfIntro,float score,int getMoney,
                        int loseMoney,int completedTaskNum,int failedTaskNum,int[] completedTaskList,int[] failedTaskList,
                            String email,String phoneNumber,String passwd){
        super(id,level,sex,username,headPortrait,selfIntro,score,getMoney,loseMoney,completedTaskNum,failedTaskNum,completedTaskList,failedTaskList);
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.passwd=passwd;
    }

    //used for new user
    public CompUserSelfInfo(String username,int sex,String passwd,String email){
        super(0,0,0,"","","",0,0,0,0,0,new int[]{},new int[]{});
        this.username=username;
        this.phoneNumber="";
        this.passwd=passwd;
        this.sex=sex;
        this.email=email;
    }

    public String getEmail(){
        return email;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setEmail(String email){
        this.email=email;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    /*
    @Override
    public String toString(){
        return this.email+this.phoneNumber+this.passwd+"\n";
    }
    **/

    public int[] getRunningTaskList(){
        return this.runningTaskList;
    }


}
