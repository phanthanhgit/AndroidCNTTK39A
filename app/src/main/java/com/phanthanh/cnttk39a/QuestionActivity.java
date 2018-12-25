package com.phanthanh.cnttk39a;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.phanthanh.cnttk39a.adapter.PostAdapter;
import com.phanthanh.cnttk39a.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ListView lstPost;
    ArrayList<Post> arrayList;
    PostAdapter adapter;
    ProgressBar progressBar;
    int page = 0;
    View footerView;
    boolean isLoading = true;
    boolean limitData = false;
    Handler mHandler;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Anh xa
        progressBar = (ProgressBar) findViewById(R.id.progressBarQA);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.footer_listview, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.rfquestion);

        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setOnRefreshListener(this);

        lstPost = (ListView) findViewById(R.id.lstquestion);
        mHandler = new mHandler();
        arrayList = new ArrayList<Post>();
        adapter = new PostAdapter(QuestionActivity.this, R.layout.item_list_post, arrayList);
        lstPost.setAdapter(adapter);
        getData(++page);

        lstPost.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleTtem, int itemCount) {
                if(absListView.getLastVisiblePosition() == itemCount - 1 && isLoading == false && itemCount != 0 && limitData == false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });

        lstPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post post = adapter.getItem(i);
                Intent intent = new Intent(QuestionActivity.this, DetailPost.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", post.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        if(isLoading == false){
            isLoading = true;
            limitData = false;
            arrayList.clear();
            page = 0;
            getData(++page);
        }
    }

    public  class mHandler extends Handler  {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    lstPost.addFooterView(footerView);
                    break;
                case 1:
                    getData(++page);
                    break;
            }
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            super.run();
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
        }
    }

    private void getData(int page){
        String url = "http://codedao.phanthanhblog.com/public/api/post/question-type?page=" + page;

        RequestQueue requestQueue = Volley.newRequestQueue(QuestionActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        JSONObject posts = response.getJSONObject("posts");
                        JSONArray arr = posts.getJSONArray("data");
                        if(arr.length() == 0){
                            limitData = true;
                            Toast.makeText(QuestionActivity.this, "Đã hết dữ liệu!", Toast.LENGTH_LONG).show();
                        }
                        for(int i = 0; i < arr.length(); i++){
                            JSONObject post = arr.getJSONObject(i);
                            arrayList.add(new Post(post.getInt("id"), post.getString("title"), post.getString("content"), post.getString("tags"), post.getString("type"), post.getInt("user_id"), post.getInt("view"), post.getString("created_at"), post.getString("username"), post.getString("avatar")));
                        }
                        adapter.notifyDataSetChanged();
                        lstPost.removeFooterView(footerView);
                        isLoading = false;
                        progressBar.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
