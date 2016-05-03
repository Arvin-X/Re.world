package com.reuworld.reworld.tools;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.reuworld.reworld.unity.BriefTaskInfo;
import com.reuworld.reworld.unity.CompTaskInfo;
import com.reuworld.reworld.unity.CompUserInfo;
import com.reuworld.reworld.unity.CompUserSelfInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arvin.X on 16/4/1.
 * data interaction with server
 */
public class DataClient {
    private static HttpURLConnection conn;
    private static URL url;
    private static OutputStream out;
    private static InputStream in;

    private final static String dstUrl="http://reuworld.com/index.php";
    private final static String TAG="--DataClient--:";


    //state code
    public final static int SUCCESS=0;
    public final static int HTTP_ERROR=-1;
    public final static int EMAIL_DUPLICATE=1;
    public final static int EMAIL_ERROR=2;
    public final static int PHONE_NUM_DUPLICATE=3;
    public final static int PHONE_NUM_ERROR=4;
    public final static int USERNAME_DUPLICATE=5;
    public final static int NO_SUCH_USER=6;
    public final static int NO_SUCH_TASK=7;
    public final static int TASK_HAS_BEEN_GOT=8;
    public final static int UNKNOWN_ERROR=99;

    //action flag
    private final static String ACTION_FLAG="actionFlag";
    private final static int ACTION_GET_COMP_USERSELFINFO=1;
    private final static int ACTION_GET_COMP_USERINFO=2;
    private final static int ACTION_GET_COMP_TASKINFO=3;
    private final static int ACTION_GET_BRIEF_TASKLIST=4;
    private final static int ACTION_ADD_USER=5;
    private final static int ACTION_LOGIN=6;
    private final static int ACTION_UPDATE_USERINFO=7;
    private final static int ACTION_PUBLISH_TASK=8;
    private final static int ACTION_UPDATE_TASK=9;
    private final static int ACTION_GET_THETASK=10;
    private final static int ACTION_COMMIT_TASK=11;
    private final static int ACTION_COMMENT_TASK=12;


    //JSON TAG
    private final static String TAG_JSON_USERINFO="userInfo";
    private final static String TAG_JSON_TASKINFO="taskInfo";
    private final static String TAG_JSON_USERID="userId";
    private final static String TAG_JSON_TASKID="taskId";
    private final static String TAG_JSON_LISTTYPE="listType";
    private final static String TAG_JSON_COMMENT="comment";

    //task list type code
    public final static int LISTTYPE_RECOMMEND=1;
    public final static int LISTTYPE_ALL=2;
    public final static int LISTTYPE_HIGHREWORD=3;


    //init
    static {
        try {
            url=new URL(dstUrl);
        }
        catch (Exception e){
            Log.i(TAG,"init: exception:"+e.toString());
        }
    }

    /**
     * register check
     * @param compUserSelfInfo user info need to register
     * @return return state code
     */
    public static int AddUser(CompUserSelfInfo compUserSelfInfo){
        return getHttpResultCode(ACTION_ADD_USER,TAG_JSON_USERINFO,JSON.toJSONString(compUserSelfInfo));
    }

    /**
     * login check
     * @param username username
     * @param passwd passwd which has been encrypted
     * @return return state code
     */
    public static int Login(String username,String passwd){
        Map<String,String> map=new HashMap<String,String>();
        map.put("username",username);
        map.put("passwd",passwd);
        return getHttpResultCode(ACTION_LOGIN,TAG_JSON_USERINFO,JSON.toJSONString(map));
    }

    /**
     * update self info
     * @param compUserSelfInfo info wait to update
     * @return return state code
     */
    public static int updateSelfInfo(CompUserSelfInfo compUserSelfInfo){
        return getHttpResultCode(ACTION_UPDATE_USERINFO,TAG_JSON_USERINFO,JSON.toJSONString(compUserSelfInfo));
    }


    /**
     * get CompUserSelfInfo with userId
     * @param userId self ID
     * @return A map with "statecode" and "content" which can be forced into Class CompUserSelfInfo
     */
    public static Map<String,Object> getCompUserSelfInfo(int userId){
        return getHttpResultMap(ACTION_GET_COMP_USERSELFINFO, CompUserSelfInfo.class, TAG_JSON_USERID, String.valueOf(userId));
    }

    /**
     * get CompUserInfo info with userId
     * @param userId userId
     * @return A map with "statecode" and "content" which can be forced into Class CompUserInfo
     */
    public static Map<String,Object> getCompUserInfo(int userId){
        return getHttpResultMap(ACTION_GET_COMP_USERINFO, CompUserInfo.class, TAG_JSON_USERID, String.valueOf(userId));
    }

    /**
     * get compelte task info with taskId
     * @param taskId taskId
     * @return A map with "statecode" and "content" which can be forced into Class CompTaskInfo
     */
    public static Map<String,Object> getCompTaskInfo(int taskId){
        return getHttpResultMap(ACTION_GET_COMP_TASKINFO, CompTaskInfo.class, TAG_JSON_TASKID, String.valueOf(taskId));

    }


    /**
     * get list of task with type specified
     * @param listType list type number
     * @return A map with "statecode" and "content" which can be forced into Class List<BriefTaskInfo>
     */
    public static Map<String,Object> getBriefTaskList(int listType){
        return getHttpResultMapWithList(ACTION_GET_BRIEF_TASKLIST, BriefTaskInfo.class, TAG_JSON_LISTTYPE, String.valueOf(listType));
    }

    /**
     * publish the task
     * @param taskWaitToPublish CompTaskInfo
     * @return state code
     */
    public static int publishTask(CompTaskInfo taskWaitToPublish){
        return getHttpResultCode(ACTION_PUBLISH_TASK, TAG_JSON_TASKINFO, JSON.toJSONString(taskWaitToPublish));
    }

    /**
     * update task info
     * @param taskWaitToUpdate CompTaskInfo
     * @return state code
     */
    public static int updateTaskInfo(CompTaskInfo taskWaitToUpdate){
        return getHttpResultCode(ACTION_UPDATE_TASK, TAG_JSON_TASKINFO, JSON.toJSONString(taskWaitToUpdate));
    }

    /**
     * get task
     * @param taskId task which be got
     * @param userId user who try to get the task
     * @return state code
     */
    public static int getTask(int taskId,int userId){
        return getHttpResultCode(ACTION_GET_THETASK, TAG_JSON_TASKID, String.valueOf(taskId), TAG_JSON_USERID, String.valueOf(userId));
    }

    /**
     * commit task
     * @param taskId task which be finished
     * @param userId the user who finished the task
     * @return state  code
     */
    public static int commitTask(int taskId,int userId){
        return getHttpResultCode(ACTION_COMMIT_TASK, TAG_JSON_TASKID, String.valueOf(taskId), TAG_JSON_USERID, String.valueOf(userId));
    }

    /**
     * comment task
     * @param taskId destination task ID
     * @param userId the user that the comments comment on
     * @param comment comment
     * @return state code
     */
    public static int commentTask(int taskId,int userId,String comment){
        return getHttpResultCode(ACTION_COMMENT_TASK, TAG_JSON_TASKID, String.valueOf(taskId), TAG_JSON_USERID, String.valueOf(userId), TAG_JSON_COMMENT, comment);
    }

    /**
     * get http result code from method getHttpResultString
     * @param actionFlag int
     * @param dataArgs which number must be even, contain Key-Value pair like TAG+content
     * @return state code
     */
    private static int getHttpResultCode(int actionFlag,String...dataArgs){
        String result=getHttpResultString(actionFlag,dataArgs);
        if(result==null){
            return HTTP_ERROR;
        }
        else {
            try {
                return Integer.parseInt(result);
            }
            catch (NumberFormatException e){
                Log.i(TAG,"AddUser: result is not number exception:"+e.toString());
                return UNKNOWN_ERROR;
            }
        }
    }

    /**
     * get http result map packed from the method getHttpResultString
     * @param actionFlag int
     * @param classData Class which you want to unpacked into
     * @param dataArgs which number must be even, contain Key-Value pair like TAG+content
     * @return A map with "statecode" and "content" which can be forced into Class the same as classData
     */
    private static Map<String,Object> getHttpResultMap(int actionFlag,Class classData,String...dataArgs){
        String result=getHttpResultString(actionFlag, dataArgs);
        Map<String,Object> resultMap=new HashMap<String,Object>();
        resultMap.put("stateCode", JSON.parseObject(result).getString("stateCode"));
        resultMap.put("content", JSON.parseObject(JSON.parseObject(result).getString("content"),classData));
        return resultMap;
    }

    /**
     * get http result map packed from the method getHttpResultString
     * @param actionFlag int
     * @param classData Class which you want to unpacked into
     * @param dataArgs which number must be even, contain Key-Value pair like TAG+content
     * @return A map with "statecode" and "content" which can be forced into Class Array the same as classData
     */
    private static Map<String,Object> getHttpResultMapWithList(int actionFlag,Class classData,String...dataArgs){
        String result=getHttpResultString(actionFlag, dataArgs);
        Map<String,Object> resultMap=new HashMap<String,Object>();
        resultMap.put("stateCode", JSON.parseObject(result).getString("stateCode"));
        resultMap.put("content", JSON.parseArray(JSON.parseObject(result).getString("content"), classData));
        return resultMap;
    }

    /**
     * use httpConnect internal
     * @param actionFlag int
     * @param dataArgs which number must be even, contain Key-Value pair like TAG+content
     * @return String get from server
     */
    private static String getHttpResultString(int actionFlag,String...dataArgs){
        Map<String,String> map=new HashMap<>();
        for(int i=0;i<dataArgs.length;i+=2){
            map.put(dataArgs[i],dataArgs[i + 1]);
        }
        map.put(ACTION_FLAG,String.valueOf(actionFlag));
        return httpConnect(JSON.toJSONString(map));
    }


    /**
     * create an http connection and return the response data
     * @param data post data
     * @return return null if http connect error, else return string get from http connection
     */
    private static String httpConnect(String data) {
        String result=null;
        try{
            conn=(HttpURLConnection)url.openConnection();
            //use post method
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //output to sever
            out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            //get result
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                in = conn.getInputStream();
                result = getStringFromInputStream(in);
            } else {
                Log.i (TAG,"httpConnect: error with http result code: "+responseCode);
                return null;
            }

        } catch (Exception e) {
            Log.i(TAG,"httpConnect: exception: "+e.toString());
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        // 一定要写len=is.read(buffer)
        // 如果while((is.read(buffer))!=-1)则无法将数据写入buffer中
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }


}
