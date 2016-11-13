package com.reuworld.reworld.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reuworld.reworld.R;
import com.reuworld.reworld.tools.DataClient;
import com.reuworld.reworld.tools.EncryptionTool;
import com.reuworld.reworld.unity.CompUserInfo;
import com.reuworld.reworld.unity.CompUserSelfInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewUserInfoAty extends Activity {

    private TextView usernameText;
    private TextView sexText;
    private TextView selfIntroText;
    private TextView getBountyText;
    private TextView lostMoneyText;
    private TextView completedText;
    private TextView failedText;
    private final String TAG="--ViewUserInfoAty--";
    private CompUserInfo compUserInfo;
    private Gson gson=new Gson();
    private Button editBtn;
    private Button viewCompletedBtn;
    private Button viewFailedBtn;
    private ImageView headPortrait;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_info);
        usernameText=(TextView)findViewById(R.id.usernameText);
        sexText=(TextView)findViewById(R.id.sexText);
        selfIntroText=(TextView)findViewById(R.id.selfInfoText);
        getBountyText=(TextView)findViewById(R.id.getBountyText);
        lostMoneyText=(TextView)findViewById(R.id.loseMoneyText);
        completedText=(TextView)findViewById(R.id.completedText);
        failedText=(TextView)findViewById(R.id.failedText);
        editBtn=(Button)findViewById(R.id.editBtn);
        viewCompletedBtn=(Button)findViewById(R.id.viewCompletedBtn);
        viewFailedBtn=(Button)findViewById(R.id.viewFailedBtn);
        headPortrait=(ImageView)findViewById(R.id.headPortrait);

        final int userId=getIntent().getIntExtra("userId",0);

        /*
         * modify according to intent info
         */
        if(getIntent().getIntExtra("isSelf",0)==0){
            LinearLayout barLayout=(LinearLayout)findViewById(R.id.barLayout);
            barLayout.removeView(editBtn);
            barLayout.removeView(viewCompletedBtn);
            barLayout.removeView(viewFailedBtn);
        }

        /*
         * end modify
         */



        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewUserInfoAty.this, EditSelfInfoAty.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        Call<Map<String,Object>> call= DataClient.service.getCompUserInfo(DataClient.ACTION_GET_COMP_USERINFO,userId);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                Log.i(TAG, "getCompUserInfo: onResponse: " + response.code() + response.headers() + '\n' + response.body());
                int statecode = ((Double) response.body().get("statecode")).intValue();
                if (statecode == 0) {
                    compUserInfo = gson.fromJson(response.body().get("content").toString(), CompUserInfo.class);
                    usernameText.setText(compUserInfo.getUsername());
                    sexText.setText(compUserInfo.getSex() == 0 ? "女" : "男");
                    selfIntroText.setText(compUserInfo.getSelfIntro());
//                    Log.i(TAG,"getCompUserINfo: onResponse: "+compUserInfo.getGetMoney());
                    getBountyText.setText(compUserInfo.getGetMoney() + "");
                    lostMoneyText.setText(compUserInfo.getLoseMoney() + "");
                    completedText.setText(compUserInfo.getCompletedTaskNum() + "");
                    failedText.setText(compUserInfo.getFailedTaskNum() + "");
                    if(compUserInfo.getHeadPortrait()!=null)
                        headPortrait.setImageBitmap(EncryptionTool.str2Img(compUserInfo.getHeadPortrait()));


                } else {
                    Toast.makeText(ViewUserInfoAty.this, "加载失败！", Toast.LENGTH_SHORT).show();
                }

                //set button
                if(compUserInfo.getCompletedTaskNum()!=0) {
                    viewCompletedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ViewUserInfoAty.this, ViewSelfTaskListAty.class);
                            intent.putExtra("listContent", compUserInfo.getCompletedTaskList());
                            startActivity(intent);
                        }
                    });
                }else {

                    viewCompletedBtn.setBackgroundResource(R.color.bg_agray);
                }
                if(compUserInfo.getFailedTaskNum()!=0) {
                    viewFailedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ViewUserInfoAty.this, TaskInfoAty.class);
                            intent.putExtra("listContent", compUserInfo.getFailedTaskList());
                            startActivity(intent);
                        }
                    });
                }else {
                    viewFailedBtn.setBackgroundResource(R.color.bg_agray);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.i(TAG, "getCompUserSelfInfo: onFailure: " + t.toString());
                t.printStackTrace();
                Toast.makeText(ViewUserInfoAty.this, "连接失败！", Toast.LENGTH_SHORT).show();

            }
        });




    }
}