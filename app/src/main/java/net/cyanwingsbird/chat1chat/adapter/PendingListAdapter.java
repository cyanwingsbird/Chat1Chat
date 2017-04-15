package net.cyanwingsbird.chat1chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.cyanwingsbird.chat1chat.Global;
import net.cyanwingsbird.chat1chat.R;
import net.cyanwingsbird.chat1chat.dataset.Friend;

import java.util.ArrayList;


/**
 * Created by lampinghim on 5/12/2016.
 */

public class PendingListAdapter extends BaseAdapter {
    static final String TAG = "PendingListAdapter";

    Context context;
    ArrayList<Friend> items;

    public PendingListAdapter(Context context, ArrayList<Friend> items) {
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

        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = View.inflate(context.getApplicationContext(), R.layout.item_pending_friend, null);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.profile_pic = (ImageView) convertView.findViewById(R.id.profile_pic);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (items.get(position).getProfilePic() == null) {
            viewHolder.profile_pic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_account_circle_50dp));
        } else {
            String profilePicURL = Global.getServerURL() + items.get(position).getProfilePic().substring(2);
            Picasso.with(context)
                    .load(profilePicURL)
                    .placeholder(R.drawable.ic_account_circle_50dp)
                    .into(viewHolder.profile_pic);
        }
        if (items.get(position).getDisplayName() != null) {
            viewHolder.friendName.setText(items.get(position).getDisplayName());
        }
        return convertView;
    }

    public static class ViewHolder {
        ImageView profile_pic;
        public TextView friendName;

    }


}
