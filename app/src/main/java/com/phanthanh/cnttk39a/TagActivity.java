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
import com.phanthanh.cnttk39a.adapter.TagAdapter;
import com.phanthanh.cnttk39a.model.Post;
import com.phanthanh.cnttk39a.model.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TagActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView lstTag;
    ArrayList<Tag> arrayList;
    TagAdapter adapter;
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
        setContentView(R.layout.activity_tag);

        //Anh xa
        progressBar = (ProgressBar) findViewById(R.id.progressBarTag);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.footer_listview, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.rftag);

        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setOnRefreshListener(this);

        lstTag = (ListView) findViewById(R.id.lsttag);
        mHandler = new mHandler();
        arrayList = new ArrayList<Tag>();
        adapter = new TagAdapter(TagActivity.this, R.layout.item_tag, arrayList);
        lstTag.setAdapter(adapter);
        getData();

        lstTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tag tag = adapter.getItem(i);
                Intent intent = new Intent(TagActivity.this, TagPostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", tag.getName());
                bundle.putString("slug", tag.getSlug());
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
            getData();
        }
    }

    public  class mHandler extends Handler  {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    lstTag.addFooterView(footerView);
                    break;
                case 1:
                    getData();
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

    private void getData(){
        String url = "http://codedao.phanthanhblog.com/public/api/tags";

        RequestQueue requestQueue = Volley.newRequestQueue(TagActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        JSONArray arr = response.getJSONArray("tags");
                        if(arr.length() == 0){
                            limitData = true;
                            Toast.makeText(TagActivity.this, "Đã hết dữ liệu!", Toast.LENGTH_LONG).show();
                        }
                        for(int i = 0; i < arr.length(); i++){
                            JSONObject tag = arr.getJSONObject(i);
                            arrayList.add(new Tag(tag.getInt("id"), tag.getString("name"), tag.getString("slug")) );
                        }
                        adapter.notifyDataSetChanged();
                        lstTag.removeFooterView(footerView);
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
