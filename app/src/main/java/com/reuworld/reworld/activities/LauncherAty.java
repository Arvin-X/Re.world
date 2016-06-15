package com.reuworld.reworld.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.reuworld.reworld.R;
import com.reuworld.reworld.tools.DataClient;
import com.reuworld.reworld.tools.SPHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherAty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launcher);


        Timer timer=new Timer();

        if(SPHelper.hasUserInfo(LauncherAty.this)){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    List<String> list=SPHelper.getUserInfo(LauncherAty.this);
                    Call<Integer> call= DataClient.service.login(DataClient.ACTION_LOGIN,list.get(0),list.get(1));
                    call.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            int statecode = response.body();
                            if (statecode == 0) {
                                //success
                                Intent intent = new Intent(LauncherAty.this, MainAty.class);
                                startActivity(intent);
                                LauncherAty.this.finish();
                            }else {
                                Toast.makeText(LauncherAty.this,"自动登录失败！",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LauncherAty.this,LoginAty.class);
                                startActivity(intent);
                                LauncherAty.this.finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(LauncherAty.this,"自动登录失败！",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LauncherAty.this,LoginAty.class);
                            startActivity(intent);
                            LauncherAty.this.finish();
                        }
                    });

                }
            },1500);
        }else {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //if  there is not any user exist already
                    //2秒后跳转并自动销毁自身
                    Intent intent = new Intent(LauncherAty.this, LoginAty.class);
                    startActivity(intent);
                    LauncherAty.this.finish();
                }
            }, 1500);
        }
    }

}
