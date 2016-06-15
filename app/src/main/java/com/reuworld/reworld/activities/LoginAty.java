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
import com.reuworld.reworld.tools.SPHelper;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAty extends Activity {

    private Button registerBtn;
    private Button loginBtn;
    private EditText usernameEdit;
    private EditText  passwdEdit;
    private final String TAG="--LoginAty--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerBtn=(Button)findViewById(R.id.registerBtn);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        usernameEdit=(EditText)findViewById(R.id.usernameEdit);
        passwdEdit=(EditText)findViewById(R.id.passwdEdit);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username=usernameEdit.getText().toString();
                final String passwd=passwdEdit.getText().toString();
                if(!username.equals("")&!passwd.equals("")){
                    Call<Integer> call=DataClient.service.login(DataClient.ACTION_LOGIN,username,passwd);
                    Log.i(TAG,username+' '+passwd);
                    call.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.i(TAG,"onresponse: info: "+response.code()+response.headers()+'\n'+response.body());
                            if(response.isSuccessful()) {
                                int statecode = response.body();
                                if (statecode == DataClient.SUCCESS) {
                                    //success
                                    final Intent intent = new Intent(LoginAty.this, MainAty.class);
                                    //get userId to use
                                    Call<Map<String,Object>> innerCall=DataClient.service.getId(DataClient.ACTION_GETID,username);
                                    innerCall.enqueue(new Callback<Map<String, Object>>() {
                                        @Override
                                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                            Log.i(TAG, "inner onresponse info: " + response.code() + response.headers() + response.body());
                                            if (response.isSuccessful()) {
                                                if (((Double)response.body().get("statecode")).intValue() == 0) {
                                                    int userId=Integer.parseInt((String) response.body().get("content"));
                                                    intent.putExtra("userId",userId);
                                                    Log.i(TAG, "userId :" + userId);
                                                    SPHelper.saveUserInfo(username, passwd,userId,LoginAty.this);
                                                    startActivity(intent);
                                                    LoginAty.this.finish();
                                                } else {
                                                    Toast.makeText(LoginAty.this, "未知错误！", Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(LoginAty.this, "未知错误！", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                            Log.i(TAG, "inner onFailure: info: " + t.toString());
                                            t.printStackTrace();
                                            Toast.makeText(LoginAty.this, "未知错误！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    //end getUserId
                                } else if (statecode == DataClient.NO_SUCH_USER) {
                                    Toast.makeText(LoginAty.this, "用户不存在!", Toast.LENGTH_SHORT).show();
                                } else if (statecode==DataClient.PASSWD_ERROR){
                                    Toast.makeText(LoginAty.this, "密码错误!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(LoginAty.this,"未知错误!",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginAty.this,"登录失败！",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(LoginAty.this,"连接失败！",Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onFailure: exception: " + t.toString());
                            t.printStackTrace();
                        }
                    });
                }
                else {
                    Toast.makeText(LoginAty.this,"用户名或密码不能为空！",Toast.LENGTH_SHORT).show();
                }

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginAty.this, RegisterAty.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });



    }
}
