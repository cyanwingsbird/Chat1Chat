package net.cyanwingsbird.chat1chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.cyanwingsbird.chat1chat.adapter.PendingListAdapter;
import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.networking.APIStatus;
import net.cyanwingsbird.chat1chat.networking.RetrofitClient;
import net.cyanwingsbird.chat1chat.networking.StatusUtils;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFdActivity extends AppCompatActivity {

    @Bind(R.id.pendingListView)
    ListView pendingListView;

    MainLoadingDialog loadingDialog;

    PendingListAdapter pendingListAdapter;
    ArrayList<Friend> pendingArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_fd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(PendingFdActivity.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pendingListAdapter = new PendingListAdapter(this, pendingArrayList);
        pendingListView.setAdapter(pendingListAdapter);

        loadingDialog = new MainLoadingDialog(this);

        pendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = Global.getLoginInfo().getUsername();
                String password = Global.getLoginInfo().getPassword();
                String target_id = pendingArrayList.get(position).getUserID();

                loadingDialog.show();
                RetrofitClient retrofitClient = new RetrofitClient();
                Call<APIStatus> call = retrofitClient.acceptPendingFd(username, password, target_id);
                call.enqueue(new Callback<APIStatus>() {
                    @Override
                    public void onResponse(Call<APIStatus> call, Response<APIStatus> response) {
                        loadingDialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.code() == 202) {
                                if(response.body()==null) {
                                    Toast.makeText(PendingFdActivity.this, "Connection error !!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Dialog dialog = new AlertDialog.Builder(PendingFdActivity.this)
                                            .setMessage("Request success !!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    getFriendList();
                                                }
                                            })
                                            .create();
                                    dialog.show();
                                }
                            }
                        } else {
                            APIStatus error = StatusUtils.parseError(response);
                            Toast.makeText(PendingFdActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<APIStatus> call, Throwable t) {
                        loadingDialog.dismiss();
                        Toast.makeText(PendingFdActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });


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
        Call<ArrayList<Friend>> call = retrofitClient.getPendingList(username, password);
        call.enqueue(new Callback<ArrayList<Friend>>() {
            @Override
            public void onResponse(Call<ArrayList<Friend>> call, Response<ArrayList<Friend>> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 202) {
                        if(response.body()==null) {
                            Toast.makeText(PendingFdActivity.this, "Your pending list is empty", Toast.LENGTH_SHORT).show();
                        }else {
                            pendingArrayList.clear();
                            pendingArrayList.addAll(response.body());
                            pendingListAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    APIStatus error = StatusUtils.parseError(response);
                    Toast.makeText(PendingFdActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Friend>> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(PendingFdActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
