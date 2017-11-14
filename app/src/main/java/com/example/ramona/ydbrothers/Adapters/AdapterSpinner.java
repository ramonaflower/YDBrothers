package com.example.ramona.ydbrothers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.R;

import java.util.List;

/**
 * Created by Ramona on 7/26/2017.
 */

public class AdapterSpinner extends BaseAdapter {
    Context context;
    List<UserEntities> list;
    LayoutInflater inflater;

    public AdapterSpinner(Context context, List<UserEntities> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.lt_item_spinner, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img2);
        TextView textView = (TextView) convertView.findViewById(R.id.txt2);
        Glide.with(context).load(list.get(position).getsUserImage()).into(imageView);
        textView.setText(list.get(position).getsUserName());
        return convertView;
    }
}
