package net.cyanwingsbird.chat1chat;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.cyanwingsbird.chat1chat.adapter.MessageAdapter;
import net.cyanwingsbird.chat1chat.dataset.Message;
import net.cyanwingsbird.chat1chat.networking.APIStatus;
import net.cyanwingsbird.chat1chat.networking.RetrofitClient;
import net.cyanwingsbird.chat1chat.networking.StatusUtils;
import net.cyanwingsbird.chat1chat.userAccount.LoginInfo;
import net.cyanwingsbird.chat1chat.userAccount.UserAccountManager;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;
import net.cyanwingsbird.chat1chat.utility.PictureConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity {

    private final static int CAMERA = 0;
    private final static int PHOTO = 1;


    MainLoadingDialog loadingDialog;
    Toolbar toolbar;
    Bitmap bitmap = null;

    String target_id;
    String target_name;
    String target_pic;
    String username;
    String password;

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
    ArrayList<Message> messages = new ArrayList<>();

    Callback<APIStatus> sending_callback;

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
        final LoginInfo loginInfo = UserAccountManager.getLogin_info();
        username = loginInfo.getUsername();
        password = loginInfo.getPassword();

        if(target_pic!=null) {
            String friendPictureUrl = Global.getServerURL() + target_pic.substring(2);
            Picasso.with(getApplicationContext())
                    .load(friendPictureUrl)
                    .placeholder(R.drawable.ic_account_circle_50dp)
                    .into(profile_pic);
        }

        loadingDialog = new MainLoadingDialog(this);

        sending_callback = new Callback<APIStatus>() {
            @Override
            public void onResponse(Call<APIStatus> call, Response<APIStatus> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 202) {
                        if (response.body() == null) {
                            Toast.makeText(ChatRoomActivity.this, "Server error !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChatRoomActivity.this, "Send success !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    APIStatus error = StatusUtils.parseError(response);
                    Toast.makeText(ChatRoomActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<APIStatus> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(ChatRoomActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        };


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

      //          Message current_message = new Message();
       //         current_message.setContent(texting_editText.getText().toString());
       //         current_message.setSender(Integer.parseInt(Global.getAccountInfo().getUserid()));

                String username = Global.getLoginInfo().getUsername();
                String password = Global.getLoginInfo().getPassword();

                RetrofitClient retrofitClient = new RetrofitClient();
                Call<APIStatus> call = retrofitClient.sendMsg(username, password, target_id, "1" , texting_editText.getText().toString(), null);
                call.enqueue(new Callback<APIStatus>() {
                    @Override
                    public void onResponse(Call<APIStatus> call, Response<APIStatus> response) {
                        loadingDialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.code() == 202) {
                                if (response.body() == null) {
                                    Toast.makeText(ChatRoomActivity.this, "Server error !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ChatRoomActivity.this, "Send success !!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            APIStatus error = StatusUtils.parseError(response);
                            Toast.makeText(ChatRoomActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<APIStatus> call, Throwable t) {
                        loadingDialog.dismiss();
                        Toast.makeText(ChatRoomActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });

                texting_editText.setText(null);
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                messageAdapter.notifyDataSetChanged();
//                message_view.smoothScrollToPosition(messages.size());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Dialog dialog;
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
                    dialog = new AlertDialog.Builder(ChatRoomActivity.this)
                            .setMessage("Are you sure to send?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loadingDialog.show();
                                    RetrofitClient retrofitClient = new RetrofitClient();
                                    File file_pic = PictureConverter.bitmapToFile(bitmap);
                                    if (bitmap == null) {
                                        Toast.makeText(ChatRoomActivity.this, "File loading error", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Call<APIStatus> call = retrofitClient.sendMsg(username, password, target_id , "2", "", file_pic);
                                        call.enqueue(sending_callback);
                                    }
                                }
                            })
                            .create();
                    dialog.show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Photo getting error!!", Toast.LENGTH_LONG).show();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
