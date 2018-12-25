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
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phanthanh.cnttk39a.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    String txtemail;
    String txtpassword;
    String txtusername;
    String txtfullname;
    String txtavatar;
    Button btnlogin;
    ProgressDialog progressDialog;
    mHandler mHandler;
    public boolean in = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Anh xa
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        mHandler = new mHandler();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadData threadData = new ThreadData();
                threadData.start();
            }
        });
    }

    private void checkLogin(String email, String password){
        String url = "https://codedao.phanthanhblog.com/checklogin/" + email + "/" + password;

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        String ok = response.getString("login");
                        if(ok.equals("success")){
                            txtusername = response.getString("username");
                            txtfullname = response.getString("fullname");
                            txtavatar = response.getString("avatar");
                            doSave();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_LONG).show();
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

    public void doSave(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("email", email.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.putString("username", txtusername);
        editor.putString("fullname", txtfullname);
        editor.putString("avatar", txtavatar);

        editor.commit();

        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    progressDialog = ProgressDialog.show(LoginActivity.this, "Đang đăng nhập", "Vui lòng chờ đợi...", true, false);
                    break;
                case 1:
                    txtemail = email.getText().toString();
                    txtpassword = password.getText().toString();
                    checkLogin(txtemail, txtpassword);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
