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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reuworld.reworld.R;
import com.reuworld.reworld.tools.DataClient;
import com.reuworld.reworld.unity.CompUserSelfInfo;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterAty extends Activity {

    EditText usernameEdit;
    EditText passwdEdit;
    EditText confirmPasswdEdit;
    EditText emailEdit;
    Button registerBtn;
    RadioButton male;
    RadioButton female;
    RadioGroup sexRadioGroup;
    int sex=0;
    private final String TAG="--RegisterAty--";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEdit=(EditText)findViewById(R.id.usernameEdit);
        passwdEdit=(EditText)findViewById(R.id.passwdEdit);
        confirmPasswdEdit=(EditText)findViewById(R.id.confirmPasswdEdit);
        emailEdit=(EditText)findViewById(R.id.emailEdit);
        male=(RadioButton)findViewById(R.id.male);
        female=(RadioButton)findViewById(R.id.female);
        sexRadioGroup=(RadioGroup)findViewById(R.id.sexRadioGroup);
        registerBtn=(Button)findViewById(R.id.registerBtn);


        //listen on sex change
        sexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.male){
                    sex=1;
                }
                else{
                    sex=0;
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameEdit.getText().toString();
                String passwd=passwdEdit.getText().toString();
                String confirmPasswd=confirmPasswdEdit.getText().toString();
                String email=emailEdit.getText().toString();
                //int stetecode=DataClient.AddUser(new CompUserSelfInfo(username,sex,passwd,email));
                Call<Integer> call=DataClient.service.addUser(DataClient.ACTION_ADD_USER,new CompUserSelfInfo(username,sex,passwd,email));
                Gson gson=new Gson();
                Log.i(TAG,gson.toJson(new CompUserSelfInfo(username,sex,passwd,email)));
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.i(TAG,"onresponse: info: "+response.code()+response.headers()+'\n'+response.body());
                        if (response.isSuccessful()) {
                            if(response.body()==DataClient.SUCCESS) {
                                Toast.makeText(RegisterAty.this, "注册成功！", Toast.LENGTH_LONG).show();
                                //TODO
                                //return to login Aty
                                Intent intent = new Intent(RegisterAty.this, LoginAty.class);
                                startActivity(intent);
                                RegisterAty.this.finish();
                            }else if(response.body()==DataClient.USERNAME_DUPLICATE) {
                                Toast.makeText(RegisterAty.this, "用户名重复！", Toast.LENGTH_LONG).show();
                            }else if(response.body()==DataClient.EMAIL_DUPLICATE){
                                Toast.makeText(RegisterAty.this, "邮箱已被注册！", Toast.LENGTH_LONG).show();
                            }else if(response.body()==DataClient.PHONE_NUM_DUPLICATE) {
                                Toast.makeText(RegisterAty.this, "手机号已被注册！", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(RegisterAty.this, "未知错误!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(RegisterAty.this, "注册失败！", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(RegisterAty.this, "注册失败！", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onFailure: exception:" + t.toString() + "  call:" + call.toString());
                        t.printStackTrace();
                    }
                });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_aty, menu);
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
