package com.reuworld.reworld.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reuworld.reworld.R;
import com.reuworld.reworld.tools.DataClient;
import com.reuworld.reworld.tools.EncryptionTool;
import com.reuworld.reworld.unity.CompUserInfo;
import com.reuworld.reworld.unity.CompUserSelfInfo;

import java.util.Map;

import javax.microedition.khronos.egl.EGLDisplay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSelfInfoAty extends Activity {

    private EditText usernameEdit;

    private RadioButton male;
    private RadioButton female;
    private RadioGroup sexRadioGroup;
    private EditText selfIntroEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private Button compBtn;
    private final String TAG="--EditSelfInfoAty--";
    private int sex;
    private ImageView headImg;
    private int userId;
    private Gson gson;

    private int RESULT_CODE=9;

    private CompUserSelfInfo compUserSelfInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_self_info);
        userId=getIntent().getIntExtra("userId",0);

        gson=new Gson();
        usernameEdit=(EditText)findViewById(R.id.usernameEdit);
        selfIntroEdit=(EditText)findViewById(R.id.selfIntroEdit);
        emailEdit=(EditText)findViewById(R.id.emailEdit);
        phoneEdit=(EditText)findViewById(R.id.phoneEdit);
        compBtn=(Button)findViewById(R.id.compBtn);
        male=(RadioButton)findViewById(R.id.male);
        female=(RadioButton)findViewById(R.id.female);
        sexRadioGroup=(RadioGroup)findViewById(R.id.sexRadioGroup);
        headImg=(ImageView)findViewById(R.id.headPortrait);

        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog
                new AlertDialog.Builder(EditSelfInfoAty.this).setTitle("选择头像").setMessage("从相册选择").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_CODE);
                    }
                }).setNegativeButton("取消", null).show();

            }
        });

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

        Call<Map<String,Object>> call= DataClient.service.getCompUserSelfInfo(DataClient.ACTION_GET_COMP_USERSELFINFO,userId);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.i(TAG, "getCompUserSelfInfo: onResponse: " + response.code() + response.headers() + '\n' + response.body());
                int statecode = ((Double) response.body().get("statecode")).intValue();
                if (statecode == 0) {
                    compUserSelfInfo = gson.fromJson(response.body().get("content").toString(), CompUserSelfInfo.class);
                    Log.i(TAG,"username: "+compUserSelfInfo.getUsername());
                    usernameEdit.setText(compUserSelfInfo.getUsername());
                    if(compUserSelfInfo.getSex()==0){
                        female.toggle();
                    }else {
                        male.toggle();
                    }
                    selfIntroEdit.setText(compUserSelfInfo.getSelfIntro());
                    emailEdit.setText(compUserSelfInfo.getEmail());
                    phoneEdit.setText(compUserSelfInfo.getPhoneNumber());
                    headImg.setImageBitmap(EncryptionTool.str2Img(compUserSelfInfo.getHeadPortrait()));

                } else {
                    Toast.makeText(EditSelfInfoAty.this, "加载失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.i(TAG, "getCompUserSelfInfo: onFailure: " + t.toString());
                t.printStackTrace();
                Toast.makeText(EditSelfInfoAty.this, "连接失败！", Toast.LENGTH_SHORT).show();
            }
        });

        compBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameEdit.getText().toString();
                String selfIntro=selfIntroEdit.getText().toString();
                String email=emailEdit.getText().toString();
                String phone=phoneEdit.getText().toString();
                compUserSelfInfo.setUsername(username);
                compUserSelfInfo.setSex(sex);
                compUserSelfInfo.setSelfIntro(selfIntro);
                compUserSelfInfo.setEmail(email);
                compUserSelfInfo.setPhoneNumber(phone);

                Call<Integer> callComp=DataClient.service.updateSelfInfo(DataClient.ACTION_UPDATE_USERINFO,compUserSelfInfo);
                callComp.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.i(TAG,"updateSelfInfo: onResponse: "+response.code()+response.headers()+'\n'+response.body());
                        int statecode=response.body();
                        if(statecode==0){
                            Toast.makeText(EditSelfInfoAty.this,"修改成功!",Toast.LENGTH_SHORT).show();
                            EditSelfInfoAty.this.finish();
                        }else {
                            Toast.makeText(EditSelfInfoAty.this,"修改失败!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(EditSelfInfoAty.this,"修改失败!",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "updateSelfInfo: exception: " + t.toString());
                        t.printStackTrace();
                    }
                });
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image
            try {
                final Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                Call<Integer> call=DataClient.service.changeAvatar(DataClient.ACTION_CHANGE_AVATAR,userId, EncryptionTool.img2Str(bitmap));

                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.i(TAG,"onActivityResult: onResponse: "+response.code()+response.headers()+"\n"+response.body());
                        int statecode=(Integer)response.body();
                        if(statecode==0){
                            headImg.setImageBitmap(bitmap);
                        }else {
                            Toast.makeText(EditSelfInfoAty.this,"失败！",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(EditSelfInfoAty.this,"失败！",Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"onActivityResult: onFailure: "+t.toString());
                        t.printStackTrace();
                    }
                });

            }catch (Exception e){
                Toast.makeText(EditSelfInfoAty.this,"失败!",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_self_info_aty, menu);
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
