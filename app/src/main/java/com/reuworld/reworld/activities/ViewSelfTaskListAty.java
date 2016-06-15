package com.reuworld.reworld.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reuworld.reworld.R;
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
    private ListView listView;
    private List<Map<String,String>> taskListContent;
    SimpleAdapter listAdapter;
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_self_task_list_aty);
        listView=(ListView)findViewById(R.id.listView);

        int listType=getIntent().getIntExtra("listType", 0);
        int[] listContent=getIntent().getIntArrayExtra("listContent");

        taskListContent=new ArrayList<>();

        for(int dstId:listContent){
            Call<Map<String,Object>> call= DataClient.service.getCompTaskInfo(DataClient.ACTION_GET_COMP_TASKINFO,dstId);
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    int statecode = ((Double)response.body().get("statecode")).intValue();
                    if (statecode == 0) {
                        final CompTaskInfo compTaskInfo = gson.fromJson(response.body().get("content").toString(), CompTaskInfo.class);

                        Map<String, String> map=new HashMap<String, String>();
                        map.put("taskId",compTaskInfo.getTaskID()+"");
                        map.put("taskTitle", compTaskInfo.getTaskTitle());
                        map.put("taskDescription", compTaskInfo.getTaskDescription());
                        map.put("promulgatorName", compTaskInfo.getPromulgatorName());
                        map.put("bounty", compTaskInfo.getBounty() + "");
                        taskListContent.add(map);

                    }else {
                        Toast.makeText(ViewSelfTaskListAty.this,"错误！",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(ViewSelfTaskListAty.this,"错误！",Toast.LENGTH_SHORT).show();
                }
            });
        }

        listAdapter=new SimpleAdapter(ViewSelfTaskListAty.this, taskListContent, R.layout.listview_tasklist,
                new String[]{"taskTitle", "taskDescription", "promulgatorName", "bounty"},
                new int[]{R.id.titleText, R.id.descriptionText, R.id.usernameText, R.id.rewordText});

        listView.setAdapter(listAdapter);

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
