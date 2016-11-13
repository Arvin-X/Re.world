package com.reuworld.reworld.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.reuworld.reworld.R;
import com.reuworld.reworld.tools.DataClient;
import com.reuworld.reworld.tools.SPHelper;
import com.reuworld.reworld.unity.CompTaskInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//编辑人物的时候按钮动态更改为“更新任务信息”
public class PublishTaskScdAty extends Activity {

    private Button sub1,sub2,sub3,sub4;
    private Button add1,add2,add3,add4;
    private EditText bountyEdit;
    private EditText depositEdit;
    private EditText waitTimeEdit;
    private EditText endTimeEdit;
    private Button publishBtn;
    private final String TAG="--PublishTaskScdAty--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_scd);

        final String taskTitle=getIntent().getStringExtra("taskTitle");
        final String taskDescription=getIntent().getStringExtra("taskDescription");
        bountyEdit=(EditText)findViewById(R.id.bountyEdit);
        depositEdit=(EditText)findViewById(R.id.depositEdit);
        waitTimeEdit=(EditText)findViewById(R.id.waitTimeEdit);
        endTimeEdit=(EditText)findViewById(R.id.endTimeEdit);
        publishBtn=(Button)findViewById(R.id.publishBtn);
        sub1=(Button)findViewById(R.id.sub1);
        sub2=(Button)findViewById(R.id.sub2);
        sub3=(Button)findViewById(R.id.sub3);
        sub4=(Button)findViewById(R.id.sub4);
        add1=(Button)findViewById(R.id.add1);
        add2=(Button)findViewById(R.id.add2);
        add3=(Button)findViewById(R.id.add3);
        add4=(Button)findViewById(R.id.add4);

        sub1.setOnClickListener(new mOnClickListener(bountyEdit,0));
        sub2.setOnClickListener(new mOnClickListener(depositEdit,0));
        sub3.setOnClickListener(new mOnClickListener(waitTimeEdit,0));
        sub4.setOnClickListener(new mOnClickListener(endTimeEdit,0));

        add1.setOnClickListener(new mOnClickListener(bountyEdit,1));
        add2.setOnClickListener(new mOnClickListener(depositEdit,1));
        add3.setOnClickListener(new mOnClickListener(waitTimeEdit,1));
        add4.setOnClickListener(new mOnClickListener(endTimeEdit,1));

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bounty=Integer.parseInt(bountyEdit.getText().toString());
                int deposit=Integer.parseInt(depositEdit.getText().toString());
                //TODO:int to String,format question
                int waitTimeInt=Integer.parseInt(waitTimeEdit.getText().toString());
                int endTimeInt=Integer.parseInt(endTimeEdit.getText().toString());


                //deal with time format,like 9999-12-31 23:59:59

                SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar now=Calendar.getInstance();
                now.add(Calendar.HOUR,waitTimeInt);
                String waitTime=sDateFormat.format(now.getTime());
                now=Calendar.getInstance();
                now.add(Calendar.HOUR,endTimeInt);
                String endTime=sDateFormat.format(now.getTime());



                int userId=getIntent().getIntExtra("userId",0);
                Log.i(TAG, "getIntent userId: " + getIntent().getIntExtra("userId", 0));

                Call<Integer> call= DataClient.service.publishTask(DataClient.ACTION_PUBLISH_TASK,
                        new CompTaskInfo(taskTitle,taskDescription,bounty,deposit,waitTime,endTime,userId));

                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.i(TAG, "call.onResponse: response info: " + response.code()+response.headers()+'\n'+response.body());
                        if(response.isSuccessful()) {
                            if (response.body() == 0) {


                                new AlertDialog.Builder(PublishTaskScdAty.this).setTitle("等待支付").setMessage("等待支付完成...").setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(PublishTaskScdAty.this, "发布成功!", Toast.LENGTH_SHORT).show();
                                        PublishTaskScdAty.this.finish();
                                    }
                                }).show();

                            } else {
                                Toast.makeText(PublishTaskScdAty.this, "发布失败!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(PublishTaskScdAty.this,"发布失败！",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(PublishTaskScdAty.this,"发布失败!",Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"call.onFailure: exception: "+t.toString());
                        t.printStackTrace();
                    }
                });
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_task_scd_aty, menu);
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

    private class mOnClickListener implements View.OnClickListener{

        EditText editText;
        int isAdd;

        public mOnClickListener(EditText editText,int isAdd){
            this.editText=editText;
            this.isAdd=isAdd;
        }

        @Override
        public void onClick(View v){
            try{
                if(Integer.parseInt(editText.getText().toString())==0){
                    return;
                }
            }catch (Exception e){
                return;
            }


            if(isAdd==1) {

                editText.setText(String.valueOf(Integer.parseInt(editText.getText().toString())+1));
            }else {
                int value=Integer.parseInt(editText.getText().toString());
                if(value>0) {
                    editText.setText(String.valueOf(value-1));
                }
            }

        }

    }

}
