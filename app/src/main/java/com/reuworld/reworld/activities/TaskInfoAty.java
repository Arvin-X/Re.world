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
import com.reuworld.reworld.unity.CompTaskInfo;
import com.reuworld.reworld.unity.CompUserInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskInfoAty extends Activity {

    private LinearLayout mainLayout;
    private TextView usernameText;
    private TextView taskTitleText;
    private TextView taskDescriptionText;
    private ImageView imgHead;
    private Button editBtn;
    private Button getTaskBtn;
    private Gson gson;
    private final String TAG="--TaskInfoAty--";
    private TextView rewordText;
    private TextView depositText;
    private CompUserInfo compUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);
        usernameText=(TextView)findViewById(R.id.usernameText);
        taskTitleText=(TextView)findViewById(R.id.taskTitleText);
        taskDescriptionText=(TextView)findViewById(R.id.taskDescriptionText);
        imgHead=(ImageView)findViewById(R.id.headPortrait);
        editBtn=(Button)findViewById(R.id.editBtn);
        getTaskBtn=(Button)findViewById(R.id.getTaskBtn);
        rewordText=(TextView)findViewById(R.id.rewordText);
        depositText=(TextView)findViewById(R.id.depositText);


        gson=new Gson();

        final int taskId=getIntent().getIntExtra("taskId",0);
        final int userId=getIntent().getIntExtra("userId",0);
        Log.i(TAG, "taskId: " + taskId);

        /*
         * modify according to intent info
         */
//        if(getIntent().getIntExtra("editable",0)==0){
//            LinearLayout barLayout=(LinearLayout)findViewById(R.id.barLayout);
//            barLayout.removeView(editBtn);
            //if this task has been completed or over
//            if(getIntent().getIntExtra("available",0)==0){
//
//                LinearLayout layout=(LinearLayout)findViewById(R.id.layout);
//                layout.removeView(getTaskBtn);
//            }
//
//        }else {
//            //you can not get your self task
//            LinearLayout layout=(LinearLayout)findViewById(R.id.layout);
//            layout.removeView(getTaskBtn);
//        }
        /*
         * end modify
         */

//        getTaskBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Call<Integer> call=DataClient.service.getTask(DataClient.ACTION_GET_THETASK,taskId,userId);
//                call.enqueue(new Callback<Integer>() {
//                    @Override
//                    public void onResponse(Call<Integer> call, Response<Integer> response) {
//                        //TODO
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<Integer> call, Throwable t) {
//
//                    }
//                });
//            }
//        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskInfoAty.this, PublishTaskFstAty.class);
                intent.putExtra("edit", 1);
                startActivity(intent);
            }
        });


        if(taskId!=0){
            Call<Map<String,Object>> call= DataClient.service.getCompTaskInfo(DataClient.ACTION_GET_COMP_TASKINFO,taskId);
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    int statecode = ((Double)response.body().get("statecode")).intValue();
                    if (statecode == 0) {
                        final CompTaskInfo compTaskInfo = gson.fromJson(response.body().get("content").toString(), CompTaskInfo.class);
                        taskTitleText.setText(compTaskInfo.getTaskTitle());
                        taskDescriptionText.setText(compTaskInfo.getTaskDescription());
                        usernameText.setText(compTaskInfo.getPromulgatorName());
                        rewordText.setText(compTaskInfo.getBounty()+"");
                        depositText.setText(compTaskInfo.getDeposit()+"");


                        //now get User Info
                        Log.i(TAG,"getCompTaskInfo: taskUserId: "+compTaskInfo.getUserId());
                        Call<Map<String,Object>> callInner=DataClient.service.getCompUserInfo(DataClient.ACTION_GET_COMP_USERINFO,compTaskInfo.getUserId());
                        callInner.enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                Log.i(TAG,"getCompUserInfo: onResponse: "+response.code()+response.headers()+'\n'+response.body());
                                int statecode=((Double)response.body().get("statecode")).intValue();
                                if(statecode==0){
                                    compUserInfo=gson.fromJson(response.body().get("content").toString(), CompUserInfo.class);
                                    Log.i(TAG,compUserInfo.getHeadPortrait());
                                    imgHead.setImageBitmap(EncryptionTool.str2Img(compUserInfo.getHeadPortrait()));
                                }else {
                                    Toast.makeText(TaskInfoAty.this,"失败！",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                Log.i(TAG,"getCompUserInfo: onFailure: "+t.toString());
                                Toast.makeText(TaskInfoAty.this,"失败！",Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });



//                        if (compTaskInfo.getState() == 0) {
//                            //This task has been completed
//                            //set taskStateButton grey and can not be trigger to get the task
//                            setBtnStateToGrey();
//                            getTaskBtn.setText("任务已结束");
//                        } else {
//                            //whether mine
//                            //if user can edit this task
//                            if(compTaskInfo.getUserId()!=userId){
//                                //remove editable button
//                                LinearLayout barLayout=(LinearLayout)findViewById(R.id.barLayout);
//                                barLayout.removeView(editBtn);
//
//
//                                //it has got or not
//                                if(compTaskInfo.getMercenary().equals(compUserInfo.getUsername())){
//                                    getTaskBtn.setText("提交任务");
//                                    getTaskBtn.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Call<Integer> innerCall=DataClient.service.commitTask(DataClient.ACTION_COMMIT_TASK,taskId,userId);
//                                            innerCall.enqueue(new Callback<Integer>() {
//                                                @Override
//                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
//                                                    if (response.body() == 0) {
//                                                        Toast.makeText(TaskInfoAty.this, "提交成功!", Toast.LENGTH_SHORT).show();
//                                                        setBtnStateToGrey();
//                                                    } else {
//                                                        Toast.makeText(TaskInfoAty.this, "提交失败!", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<Integer> call, Throwable t) {
//
//                                                    Toast.makeText(TaskInfoAty.this, "提交失败!", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                        }
//                                    });
//
//                                }else {

                                    getTaskBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Call<Integer> innerCall = DataClient.service.getTask(DataClient.ACTION_GET_THETASK, taskId, userId);
                                            innerCall.enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    if (response.body() == 0) {
                                                        Toast.makeText(TaskInfoAty.this, "接取任务成功!", Toast.LENGTH_SHORT).show();
                                                        getTaskBtn.setText("提交任务");
                                                        getTaskBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Toast.makeText(TaskInfoAty.this, "提交成功!",Toast.LENGTH_SHORT).show();
                                                                getTaskBtn.setText("任务已结束");
                                                                setBtnStateToGrey();
                                                            }
                                                        });


                                                    } else {
                                                        Toast.makeText(TaskInfoAty.this, "接取任务失败!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable t) {
                                                    Toast.makeText(TaskInfoAty.this, "接取任务失败!", Toast.LENGTH_SHORT).show();
                                                    t.printStackTrace();
                                                    Log.i(TAG, "innerCall response: exception: " + t.toString());
                                                }
                                            });
                                        }
                                    });


//                                }



//                            }else {
//                                setBtnStateToGrey();
//                            }




                        }


//                    } else {
//                        Toast.makeText(TaskInfoAty.this, "任务加载失败!", Toast.LENGTH_SHORT).show();
//                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(TaskInfoAty.this, "任务加载失败!", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    Log.i(TAG, "call response: exception: " + t.toString());
                }
            });


        }else {
            //load failed and return
            Toast.makeText(TaskInfoAty.this,"加载任务信息失败!",Toast.LENGTH_SHORT).show();
            TaskInfoAty.this.finish();
        }


    }



    private void setBtnStateToGrey(){
        getTaskBtn.setBackgroundResource(R.color.bg_agray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_info_aty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
