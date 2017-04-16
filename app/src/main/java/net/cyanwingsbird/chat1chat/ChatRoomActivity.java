package net.cyanwingsbird.chat1chat;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.cyanwingsbird.chat1chat.adapter.MessageAdapter;
import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.dataset.Message;
import net.cyanwingsbird.chat1chat.networking.APIStatus;
import net.cyanwingsbird.chat1chat.networking.RetrofitClient;
import net.cyanwingsbird.chat1chat.networking.StatusUtils;
import net.cyanwingsbird.chat1chat.utility.GPSTracker;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;
import net.cyanwingsbird.chat1chat.utility.PictureConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private final static int AUDIO = 2;
    private final static int VIDEO = 3;


    MainLoadingDialog loadingDialog;
    Toolbar toolbar;
    Bitmap bitmap = null;
    Uri audio_uri;
    Uri videoUri;

    private ClipboardManager myClipboard;
    private ClipData myClip;
    private MediaPlayer mp;

    Boolean isLoopingReceive = true;

    String target_id;
    String target_name;
    String target_pic;
    String username;
    String password;

    @Bind(R.id.send_button)
    ImageView send_button;
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadingDialog = new MainLoadingDialog(this);

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        target_id = getIntent().getStringExtra("target_id");
        target_name = getIntent().getStringExtra("target_name");
        target_pic = getIntent().getStringExtra("target_pic");

        friend_nickname.setText(target_name);

        username = Global.getLoginInfo().getUsername();
        password = Global.getLoginInfo().getPassword();

        if (target_pic != null) {
            String friendPictureUrl = Global.getServerURL() + target_pic.substring(2);
            Picasso.with(getApplicationContext())
                    .load(friendPictureUrl)
                    .placeholder(R.drawable.ic_account_circle_50dp)
                    .into(profile_pic);
        }

        messageAdapter = new MessageAdapter(ChatRoomActivity.this, messages);
        message_view.setAdapter(messageAdapter);

        getMessageList();


        new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(1500);
                    } catch (InterruptedException e) {
                    } finally {
                        if (isLoopingReceive) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loopingRenewMessage();
                                }
                            });
                        }
                    }
                }
            }
        }.start();

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

        texting_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try {
                            sleep(300);
                        } catch (InterruptedException e) {
                        } finally {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    message_view.smoothScrollToPosition(messages.size());
                                }
                            });
                        }
                    }
                }.start();
            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message current_message = new Message();
                current_message.setFromUserID(Global.getAccountInfo().getUserID());
                current_message.setMessageContent(texting_editText.getText().toString());
                current_message.setMessageType("1");
                current_message.setToUserID(target_id);
                messages.add(current_message);

                String username = Global.getLoginInfo().getUsername();
                String password = Global.getLoginInfo().getPassword();

                RetrofitClient retrofitClient = new RetrofitClient();
                Call<APIStatus> call = retrofitClient.sendMsg(username, password, target_id, "1", texting_editText.getText().toString(), null);
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
                messageAdapter.notifyDataSetChanged();
                message_view.smoothScrollToPosition(messages.size());

            }
        });

        message_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message current_message = messages.get(position);
                Intent intent;
                if (current_message.getMessageType().equals("2")) {
                    //Image
                    intent = new Intent();
                    intent.setClass(ChatRoomActivity.this, ImageViewActivity.class);
                    intent.putExtra("imageURL", Global.getServerURL() + current_message.getMessageContent().substring(2));
                    startActivity(intent);
                } else if (current_message.getMessageType().equals("3")) {
                    //Video
                    intent = new Intent();
                    intent.setClass(ChatRoomActivity.this, VideoViewActivity.class);
                    intent.putExtra("videoURL", Global.getServerURL() + current_message.getMessageContent().substring(2));
                    startActivity(intent);
                } else if (current_message.getMessageType().equals("4")) {
                    //Location
                    intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?q="+current_message.getMessageContent()));
                    startActivity(intent);
                } else if (current_message.getMessageType().equals("5")) {
                    //Audio
                    loadingDialog.show();
                    if (audio_uri != null) {
                        if (mp != null) {
                            mp.release();
                            mp = null;
                            loadingDialog.dismiss();
                        }
                        if (mp == null) {
                            try {
                                mp = new MediaPlayer();
                                mp.setDataSource(audio_uri.toString());

                                mp.prepare();
                                mp.start();
                                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mp.release();
                                    }
                                });
                                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        loadingDialog.dismiss();
                                    }
                                });
                                mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                        loadingDialog.dismiss();
                                        Toast.makeText(ChatRoomActivity.this, "Network Connection Fail !!", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                                loadingDialog.dismiss();
                            }
                            loadingDialog.dismiss();
                        }
                    }
                    loadingDialog.dismiss();
                } else {
                    //Text
                    String text = current_message.getMessageContent();
                    myClip = ClipData.newPlainText("text", text);
                    myClipboard.setPrimaryClip(myClip);
                    Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
                }
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
                intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), AUDIO);
                return true;
            case R.id.menu_send_video:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10); //15 sec
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 52428800L); //50MB
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, VIDEO);
                } else {
                    Toast.makeText(ChatRoomActivity.this, "There are no camera app in your phone !!", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.menu_send_location:
                GPSTracker gps = new GPSTracker(ChatRoomActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    final double latitude = gps.getLatitude();
                    final double longitude = gps.getLongitude();

                    dialog = new AlertDialog.Builder(ChatRoomActivity.this)
                            .setMessage("Your Location is: \nLat:" + latitude + "\nLong: " + longitude + "\nAre you sure to send the location?")
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

                                    Call<APIStatus> call = retrofitClient.sendMsg(username, password, target_id, "4", latitude + "," + longitude, null);
                                    call.enqueue(sending_callback);
                                }
                            })
                            .create();
                    dialog.show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Dialog dialog;
        if (resultCode != RESULT_CANCELED) {
            // Image
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
                            .setMessage("Are you sure to send the image?")
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
                                    File file_pic = PictureConverter.bitmapToFile(getApplicationContext().getFilesDir(),bitmap);
                                    if (bitmap == null) {
                                        Toast.makeText(ChatRoomActivity.this, "File loading error", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Call<APIStatus> call = retrofitClient.sendMsg(username, password, target_id, "2", "", file_pic);
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

            //Audio
            if (requestCode == AUDIO && data != null) {
                if (resultCode == RESULT_OK) {
                    audio_uri = data.getData();
                    dialog = new AlertDialog.Builder(ChatRoomActivity.this)
                            .setMessage("Are you sure to send the audio?")
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

                                    File audio_file = new File(getRealPathFromURI(audio_uri));
                                    if (audio_file == null) {
                                        Toast.makeText(ChatRoomActivity.this, "File loading error", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Call<APIStatus> call = retrofitClient.sendMsg(username, password, target_id, "5", "", audio_file);
                                        call.enqueue(sending_callback);
                                    }
                                }
                            })
                            .create();
                    dialog.show();
                }
            }

            //Video
            if (requestCode == VIDEO && resultCode == RESULT_OK && data != null) {
                videoUri = data.getData();

                dialog = new AlertDialog.Builder(ChatRoomActivity.this)
                        .setMessage("Are you sure to send the video?")
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

                                File video_file = new File(getRealPathFromURI(videoUri));
                                if (video_file == null) {
                                    Toast.makeText(ChatRoomActivity.this, "File loading error", Toast.LENGTH_SHORT).show();
                                } else {
                                    Call<APIStatus> call = retrofitClient.sendMsg(username, password, target_id, "3", "", video_file);
                                    call.enqueue(sending_callback);
                                }
                            }
                        })
                        .create();
                dialog.show();
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getMessageList() {
        loadingDialog.show();
        String username = Global.getLoginInfo().getUsername();
        String password = Global.getLoginInfo().getPassword();

        RetrofitClient retrofitClient = new RetrofitClient();
        Call<ArrayList<Message>> call = retrofitClient.getMsg(username, password, target_id);
        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 202) {
                        if (response.body() == null) {
                            Toast.makeText(ChatRoomActivity.this, "Say something with your friend ^_^", Toast.LENGTH_SHORT).show();
                        } else {
                            messages.clear();
                            messages.addAll(response.body());
                            messageAdapter.notifyDataSetChanged();
                            message_view.smoothScrollToPosition(messages.size());
                        }
                    }
                } else {
                    APIStatus error = StatusUtils.parseError(response);
                    Toast.makeText(ChatRoomActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(ChatRoomActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });


    }

    public void loopingRenewMessage() {
        String username = Global.getLoginInfo().getUsername();
        String password = Global.getLoginInfo().getPassword();

        RetrofitClient retrofitClient = new RetrofitClient();
        Call<ArrayList<Message>> call = retrofitClient.getMsg(username, password, target_id);
        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 202) {
                        if (response.body() == null) {
                        } else {
                            if (response.body().size() > messages.size()) {
                                messages.clear();
                                messages.addAll(response.body());
                                messageAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } else {
                    APIStatus error = StatusUtils.parseError(response);
                    Toast.makeText(ChatRoomActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(ChatRoomActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public String getTarget_name() {
        return target_name;
    }

    public String getTarget_profile() {
        if (target_pic != null) {
            return Global.getServerURL() + target_pic.substring(2);
        } else {
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoopingReceive = true;

    }
    @Override
    protected void onPause() {
        super.onPause();
        isLoopingReceive = false;
        if (mp != null) {
            mp.release();
            mp = null;
        };
    }
}
