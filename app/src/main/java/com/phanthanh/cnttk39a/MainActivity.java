package com.phanthanh.cnttk39a;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phanthanh.cnttk39a.adapter.PostAdapter;
import com.phanthanh.cnttk39a.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

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
    NavigationView navigationView;
    TextView txtfullname;
    TextView txtemail;
    ImageView txtavatar;
    int activity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Anh xa
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.footer_listview, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLV);

        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setOnRefreshListener(this);

        lstPost = (ListView) findViewById(R.id.lstpost);
        mHandler = new mHandler();
        arrayList = new ArrayList<Post>();
        adapter = new PostAdapter(MainActivity.this, R.layout.item_list_post, arrayList);
        lstPost.setAdapter(adapter);
        getData(++page);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUser() == true){
                    activity = 2;
                    Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                    startActivity(intent);
                } else {
                    activity = 1;
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtavatar = (ImageView) headerView.findViewById(R.id.tavatar);
        txtfullname = (TextView) headerView.findViewById(R.id.tfullname);
        txtemail = (TextView) headerView.findViewById(R.id.temail);

        loginManager();

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
                activity = 3;
                Post post = adapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, DetailPost.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", post.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_logout){
            SharedPreferences sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
            if(sharedPreferences != null){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("username");
                editor.remove("fullname");
                editor.remove("avatar");
                editor.commit();
            }
            loginManager();
        } else if (id == R.id.nav_login) {
            activity = 1;
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_question) {
            Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tags) {
            Intent intent = new Intent(MainActivity.this, TagActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        String url = "http://codedao.phanthanhblog.com/public/api/post?page=" + page;

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        JSONObject posts = response.getJSONObject("posts");
                        JSONArray arr = posts.getJSONArray("data");
                        if(arr.length() == 0){
                            limitData = true;
                            Toast.makeText(MainActivity.this, "Đã hết dữ liệu!", Toast.LENGTH_LONG).show();
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

    public boolean checkUser(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        if(sharedPreferences != null) {
            if(!sharedPreferences.getString("email", "").equals("")) return true;
            else return false;
        } else return false;
    }

    public void loginManager(){
        if(checkUser() == true){
            SharedPreferences sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
            Menu nav_menu = navigationView.getMenu();
            nav_menu.findItem(R.id.nav_login).setVisible(false);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);

            String image = sharedPreferences.getString("avatar", "");

            image = image.replace("data:image/png;base64,","");
            byte[] imageAsBytes = Base64.decode(image.getBytes(), 0);
            Bitmap mbitmap = BitmapFactory.decodeByteArray(
                    imageAsBytes, 0, imageAsBytes.length);
            Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
            Canvas canvas = new Canvas(imageRounded);
            Paint mpaint = new Paint();
            mpaint.setAntiAlias(true);
            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 300, 300, mpaint);
            txtavatar.setImageBitmap(imageRounded);

            txtemail.setText(sharedPreferences.getString("email", ""));
            txtfullname.setText(sharedPreferences.getString("fullname", "username"));


        } else {
            Menu nav_menu = navigationView.getMenu();
            nav_menu.findItem(R.id.nav_login).setVisible(true);
            nav_menu.findItem(R.id.nav_logout).setVisible(false);

            txtavatar.setImageResource(R.mipmap.ic_launcher_round);

            txtemail.setText("Bạn chưa đăng nhập");
            txtfullname.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(activity == 1) loginManager();
        if(activity == 2) onRefresh();
        activity = 0;
    }
}
