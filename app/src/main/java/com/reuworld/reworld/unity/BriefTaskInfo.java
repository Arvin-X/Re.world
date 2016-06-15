package com.reuworld.reworld.unity;

/**
 * Created by Arvin.X on 16/2/6.
 * Data Unity V1.0
 */
public class BriefTaskInfo {
    //UserID
    int userId;
    //任务标题
    String taskTitle;
    //任务描述
    String taskDescription;
    //任务ID
    int taskID;
    //任务发布者
    String promulgatorName;
    //任务赏金
    int bounty;
    //任务状态(0为已完成，否则根据isComfirmNeed被判定为接取人数＋1（等待接取列表人数＋1），即1意味着0人接取)
    int state;
    //任务发布时间
    String promulgateTime;
    //任务标签
    String labels;
    //任务等待时间（超时则任务关闭，否则手动关闭（为空））
    String waitTime;


    public BriefTaskInfo(int userId,String taskTitle,String taskDescription,int taskID,String promulgatorName,int bounty,int state,String promulgateTime,String labels,String waitTime){
        this.userId=userId;
        this.taskTitle=taskTitle;
        this.taskDescription=taskDescription;
        this.taskID=taskID;
        this.promulgateTime=promulgateTime;
        this.promulgatorName=promulgatorName;
        this.bounty=bounty;
        this.state=state;
        this.labels=labels;
        this.waitTime=waitTime;
    }

//    public CompTaskInfo getCompInfo(){
//        //TODO
//    }

    public int getBounty() {
        return bounty;
    }

    public int getState() {
        return state;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getLabels() {
        return labels;
    }

    public String getPromulgateTime() {
        return promulgateTime;
    }

    public String getPromulgatorName() {
        return promulgatorName;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId=userId;
    }

    public String getTaskDescription(){
        return taskDescription;
    }

    public void setBounty(int bounty) {
        this.bounty = bounty;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public void setTaskDescription(String taskDescription){
        this.taskDescription=taskDescription;
    }
}
