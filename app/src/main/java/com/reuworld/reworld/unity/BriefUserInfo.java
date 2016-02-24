package com.reuworld.reworld.unity;

/**
 * Created by Arvin.X on 16/2/6.
 * Data Unity V1.0
 */

//简化版用户信息数据类
public class BriefUserInfo {
    //用户ID
    int id;
    //用户等级
    int level;
    //用户昵称
    String userName;
    //用户头像
    String headPortrait;
    //个人说明
    String selfIntro;

    public BriefUserInfo(int id, int level,String userName,String headPortrait,String selfIntro){
        this.id=id;
        this.level=level;
        this.userName=userName;
        this.headPortrait=headPortrait;
        this.selfIntro=selfIntro;
    }

    //获取完整用户信息
//    public CompUserInfo getCompInfo(){
//        //TODO
//
//    }


    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public String getUserName() {
        return userName;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
