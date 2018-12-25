package com.phanthanh.cnttk39a.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
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

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> implements AdapterView.OnItemClickListener {

    public ArrayList<Post> dataSet;
    Context context;
    int resource;

    public PostAdapter(@NonNull Context context, int resource, ArrayList<Post> dataSet) {
        super(context, resource, dataSet);
        this.dataSet = dataSet;
        this.context = context;
        this.resource = resource;
    }

    public static class ViewHolder {
        TextView title;
        ImageView avatar;
        TextView view;
        TextView username;
        TextView tags;
        ImageView type;
        TextView time;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post post = getItem(position);
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, null);
        viewHolder = new ViewHolder();
        viewHolder.title = (TextView) convertView.findViewById(R.id.txttieude);
        viewHolder.username = (TextView) convertView.findViewById(R.id.txtusername);
        viewHolder.avatar = (ImageView) convertView.findViewById(R.id.txtavartar);
        viewHolder.tags = (TextView) convertView.findViewById(R.id.txttags);
        viewHolder.view = (TextView) convertView.findViewById(R.id.txtview);
        viewHolder.type = (ImageView) convertView.findViewById(R.id.txttype);
        viewHolder.time = (TextView) convertView.findViewById(R.id.txtngaydang);

        viewHolder.title.setText(post.getTitle());
        viewHolder.username.setText(post.getUsername());
        //Avatar
        String image = post.getAvatar();
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
        viewHolder.avatar.setImageBitmap(imageRounded);

        viewHolder.tags.setText(post.getTag());
        viewHolder.view.setText(""+ post.getView());
        if(post.getType().equals("question")){
            viewHolder.type.setImageResource(R.mipmap.ic_question);
        }

        viewHolder.time.setText("- " + post.getCreatedat());

        return convertView;
    }

    public void addListItem(ArrayList<Post> itemplus){
        dataSet.addAll(itemplus);
        this.notifyDataSetChanged();
    }
}
