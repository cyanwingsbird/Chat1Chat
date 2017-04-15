package net.cyanwingsbird.chat1chat;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.cyanwingsbird.chat1chat.networking.APIStatus;
import net.cyanwingsbird.chat1chat.networking.RetrofitClient;
import net.cyanwingsbird.chat1chat.networking.StatusUtils;
import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;
import net.cyanwingsbird.chat1chat.userAccount.LoginInfo;
import net.cyanwingsbird.chat1chat.userAccount.UserAccountManager;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;
import net.cyanwingsbird.chat1chat.utility.PictureConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSettingActivity extends AppCompatActivity {

    @Bind(R.id.icon_preview)
    ImageView icon_preview;
    @Bind(R.id.select_image_button)
    ImageView select_image_button;
    @Bind(R.id.name_entering_editText)
    EditText name_entering_editText;
    @Bind(R.id.finish_button)
    Button finish_button;

    private final static int CAMERA = 0;
    private final static int PHOTO = 1;

    String username;
    String password;
    String display_name;

    MainLoadingDialog loadingDialog = new MainLoadingDialog(this);

    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadingDialog = new MainLoadingDialog(this);
        ButterKnife.bind(ProfileSettingActivity.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Global.getAccountInfo().getProfilePic() != null) {
            String profilePicURL = Global.getServerURL() + Global.getAccountInfo().getProfilePic().substring(2);
            Picasso.with(getApplicationContext()).invalidate(profilePicURL);
            Picasso.with(getApplicationContext())
                    .load(profilePicURL)
                    .placeholder(R.drawable.ic_account_circle_50dp)
                    .into(icon_preview);
        }
        if (Global.getAccountInfo().getDisplayName() != null) {
            name_entering_editText.setText(Global.getAccountInfo().getDisplayName());
        }

        select_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(ProfileSettingActivity.this)
                        .setMessage("Please choose a method to upload")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues value = new ContentValues();
                                value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
                                startActivityForResult(intent, CAMERA);
                            }
                        })
                        .setNegativeButton("Albums", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setType("image/jpeg");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, PHOTO);
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();

                final LoginInfo loginInfo = UserAccountManager.getLogin_info();
                username = loginInfo.getUsername();
                password = loginInfo.getPassword();

                RetrofitClient retrofitClient = new RetrofitClient();
                Call<APIStatus> call;
                File file_pic = PictureConverter.bitmapToFile(bitmap);
                if (bitmap != null) {
                    call = retrofitClient.changeProfile(username, password, name_entering_editText.getText().toString(), file_pic);
                } else {
                    call = retrofitClient.changeProfile(username, password, name_entering_editText.getText().toString(),null);
                }
                call.enqueue(new Callback<APIStatus>() {
                    @Override
                    public void onResponse(Call<APIStatus> call, Response<APIStatus> response) {
                        loadingDialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.code() == 202) {
                                if (response.body() == null) {
                                    Toast.makeText(ProfileSettingActivity.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfileSettingActivity.this, "Profile changing success !!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            APIStatus error = StatusUtils.parseError(response);
                            Toast.makeText(ProfileSettingActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIStatus> call, Throwable t) {
                        loadingDialog.dismiss();
                        Toast.makeText(ProfileSettingActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });

                String profilePicURL = Global.getServerURL() + Global.getAccountInfo().getProfilePic().substring(2);
                Picasso.with(getApplicationContext()).invalidate(profilePicURL);
                Global.getAccountInfo().setDisplayName(name_entering_editText.getText().toString());

                finish_button.setEnabled(false);
                finish_button.setAlpha(0.5f);
                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish_button.setEnabled(true);
                                finish_button.setAlpha(1f);
                            }
                        });
                    }
                }, 5000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if ((requestCode == CAMERA || requestCode == PHOTO) && data != null) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    if (requestCode == PHOTO) {
                        bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(cr.openInputStream(uri)), 300, 300);
                    } else {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    }
                    icon_preview.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Photo getting error!!", Toast.LENGTH_LONG).show();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
