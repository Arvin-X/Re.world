package com.reuworld.reworld.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reuworld.reworld.R;
import com.reuworld.reworld.activities.TaskInfoAty;
import com.reuworld.reworld.tools.DataClient;
import com.reuworld.reworld.unity.BriefTaskInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * interface.
 */
public class TaskListFrg extends ListFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private View contentView;
    private BGARefreshLayout refreshLayout;
    private List<Map<String,String>> taskListContent;
    private int listType;
    private int userId;
    private SimpleAdapter listAdapter;
    private final String TAG="--TaskListFrg--";
    private Gson gson=new Gson();

    private final String promptStr="加载中...";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskListFrg() {

    }

    public static TaskListFrg newInstance(int listType,int userId) {
        TaskListFrg fragment = new TaskListFrg();
        Bundle args = new Bundle();
        args.putInt("listType", listType);
        args.putInt("userId",userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_list_refresh,
                container, false);

        if (getArguments() != null) {
            listType = getArguments().getInt("listType");
            userId=getArguments().getInt("userId");
        }
        initRefreshLayout();
        taskListContent=new ArrayList<>();
        beginLoadingMore();
        listAdapter=new SimpleAdapter(this.getActivity(), taskListContent, R.layout.listview_tasklist,
                new String[]{"taskTitle", "taskDescription", "promulgatorName", "bounty"},
                new int[]{R.id.titleText, R.id.descriptionText, R.id.usernameText, R.id.rewordText});
        setListAdapter(listAdapter);

        return contentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //TODO
        //show task info
        Intent intent=new Intent(TaskListFrg.this.getActivity(), TaskInfoAty.class);
        intent.putExtra("taskId",Integer.parseInt(taskListContent.get(position).get("taskId")));
        intent.putExtra("userId",userId);
        Log.i(TAG, "onListItemClick: taskId: " + taskListContent.get(position).get("taskId"));
        startActivity(intent);
    }

    private void initRefreshLayout(){
        refreshLayout=(BGARefreshLayout)contentView.findViewById(R.id.refreshLayout);
        // 为BGARefreshLayout设置代理
        refreshLayout.setDelegate(TaskListFrg.this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(TaskListFrg.this.getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格
        refreshLayout.setRefreshViewHolder(refreshViewHolder);



        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
//         refreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText(promptStr);
        // 设置整个加载更多控件的背景颜色资源id
//        refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景drawable资源id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源id
//        refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景drawable资源id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        refreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据
        Call<Map<String,Object>> call=DataClient.service.getBriefTaskList(DataClient.ACTION_GET_BRIEF_TASKLIST,listType,userId);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Map<String,Object> map=response.body();
                Integer statecode=((Double)map.get("statecode")).intValue();
                //query success
                if(statecode==0){
                    Log.i(TAG,"onBGARefreshLayoutBeginRefreshing: "+map.get("content").toString());
                    List<BriefTaskInfo> list=gson.fromJson(map.get("content").toString(),new TypeToken<ArrayList<BriefTaskInfo>>(){}.getType());
//                    List<BriefTaskInfo> list=(List<BriefTaskInfo>)map.get("content");
//                    Gson gson=new Gson();
//                    Log.i(TAG,"onBGARefreshLayoutBeginRefreshing: "+list.get(0).getClass());
                    taskListContent.clear();
                    taskListContent.addAll(getDataFromResponseList(list));
//                    taskListContent=getDataFromResponseList(list);
                    listAdapter.notifyDataSetChanged();
//                    listAdapter=new SimpleAdapter(TaskListFrg.this.getActivity(), taskListContent, R.layout.listview_tasklist,
//                            new String[]{"taskTitle", "taskDescription", "promulgatorName", "bounty"},
//                            new int[]{R.id.titleText, R.id.descriptionText, R.id.usernameText, R.id.rewordText});
//                    setListAdapter(listAdapter);


                }else{
                    //unknown error
                    Toast.makeText(TaskListFrg.this.getActivity(),"未知错误！",Toast.LENGTH_LONG).show();
                    Log.i(TAG,"onBGARefreshLayoutBeginRefreshing: statecode: "+statecode);
                }
                refreshLayout.endRefreshing();
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(TaskListFrg.this.getActivity(),"加载失败！",Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.i(TAG, "onBGARefreshLayoutBeginRefreshing: exception" + t.toString());
                refreshLayout.endRefreshing();
            }
        });
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以
        Call<Map<String, Object>> call = DataClient.service.getBriefTaskList(DataClient.ACTION_GET_BRIEF_TASKLIST, listType,userId);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Map<String, Object> map = response.body();
                Integer statecode=((Double)map.get("statecode")).intValue();
                if (statecode == 0) {
                    //query success
                    List<BriefTaskInfo> list=gson.fromJson(map.get("content").toString(),new TypeToken<ArrayList<BriefTaskInfo>>(){}.getType());
//                    List<BriefTaskInfo> list = (List<BriefTaskInfo>) map.get("content");
                    taskListContent.clear();
                    taskListContent.addAll(getDataFromResponseList(list));
//                    taskListContent = getDataFromResponseList(list);
                    listAdapter.notifyDataSetChanged();
//                    listAdapter=new SimpleAdapter(TaskListFrg.this.getActivity(), taskListContent, R.layout.listview_tasklist,
//                            new String[]{"taskTitle", "taskDescription", "promulgatorName", "bounty"},
//                            new int[]{R.id.titleText, R.id.descriptionText, R.id.usernameText, R.id.rewordText});
//                    setListAdapter(listAdapter);
                } else {
                    //unknown error
                    Toast.makeText(TaskListFrg.this.getActivity(), "未知错误！", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onBGARefreshLayoutBeginLoadingMore: statecode: " + statecode);
                }
                refreshLayout.endLoadingMore();
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(TaskListFrg.this.getActivity(), "加载失败！", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.i(TAG, "onBGARefreshLayoutBeginLoadingMore: exception" + t.toString());
                refreshLayout.endLoadingMore();
            }
        });
        //TODO:what does this mean
        return false;
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在activity的onStart方法中调用，自动进入正在刷新状态获取最新数据
    public void beginRefreshing() {
        refreshLayout.beginRefreshing();
    }

    // 通过代码方式控制进入加载更多状态
    public void beginLoadingMore() {
        refreshLayout.beginLoadingMore();
    }

    private List<Map<String,String>> getDataFromResponseList(List<BriefTaskInfo> list){
        List<Map<String,String>> result=new ArrayList<>();
        BriefTaskInfo briefTaskInfo;
        Map<String, String> map;
        for (int i=0;i<list.size();i++) {
            map= new HashMap<>();
            briefTaskInfo=list.get(i);
            map.put("taskId",briefTaskInfo.getTaskID()+"");
            map.put("taskTitle", briefTaskInfo.getTaskTitle());
            map.put("taskDescription", briefTaskInfo.getTaskDescription());
            map.put("promulgatorName", briefTaskInfo.getPromulgatorName());
            map.put("bounty", "¥ "+briefTaskInfo.getBounty());
            result.add(map);
        }
        return result;
    }

}
