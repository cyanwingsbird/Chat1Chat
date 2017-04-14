package net.cyanwingsbird.chat1chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.cyanwingsbird.chat1chat.networking.APIStatus;
import net.cyanwingsbird.chat1chat.networking.RetrofitClient;
import net.cyanwingsbird.chat1chat.networking.StatusUtils;
import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;
import net.cyanwingsbird.chat1chat.userAccount.LoginInfo;
import net.cyanwingsbird.chat1chat.userAccount.UserAccountManager;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFdActivity extends AppCompatActivity {

    @Bind(R.id.fd_userID_editText)
    EditText fd_userID_editText;

    @Bind(R.id.add_friend_button)
    Button add_friend_button;

    @Bind(R.id.myUserIdText)
    TextView myUserIdText;

    MainLoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fd);
        ButterKnife.bind(AddFdActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadingDialog = new MainLoadingDialog(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myUserIdText.setText("Your User id: " + Global.getAccountInfo().getUserID());

        add_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Global.getLoginInfo().getUsername();
                String password = Global.getLoginInfo().getPassword();
                String fdUserId = fd_userID_editText.getText().toString();

                loadingDialog.show();

                RetrofitClient retrofitClient = new RetrofitClient();
                Call<APIStatus> call = retrofitClient.addFd(username, password, fdUserId);
                call.enqueue(new Callback<APIStatus>() {
                    @Override
                    public void onResponse(Call<APIStatus> call, Response<APIStatus> response) {
                        loadingDialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.code() == 202) {
                                if(response.body()==null) {
                                    Toast.makeText(AddFdActivity.this, "User ID not exist !", Toast.LENGTH_SHORT).show();
                                }else {
                                    Dialog dialog = new AlertDialog.Builder(AddFdActivity.this)
                                            .setMessage("Request success !")
                                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .create();
                                    dialog.show();


                                }
                            }
                        } else {
                            APIStatus error = StatusUtils.parseError(response);
                            Toast.makeText(AddFdActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<APIStatus> call, Throwable t) {
                        loadingDialog.dismiss();
                        Toast.makeText(AddFdActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
            }
        });


    }
}
