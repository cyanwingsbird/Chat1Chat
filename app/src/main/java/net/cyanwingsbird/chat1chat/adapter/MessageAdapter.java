package net.cyanwingsbird.chat1chat.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import net.cyanwingsbird.chat1chat.ChatRoomActivity;
import net.cyanwingsbird.chat1chat.Global;
import net.cyanwingsbird.chat1chat.R;
import net.cyanwingsbird.chat1chat.dataset.Message;

import java.util.ArrayList;

/**
 * Created by lampinghim on 16/8/2016.
 */

public class MessageAdapter extends BaseAdapter {

    static final String TAG = "MessageAdapter";

    Context context;
    ArrayList<Message> items;
    ChatRoomActivity chatRoomActivity = null;

    public MessageAdapter(Context context, ArrayList<Message> items) {
        this.context = context;
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Message current_message = items.get(position);
        if (chatRoomActivity == null) {
            chatRoomActivity = (ChatRoomActivity) context;
        }
        boolean isComMsg;
        if (current_message.getFromUserID().toString().equals(Global.getAccountInfo().getUserID())) {
            isComMsg = false;
        } else {
            isComMsg = true;
        }

        if (isComMsg) {
            if(current_message.getMessageType().equals("2")) {
                convertView = View.inflate(context.getApplicationContext(), R.layout.item_image_other, null);
                ImageView tv_chatImage = (ImageView) convertView.findViewById(R.id.tv_chatImage);
                Picasso.with(chatRoomActivity)
                        .load(Global.getServerURL() + current_message.getMessageContent())
                        .placeholder(R.drawable.ic_sync_black_24dp)
                        .into(tv_chatImage);
            }else if(current_message.getMessageType().equals("3")) {
                convertView = View.inflate(context.getApplicationContext(), R.layout.item_video_other, null);
                ImageView tv_chatVideo = (ImageView) convertView.findViewById(R.id.tv_chatVideo);
            }else{
                convertView = View.inflate(context.getApplicationContext(), R.layout.item_chatting_other, null);
                TextView tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                tvContent.setText(current_message.getMessageContent());
            }




        } else {
            if(current_message.getMessageType().equals("2")) {
                convertView = View.inflate(context.getApplicationContext(), R.layout.item_image_myself, null);
                ImageView tv_chatImage = (ImageView) convertView.findViewById(R.id.tv_chatImage);
                Picasso.with(chatRoomActivity)
                        .load(Global.getServerURL() + current_message.getMessageContent())
                        .placeholder(R.drawable.ic_sync_black_24dp)
                        .into(tv_chatImage);
            }else if(current_message.getMessageType().equals("3")) {
                convertView = View.inflate(context.getApplicationContext(), R.layout.item_video_myself, null);
                ImageView tv_chatVideo = (ImageView) convertView.findViewById(R.id.tv_chatVideo);
            }else{
                convertView = View.inflate(context.getApplicationContext(), R.layout.item_chatting_myself, null);
                TextView tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                tvContent.setText(current_message.getMessageContent());
            }
        }

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
        ImageView iv_userhead = (ImageView) convertView.findViewById(R.id.iv_userhead);
        if (isComMsg == false) {
            tvUserName.setText(Global.getLoginInfo().getUsername());
            if(Global.getAccountInfo().getProfilePic()!=null) {
                Picasso.with(chatRoomActivity)
                        .load(Global.getServerURL() + Global.getAccountInfo().getProfilePic().substring(2))
                        .placeholder(R.drawable.ic_account_circle_50dp)
                        .into(iv_userhead);
            }
        } else {
            if(chatRoomActivity.getTarget_profile()!=null) {
                Picasso.with(chatRoomActivity)
                        .load(chatRoomActivity.getTarget_profile())
                        .placeholder(R.drawable.ic_account_circle_50dp)
                        .into(iv_userhead);
            }
            tvUserName.setText(chatRoomActivity.getTarget_name());
        }

        return convertView;
    }
}
