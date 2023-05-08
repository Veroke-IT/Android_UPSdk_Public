package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android_up_sdk.R;

import java.util.List;

public class SearchOffersAdapter extends BaseAdapter {
    Context context;
    List<String> listData;
    LayoutInflater inflter;


    public SearchOffersAdapter(Context _context, List<String> _listData) {
        this.context = _context;
        this.listData = _listData;
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
            convertView = inflter.inflate(R.layout.adpter_auto_complete_search_location_info, null);
            viewHolder = new ViewHolder();
            viewHolder.txvName = convertView.findViewById(R.id.adptr_autocmplt_seatch_txv_name);
            viewHolder.txvAddress = convertView.findViewById(R.id.adptr_autocmplt_seatch_txv_address);
            viewHolder.txvDistance = convertView.findViewById(R.id.adptr_autocmplt_seatch_txv_distance);
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
        return convertView;
    }

    public static class ViewHolder {
        TextView txvName, txvAddress, txvDistance;
    }


}