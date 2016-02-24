package com.reuworld.reworld.unity;

/**
 * Created by Arvin.X on 16/2/6.
 * Data Unity V1.0
 */
public class CompUserInfo extends BriefUserInfo {

    //综合评分
    float score;
    //已获取赏金
    int getMoney;
    //已赔偿违约金
    int loseMoney;
    //已完成任务数
    int completedTaskNum;
    //失败任务数
    int failedTaskNum;
    //完成任务列表
    int[] completedTaskList;
    //失败任务列表
    int[] failedTaskList;


    public CompUserInfo(int id,int level, String userName,String headPortrait,String selfIntro,float score,int getMoney,
                        int loseMoney,int completedTaskNum,int failedTaskNum,int[] completedTaskList,int[] failedTaskList){
         super(id,level,userName,headPortrait,selfIntro);
        this.score=score;
        this.getMoney=getMoney;
        this.loseMoney=loseMoney;
        this.completedTaskNum=completedTaskNum;
        this.failedTaskNum=failedTaskNum;
        this.completedTaskList=completedTaskList;
        this.failedTaskList=failedTaskList;
    }

    public float getScore() {
        return score;
    }

    public int getCompletedTaskNum() {
        return completedTaskNum;
    }

    public int getFailedTaskNum() {
        return failedTaskNum;
    }

    public int getGetMoney() {
        return getMoney;
    }

    public int getLoseMoney() {
        return loseMoney;
    }

    public int[] getCompletedTaskList() {
        return completedTaskList;
    }

    public int[] getFailedTaskList() {
        return failedTaskList;
    }

    public void setCompletedTaskList(int[] completedTaskList) {
        this.completedTaskList = completedTaskList;
    }

    public void setCompletedTaskNum(int completedTaskNum) {
        this.completedTaskNum = completedTaskNum;
    }

    public void setFailedTaskList(int[] failedTaskList) {
        this.failedTaskList = failedTaskList;
    }

    public void setFailedTaskNum(int failedTaskNum) {
        this.failedTaskNum = failedTaskNum;
    }

    public void setGetMoney(int getMoney) {
        this.getMoney = getMoney;
    }

    public void setLoseMoney(int loseMoney) {
        this.loseMoney = loseMoney;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
