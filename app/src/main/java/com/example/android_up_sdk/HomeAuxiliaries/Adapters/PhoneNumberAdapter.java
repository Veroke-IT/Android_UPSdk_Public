package com.example.android_up_sdk.HomeAuxiliaries.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_up_sdk.R;
import com.example.android_up_sdk.Utils.IAdapterCallback;

import java.util.ArrayList;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private IAdapterCallback iAdapterCallback;
    private int lastPosition = -1;

    public PhoneNumberAdapter(Context context, ArrayList<String> mData, IAdapterCallback iAdapterCallback) {
        this.context = context;
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.iAdapterCallback = iAdapterCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lay_item_multiple_phone_number, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = position;
        if (mData.get(position) != null) {

            holder.txvPhoneNumber.setText(mData.get(position));


            holder.btnRlCall.setOnClickListener(v -> iAdapterCallback.onAdapterEventFired(IAdapterCallback.EVENT_A, pos));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txvPhoneNumber;
        Button btnCall;
        RelativeLayout btnRlCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txvPhoneNumber = itemView.findViewById(R.id.lay_item_multiple_phone_number_txv_phno);
            btnRlCall = itemView.findViewById(R.id.lay_item_multiple_phone_number_rl_call_phno);

        }
    }
}

