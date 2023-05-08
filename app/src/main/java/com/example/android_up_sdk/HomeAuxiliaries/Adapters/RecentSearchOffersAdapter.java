package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.IAdapterCallback;

import java.util.List;

public class RecentSearchOffersAdapter extends BaseAdapter {
    Context context;
    List<String> listData;
    LayoutInflater inflter;
    IAdapterCallback iAdapterCallback;

    public RecentSearchOffersAdapter(Context _context, List<String> _listData, IAdapterCallback _iAdapterCallback) {
        this.context = _context;
        this.listData = _listData;
        this.iAdapterCallback = _iAdapterCallback;
        inflter = (LayoutInflater.from(_context));
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflter.inflate(R.layout.adpter_recent_searches, null);
            viewHolder = new ViewHolder();
            viewHolder.txvName = convertView.findViewById(R.id.adptr_autocmplt_seatch_txv_name);
            viewHolder.rlDelete = convertView.findViewById(R.id.adptr_recent_search_rl_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (listData != null && listData.size() > 0) {
            try {
                String strName = listData.get(position);
                StringBuilder builder = new StringBuilder();
                String[] strArray = strName.split(" ");
                for (String s : strArray) {
                    String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                    builder.append(cap + " ");
                }
                viewHolder.txvName.setText(builder.toString());
            } catch (Exception e) {
                viewHolder.txvName.setText(listData.get(position));

                e.printStackTrace();
            }
        }

        viewHolder.txvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterCallback.onAdapterEventFired(IAdapterCallback.EVENT_B, position);
            }
        });
        viewHolder.rlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterCallback.onAdapterEventFired(IAdapterCallback.EVENT_A, position);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView txvName;
        RelativeLayout rlDelete;
    }

}