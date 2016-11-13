package com.reuworld.reworld.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reuworld.reworld.R;
import com.reuworld.reworld.fragments.TaskListFrg;
import com.reuworld.reworld.tools.DataClient;
import com.reuworld.reworld.unity.CompTaskInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewSelfTaskListAty extends Activity {

    private final int runningList=0;
    private final int completedList=1;
    private final int failedList=2;
    private FragmentManager fm;
    private Fragment recommendFrg;
    private ListView listView;
    private List<Map<String,String>> taskListContent;
    SimpleAdapter listAdapter;
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_self_task_list_aty);

        int listType=getIntent().getIntExtra("listType", 0);
        int userId=getIntent().getIntExtra("userId",0);

        recommendFrg= TaskListFrg.newInstance(listType, userId);

        fm=getFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.replace(R.id.contentFl, recommendFrg);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_self_task_list_aty, menu);
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
