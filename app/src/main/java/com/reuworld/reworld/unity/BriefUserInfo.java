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
    //性别，女0，男1
    int sex;
    //用户昵称
    String username;
    //用户头像
    String headPortrait;
    //个人说明
    String selfIntro;

    public BriefUserInfo(int id, int level,int sex,String username,String headPortrait,String selfIntro){
        this.id=id;
        this.level=level;
        this.sex=sex;
        this.username=username;
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

    public int getSex(){
        return sex;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSex(int sex){
        this.sex=sex;
    }

}
