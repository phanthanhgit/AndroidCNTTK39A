package com.phanthanh.cnttk39a.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phanthanh.cnttk39a.R;
import com.phanthanh.cnttk39a.model.Post;
import com.phanthanh.cnttk39a.model.Tag;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends ArrayAdapter<Tag> implements AdapterView.OnItemClickListener {

    public ArrayList<Tag> dataSet;
    Context context;
    int resource;

    public TagAdapter(@NonNull Context context, int resource, ArrayList<Tag> dataSet){
        super(context, resource, dataSet);
        this.dataSet = dataSet;
        this.context = context;
        this.resource = resource;
    }

    public static class ViewHolder{
        TextView name;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tag tag = getItem(position);
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, null);
        viewHolder = new ViewHolder();
        viewHolder.name = (TextView) convertView.findViewById(R.id.txtname);

        viewHolder.name.setText(tag.getName());
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
