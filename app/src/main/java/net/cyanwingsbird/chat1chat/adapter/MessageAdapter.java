package net.cyanwingsbird.chat1chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.cyanwingsbird.chat1chat.ChatRoomActivity;
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
        /*
        Message current_messsage = items.getMessages().get(position);

        boolean isComMsg;
        ViewHolder viewHolder = null;
        if(current_messsage.getSender().toString().equals(Global.getAccountInfo().getUserid()))
        {
            isComMsg = false;
        }else
        {
            isComMsg = true;
        }
   //     if (convertView == null) {
            if (isComMsg) {
                convertView = View.inflate(context.getApplicationContext(),
                        R.layout.item_chatting_other, null);
            } else {
                convertView = View.inflate(context.getApplicationContext(),
                        R.layout.item_chatting_myself, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.tvUserName = (TextView) convertView
                    .findViewById(R.id.tv_username);
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_chatcontent);
            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);

//        }else {
 //           viewHolder = (ViewHolder) convertView.getTag();
  //      }



        if(isComMsg==false)
        {
            viewHolder.tvUserName.setText(Global.getLoginInfo().getUsername());
        }else
        {
            if(chatRoomActivity ==null)
            {
                chatRoomActivity = (ChatRoomActivity)context;
            }
            viewHolder.tvUserName.setText(chatRoomActivity.getTarget_name());
        }
        viewHolder.tvContent.setText(current_messsage.getContent());
        */
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvUserName;
        public TextView tvContent;
        public boolean isComMsg = true;
    }


}
