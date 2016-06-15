package com.reuworld.reworld.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.reuworld.reworld.R;
import com.reuworld.reworld.fragments.SettingsFrg;
import com.reuworld.reworld.fragments.TaskListFrg;
import com.reuworld.reworld.tools.DataClient;


//核心Activity
public class MainAty extends Activity {
    FragmentManager fm;
    FrameLayout contentFl;
    Button recommendBtn;
    Button allBtn;
    Button highRewordBtn;
    Button settingsBtn;
    Fragment recommendFrg;
    Fragment highRewordFrg;
    Fragment allFrg;
    Fragment settingsFrg;
    Button publishbtn;
    private final String TAG="--MainAty--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int userId=getIntent().getIntExtra("userId",0);
        contentFl=(FrameLayout)findViewById(R.id.contentFl);
        recommendBtn=(Button)findViewById(R.id.recommendBtn);
        allBtn=(Button)findViewById(R.id.allBtn);
        highRewordBtn=(Button)findViewById(R.id.highRewordBtn);
        settingsBtn=(Button)findViewById(R.id.settingsBtn);

        recommendFrg=TaskListFrg.newInstance(DataClient.LISTTYPE_RECOMMEND,userId);
        publishbtn=(Button)findViewById(R.id.publishTaskBtn);


        fm=getFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.replace(R.id.contentFl,recommendFrg);
        transaction.commit();

        publishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainAty.this,PublishTaskFstAty.class);
                intent.putExtra("userId",userId);
                Log.i(TAG,"getIntent userId: "+userId);
                startActivity(intent);
            }
        });
        //set fragment transaction
        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recommendFrg == null)
                    recommendFrg = TaskListFrg.newInstance(DataClient.LISTTYPE_RECOMMEND,userId);
                FragmentTransaction ts=fm.beginTransaction();
                ts.replace(R.id.contentFl, recommendFrg);
                ts.commit();
            }
        });
        highRewordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (highRewordFrg == null)
                    highRewordFrg = TaskListFrg.newInstance(DataClient.LISTTYPE_HIGHREWORD,userId);
                FragmentTransaction ts=fm.beginTransaction();
                ts.replace(R.id.contentFl, highRewordFrg);
                ts.commit();
            }
        });
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFrg == null)
                    allFrg = TaskListFrg.newInstance(DataClient.LISTTYPE_ALL,userId);
                FragmentTransaction ts=fm.beginTransaction();
                ts.replace(R.id.contentFl, allFrg);
                ts.commit();
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingsFrg == null)
                    settingsFrg = SettingsFrg.newInstance(userId);
                FragmentTransaction ts=fm.beginTransaction();
                ts.replace(R.id.contentFl, settingsFrg);
                ts.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_aty, menu);
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
