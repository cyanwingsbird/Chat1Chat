package net.cyanwingsbird.chat1chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import net.cyanwingsbird.chat1chat.adapter.FriendListAdapter;
import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.dataset.FriendList;

import java.util.ArrayList;

import butterknife.Bind;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.friendListView)
    ListView friendListView;

    FriendListAdapter friendListAdapter;
    ArrayList<Friend> friendArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public void onResume(){
        super.onResume();
        getFriendList();

    }

    public void getFriendList() {
      //  try {
            String username = Global.getLoginInfo().getUsername();
            String password = Global.getLoginInfo().getPassword();
            /*
            GetFriendListHandler handler = new GetFriendListHandler(MainActivity.this);
            RequestBody body = new FormBody.Builder()
                    .add("ac", username)
                    .add("pw", password)
                    .add("aes_key", encrypted_key)
                    .build();
            handler.execute(body);

        } catch (Exception e) {
            Log.i(TAG, "Exception: " + e);
        }

        */


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog dialog;
        switch (item.getItemId()) {
            case R.id.menu_add_fd:
                Intent intent = new Intent(MainActivity.this, AddFdActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_show_pending:
                dialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Coming Soon")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                return true;
            case R.id.menu_setting:
                dialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Coming Soon")
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
