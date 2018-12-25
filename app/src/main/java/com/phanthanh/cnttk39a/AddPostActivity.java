package com.phanthanh.cnttk39a;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPostActivity extends AppCompatActivity {

    EditText txttitle;
    EditText txtcontent;
    EditText txttag;
    RadioButton txtpost;
    RadioButton txtquestion;
    Button btnAdd;
    Button btnCancel;
    ProgressDialog progressDialog;
    mHandler mHandler;
    String title;
    String content;
    String tag;
    String email;
    String password;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        txttitle = (EditText) findViewById(R.id.txttitle1);
        txtcontent = (EditText) findViewById(R.id.txtcontent1);
        txttag = (EditText) findViewById(R.id.txttag);
        txtpost = (RadioButton) findViewById(R.id.txtpost);
        txtquestion = (RadioButton) findViewById(R.id.txtquestion);

        btnAdd = (Button) findViewById(R.id.btnadd);
        btnCancel = (Button) findViewById(R.id.btncancel);

        mHandler = new mHandler();

        SharedPreferences sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        password = sharedPreferences.getString("password", "");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = txttitle.getText().toString();
                content = txtcontent.getText().toString();
                if(txtpost.isChecked()) type = "post"; else type = "question";
                tag = txttag.getText().toString();
                ThreadData threadData = new ThreadData();
                threadData.start();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addPost(String email, String password, String title, String content, String tag, String type){
        String url = "https://codedao.phanthanhblog.com/post/add/"+email+"/"+password+"?title="+title+"&content="+content+"&type="+type+"&tag="+tag;

        RequestQueue requestQueue = Volley.newRequestQueue(AddPostActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        String ok = response.getString("add");
                        if(ok.equals("success")){
                            Toast.makeText(AddPostActivity.this, "Đăng bài viết thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddPostActivity.this, "Bài viết phải có tiêu đề và nội dung!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
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

    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    progressDialog = ProgressDialog.show(AddPostActivity.this, "Đang đăng bài", "Vui lòng chờ đợi...", true, false);
                    break;
                case 1:
                    addPost(email, password, title, content, tag, type);
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
}
