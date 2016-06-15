package com.reuworld.reworld.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reuworld.reworld.R;
import com.reuworld.reworld.activities.ViewSelfTaskListAty;
import com.reuworld.reworld.activities.ViewUserInfoAty;
import com.reuworld.reworld.tools.DataClient;
import com.reuworld.reworld.tools.EncryptionTool;
import com.reuworld.reworld.unity.CompUserInfo;
import com.reuworld.reworld.unity.CompUserSelfInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFrg.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFrg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFrg extends Fragment {
    private int userId;
    private String TAG="--settingsFrg--";
    private Gson gson=new Gson();
    private Button runningTaskListbtn;
    private CompUserSelfInfo compUserSelfInfo;

    private Button selfInfoBtn;
    private ImageView imgHead;
    public SettingsFrg() {
        // Required empty public constructor
    }

    public static SettingsFrg newInstance(int userId) {
        SettingsFrg fragment = new SettingsFrg();
        Bundle args = new Bundle();
        args.putInt("userId",userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView=inflater.inflate(R.layout.fragment_settings, container, false);
        selfInfoBtn=(Button)contentView.findViewById(R.id.selfInfoBtn);
        imgHead=(ImageView)contentView.findViewById(R.id.imgHead);
        runningTaskListbtn=(Button)contentView.findViewById(R.id.runningTaskListBtn);
        if(getArguments()!=null){
            userId=getArguments().getInt("userId");
        }

        selfInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsFrg.this.getActivity(), ViewUserInfoAty.class);
                intent.putExtra("isSelf", 1);
                intent.putExtra("userId", userId);
                startActivity(intent);
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
                    imgHead.setImageBitmap(EncryptionTool.str2Img(compUserSelfInfo.getHeadPortrait()));

                    if(compUserSelfInfo.getRunningTaskList().length!=0) {
                        runningTaskListbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SettingsFrg.this.getActivity(), ViewSelfTaskListAty.class);
                                intent.putExtra("listType", 0);
                                intent.putExtra("listContent", compUserSelfInfo.getRunningTaskList());
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    Toast.makeText(SettingsFrg.this.getActivity(), "加载失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.i(TAG, "getCompUserSelfInfo: onFailure: " + t.toString());
                t.printStackTrace();
                Toast.makeText(SettingsFrg.this.getActivity(), "连接失败！", Toast.LENGTH_SHORT).show();

            }
        });


        // Inflate the layout for this fragment
        return contentView;
    }


}
