package net.cyanwingsbird.chat1chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.cyanwingsbird.chat1chat.R;
import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.dataset.FriendList;


/**
 * Created by lampinghim on 5/12/2016.
 */

public class PendingListAdapter extends BaseAdapter {
    static final String TAG = "PendingListAdapter";

    Context context;
    FriendList items;

    public PendingListAdapter(Context context, FriendList items) {
        this.context = context;
        this.items = items;
    }

    public int getCount() {
        return items.getFriendArrayList().size();
    }

    public Object getItem(int position) {
        return items.getFriendArrayList().get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Friend current_friend = items.getFriendArrayList().get(position);
        FriendListAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new FriendListAdapter.ViewHolder();
    //        convertView = View.inflate(context.getApplicationContext(), R.layout.item_pending_friend, null);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (FriendListAdapter.ViewHolder) convertView.getTag();
        }

     //   viewHolder.friendName.setText(current_friend.getName());
        return convertView;
    }

    public static class ViewHolder {
        public TextView friendName;

    }


}
