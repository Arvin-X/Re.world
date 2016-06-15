package com.reuworld.reworld.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arvin.X on 16/5/2.
 * Local SharedPreferences Helper
 */
public class SPHelper {
    private final static String TAG="--SPHelper--";

    /**
     * save userInfo with sharedPreferences
     * @param username String
     * @param passwd String,encrypted with HMAC
     * @param context Activity
     * @return success return 0, fail return 1
     */
    public static int saveUserInfo(String username,String passwd,int userId,Context context){
        try {
            SharedPreferences userInfo = context.getSharedPreferences("userInfo", 0);
            SharedPreferences.Editor editor = userInfo.edit();
            editor.putString("username", username);
            editor.putString("passwd", passwd);
            editor.putInt("userId",userId);
        }
        catch (Exception e){
            Log.i(TAG,"saveUserInfo error:"+e.toString());
            return 1;
        }
        return 0;
    }

    /**
     * get userInfo in SharedPreferences
     * @param context Activity
     * @return return List, NULL if fail
     */
    public static List<String> getUserInfo(Context context){
        List<String> result=new ArrayList<>();
        try {
            SharedPreferences userInfo=context.getSharedPreferences("userInfo",0);
            result.add(userInfo.getString("username",""));
            result.add(userInfo.getString("passwd",""));
            result.add(userInfo.getInt("userId",0)+"");
            return result;
        }
        catch (Exception e){
            return result;
        }

    }

    public static boolean hasUserInfo(Context context){
        SharedPreferences userInfo=context.getSharedPreferences("userInfo",0);
        if(userInfo.getString("username","").equals("")){
            return false;
        }else{
            return true;
        }
    }
}
