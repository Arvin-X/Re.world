package com.reuworld.reworld.unity;

/**
 * Created by Arvin.X on 16/2/24.
 * Data Unity V1.0
 */
public class CompUserSelfInfo extends CompUserInfo {
    //邮箱
    String email;
    //手机号
    String phoneNumber;
    //加密过的密码
    String passwd;

    public CompUserSelfInfo(int id,int level, String userName,String headPortrait,String selfIntro,float score,int getMoney,
                        int loseMoney,int completedTaskNum,int failedTaskNum,int[] completedTaskList,int[] failedTaskList,
                            String email,String phoneNumber,String passwd){
        super(id,level,userName,headPortrait,selfIntro,score,getMoney,loseMoney,completedTaskNum,failedTaskNum,completedTaskList,failedTaskList);
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.passwd=passwd;
    }

}
