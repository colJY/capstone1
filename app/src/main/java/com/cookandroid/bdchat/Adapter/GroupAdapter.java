package com.cookandroid.bdchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.cookandroid.bdchat.Models.GroupMessage;
import com.cookandroid.bdchat.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends ArrayAdapter<GroupMessage> {
    private ArrayList<GroupMessage> dataSet;
    Context mContext;

    private static class ViewHolder{
        CircleImageView receiverAvt;
        TextView    receiverName;
        TextView    receiverMess;
        TextView    receiverTime;
        TextView    senderMess;
        TextView    senderTime;
        ConstraintLayout constraintLayout;
        ConstraintLayout receiverlayout;
    }

    public GroupAdapter(ArrayList<GroupMessage> data, Context context){
        super(context,R.layout.item_message_group,data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        GroupMessage groupMess = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_message_group,parent,false);
            viewHolder.receiverAvt = (CircleImageView)convertView.findViewById(R.id.image_message_profile);
            viewHolder.receiverName = (TextView)convertView.findViewById(R.id.text_message_name);
            viewHolder.receiverMess = (TextView)convertView.findViewById(R.id.text_message_receiver);
            viewHolder.receiverTime = (TextView)convertView.findViewById(R.id.text_message_time_receiver);
            viewHolder.senderMess = (TextView)convertView.findViewById(R.id.text_message_send);
            viewHolder.senderTime = (TextView)convertView.findViewById(R.id.text_message_time_send);
            viewHolder.constraintLayout = (ConstraintLayout) convertView.findViewById(R.id.constraintLayout);
            viewHolder.receiverlayout = (ConstraintLayout) convertView.findViewById(R.id.receiverConstraintlayout);
            result=convertView;
                convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.receiverAvt.setVisibility(View.INVISIBLE);
        viewHolder.receiverName.setVisibility(View.INVISIBLE);
        viewHolder.receiverMess.setVisibility(View.INVISIBLE);
        viewHolder.receiverTime.setVisibility(View.INVISIBLE);
        viewHolder.senderMess.setVisibility(View.INVISIBLE);
        viewHolder.senderTime.setVisibility(View.INVISIBLE);
        viewHolder.constraintLayout.setVisibility(View.INVISIBLE);
        viewHolder.receiverlayout.setVisibility(View.INVISIBLE);

        if (groupMess.getSenMsg().equals("")){
            viewHolder.receiverName.setVisibility(View.VISIBLE);
            viewHolder.receiverAvt.setVisibility(View.VISIBLE);
            viewHolder.receiverMess.setVisibility(View.VISIBLE);
            viewHolder.receiverTime.setVisibility(View.VISIBLE);
            viewHolder.receiverlayout.setVisibility(View.VISIBLE);
            viewHolder.receiverName.setText(groupMess.getRecName());
            viewHolder.receiverMess.setText(groupMess.getRecMsg());
            viewHolder.receiverTime.setText(groupMess.getRecTime() + " ");//+groupMess.getRecDate().substring(0,groupMess.getRecDate().length()-6)
        }
        else {
            viewHolder.senderMess.setVisibility(View.VISIBLE);
            viewHolder.senderTime.setVisibility(View.VISIBLE);
            viewHolder.constraintLayout.setVisibility(View.VISIBLE);
            viewHolder.senderMess.setText(groupMess.getSenMsg());
            viewHolder.senderTime.setText(groupMess.getSenTime()+" ");//+groupMess.getSenDate().substring(0,groupMess.getSenDate().length()-6)
        }

        return convertView;
    }
}
