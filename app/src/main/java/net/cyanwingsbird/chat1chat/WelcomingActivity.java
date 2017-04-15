package net.cyanwingsbird.chat1chat;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import net.cyanwingsbird.chat1chat.networking.APIStatus;
import net.cyanwingsbird.chat1chat.networking.RetrofitClient;
import net.cyanwingsbird.chat1chat.networking.StatusUtils;
import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;
import net.cyanwingsbird.chat1chat.userAccount.LoginInfo;
import net.cyanwingsbird.chat1chat.userAccount.UserAccountManager;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomingActivity extends AppCompatActivity {

    MainLoadingDialog loadingDialog;
    LoginInfo loginInfo;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcoming);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.Black));
        }

        loadingDialog = new MainLoadingDialog(this);

        new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loginInfo = UserAccountManager.getLogin_info();
                Global.setLoginInfo(loginInfo);
                loadingDialog.show();
                if (loginInfo == null) {
                    new Thread() {
                        public void run() {
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            loadingDialog.dismiss();
                            Intent intent = new Intent();
                            intent.setClass(WelcomingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }.start();
                } else {
                    username = loginInfo.getUsername();
                    password = loginInfo.getPassword();

                    RetrofitClient retrofitClient = new RetrofitClient();
                    Call<AccountInfo> call = retrofitClient.login(username, password);
                    call.enqueue(new Callback<AccountInfo>() {
                        @Override
                        public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                            loadingDialog.dismiss();
                            if (response.isSuccessful()) {
                                if (response.code() == 202) {
                                    if (response.body() == null) {
                                        Toast.makeText(WelcomingActivity.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Global.setLoginInfo(new LoginInfo(username, password));
                                        Global.setAccountInfo(response.body());
                                        Toast.makeText(WelcomingActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(WelcomingActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                APIStatus error = StatusUtils.parseError(response);
                                Toast.makeText(WelcomingActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<AccountInfo> call, Throwable t) {
                            loadingDialog.dismiss();
                            Toast.makeText(WelcomingActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });
                }
            }
        }.start();

    }


}
