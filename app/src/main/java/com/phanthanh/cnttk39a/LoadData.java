package com.phanthanh.cnttk39a;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LoadData extends AsyncTask<String, String, String> {

    public Context context;
    public ArrayList<String> arrayList;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... str) {
        return docNoiDung_Tu_URL(str[0]);
    }

    @Override
    protected void onPostExecute(String s) {
//        Toast.makeText(context , s, Toast.LENGTH_LONG).show();
        try {
            arrayList = new ArrayList<>();
            JSONObject read = new JSONObject(s);
            String ss = read.getString("max");
            JSONObject posts = read.getJSONObject("posts");
            JSONArray arr = posts.getJSONArray("data");
            for(int i = 0; i < arr.length(); i++){
                JSONObject post = arr.getJSONObject(i);
                arrayList.add(post.getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }
}
