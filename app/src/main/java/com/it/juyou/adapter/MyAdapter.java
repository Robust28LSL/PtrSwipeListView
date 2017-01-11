package com.it.juyou.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.juyou.R;
import com.it.juyou.view.DragMessageView;

import java.util.HashSet;
import java.util.List;

/**
 * Created by LSL on 2016/11/15.
 */
public class MyAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private HashSet<DragMessageView> set = new HashSet<>();

    public MyAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(context, R.layout.lv_item, null);
        } else {
            view = convertView;
        }

        final DragMessageView dragMessageView = (DragMessageView) view;
        dragMessageView.setmOnDragChangeListener(new DragMessageView.OnDragChangeListener() {

            @Override
            public void onOpen() {
                set.add(dragMessageView);
            }

            @Override
            public void onClose() {
                set.remove(dragMessageView);
            }

            @Override
            public void onDraging() {
                for (DragMessageView dragview : set) {
                    dragview.close();
                }
            }
        });

        ViewHolder holder = ViewHolder.getHolder(view);
        holder.tv.setText(list.get(position));
        holder.tv_tofirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "当前条目" + position + "----置顶", Toast.LENGTH_SHORT).show();
                String str = list.get(position);
                list.remove(str) ;
                list.add(0, str);
                MyAdapter.this.notifyDataSetChanged();
            }
        });

        holder.tv_todelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "当前条目" + position + "----删除", Toast.LENGTH_SHORT).show();
                list.remove(position);
                MyAdapter.this.notifyDataSetChanged();
            }
        });

        return view;
    }

    private static class ViewHolder {

        TextView tv;
        TextView tv_tofirst;
        TextView tv_todelete;

        public ViewHolder(TextView tv, TextView tv_tofirst, TextView tv_todelete) {
            this.tv = tv;
            this.tv_tofirst = tv_tofirst;
            this.tv_todelete = tv_todelete;
        }

        public static ViewHolder getHolder(View view) {
            Object tag = view.getTag();
            if (tag != null) {
                return ((ViewHolder) tag);
            } else {
                return new ViewHolder((
                        (TextView) view.findViewById(R.id.tv)),
                        ((TextView) view.findViewById(R.id.tv_tofirst)),
                        ((TextView) view.findViewById(R.id.tv_todelete)));
            }
        }
    }
}