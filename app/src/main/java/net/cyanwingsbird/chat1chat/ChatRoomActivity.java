package net.cyanwingsbird.chat1chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.cyanwingsbird.chat1chat.adapter.MessageAdapter;
import net.cyanwingsbird.chat1chat.dataset.Message;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ChatRoomActivity extends AppCompatActivity {

    MainLoadingDialog loadingDialog;

    String target_id;
    String target_name;

    @Bind(R.id.send_button)
    Button send_button;
    @Bind(R.id.texting_editText)
    EditText texting_editText;
    @Bind(R.id.message_view)
    ListView message_view;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(ChatRoomActivity.this);

        target_id = getIntent().getStringExtra("target_id");
        target_name = getIntent().getStringExtra("target_name");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(target_name);

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
}
