package net.cyanwingsbird.chat1chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.cyanwingsbird.chat1chat.adapter.MessageAdapter;
import net.cyanwingsbird.chat1chat.dataset.Message;
import net.cyanwingsbird.chat1chat.userAccount.UserAccountManager;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ChatRoomActivity extends AppCompatActivity {

    MainLoadingDialog loadingDialog;
    Toolbar toolbar;

    String target_id;
    String target_name;
    String target_pic;

    @Bind(R.id.send_button)
    Button send_button;
    @Bind(R.id.texting_editText)
    EditText texting_editText;
    @Bind(R.id.message_view)
    ListView message_view;
    @Bind(R.id.profile_pic)
    ImageView profile_pic;
    @Bind(R.id.friend_nickname)
    TextView friend_nickname;

    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(ChatRoomActivity.this);

        target_id = getIntent().getStringExtra("target_id");
        target_name = getIntent().getStringExtra("target_name");
        target_pic = getIntent().getStringExtra("target_pic");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        friend_nickname.setText(target_name);

        if(target_pic!=null) {
            String friendPictureUrl = Global.getServerURL() + target_pic.substring(2);
            Picasso.with(getApplicationContext())
                    .load(friendPictureUrl)
                    .placeholder(R.drawable.ic_account_circle_50dp)
                    .into(profile_pic);
        }

        loadingDialog = new MainLoadingDialog(this);


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Message current_message = new Message();
                current_message.setContent(texting_editText.getText().toString());
                current_message.setSender(Integer.parseInt(Global.getAccountInfo().getUserid()));

                String username = Global.getLoginInfo().getUsername();
                String password = Global.getLoginInfo().getPassword();



                texting_editText.setText(null);
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                messageAdapter.notifyDataSetChanged();
                message_view.smoothScrollToPosition(messageSet.getMessages().size());
*/
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatroom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog dialog;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_send_pic:
                dialog = new AlertDialog.Builder(ChatRoomActivity.this)
                        .setMessage("Coming Soon!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                return true;
            case R.id.menu_send_audio:
                dialog = new AlertDialog.Builder(ChatRoomActivity.this)
                        .setMessage("Coming Soon!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                return true;
            case R.id.menu_send_video:
                dialog = new AlertDialog.Builder(ChatRoomActivity.this)
                        .setMessage("Coming Soon!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                return true;
            case R.id.menu_send_location:
                dialog = new AlertDialog.Builder(ChatRoomActivity.this)
                        .setMessage("Coming Soon!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
