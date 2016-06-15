package com.reuworld.reworld.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.reuworld.reworld.R;
import com.reuworld.reworld.tools.DataClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//编辑任务时要复用这个activity和xml，需要处理传递的数据
public class PublishTaskFstAty extends Activity {

    EditText taskTitleEdit;
    EditText taskDescriptionEdit;
    Button nextStepbtn;
    private final String TAG="--PublishTaskAty--";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_fst);
        taskTitleEdit=(EditText)findViewById(R.id.taskTitleEdit);
        taskDescriptionEdit=(EditText)findViewById(R.id.taskDescriptionEdit);
        nextStepbtn=(Button)findViewById(R.id.nextStepBtn);

        final int userId=getIntent().getIntExtra("userId",0);

        /*
         * modify according to intent info
         */
        if(getIntent().getIntExtra("edit",0)==1){
            int taskId=getIntent().getIntExtra("taskId",0);
            Call<Map<String,Object>> call= DataClient.service.getCompTaskInfo(DataClient.ACTION_GET_COMP_TASKINFO,taskId);
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                }
            });

        }
        /*
          modify end
         */

        nextStepbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle=taskTitleEdit.getText().toString();
                String taskDescription=taskDescriptionEdit.getText().toString();
                if(!taskTitle.equals("")&&!taskDescription.equals("")){
                    Intent intent=new Intent(PublishTaskFstAty.this,PublishTaskScdAty.class);
                    intent.putExtra("taskTitle",taskTitle);
                    intent.putExtra("taskDescription",taskDescription);
                    intent.putExtra("userId",userId);
                    Log.i(TAG, "getIntent userId: " + userId);
                    startActivity(intent);
                    PublishTaskFstAty.this.finish();
                }else {
                    Toast.makeText(PublishTaskFstAty.this,"标题或内容不能为空!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_task_aty, menu);
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
