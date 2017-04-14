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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.cyanwingsbird.chat1chat.adapter.FriendListAdapter;
import net.cyanwingsbird.chat1chat.adapter.PendingListAdapter;
import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.dataset.FriendList;
import net.cyanwingsbird.chat1chat.networking.APIStatus;
import net.cyanwingsbird.chat1chat.networking.RetrofitClient;
import net.cyanwingsbird.chat1chat.networking.StatusUtils;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.friendListView)
    ListView friendListView;

    MainLoadingDialog loadingDialog;

    FriendListAdapter friendListAdapter;
    ArrayList<Friend> friendArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(MainActivity.this);

        friendListAdapter = new FriendListAdapter(this, friendArrayList);
        friendListView.setAdapter(friendListAdapter);

        loadingDialog = new MainLoadingDialog(this);

        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
                intent.putExtra("target_id", friendArrayList.get(position).getUserID());
                intent.putExtra("target_name", friendArrayList.get(position).getDisplayName());
                startActivity(intent);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        getFriendList();

    }

    public void getFriendList() {
        String username = Global.getLoginInfo().getUsername();
        String password = Global.getLoginInfo().getPassword();

        loadingDialog.show();

        RetrofitClient retrofitClient = new RetrofitClient();
        Call<ArrayList<Friend>> call = retrofitClient.getFdList(username, password);
        call.enqueue(new Callback<ArrayList<Friend>>() {
            @Override
            public void onResponse(Call<ArrayList<Friend>> call, Response<ArrayList<Friend>> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 202) {
                        if(response.body()==null) {
                            Toast.makeText(MainActivity.this, "You should add a new friend before chatting", Toast.LENGTH_SHORT).show();
                        }else {

                            friendArrayList.clear();
                            friendArrayList.addAll(response.body());
                            friendListAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    APIStatus error = StatusUtils.parseError(response);
                    Toast.makeText(MainActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Friend>> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(MainActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog dialog;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_add_fd:
                intent = new Intent(MainActivity.this, AddFdActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_show_pending:
                intent = new Intent(MainActivity.this, PendingFdActivity.class);
                startActivity(intent);
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
