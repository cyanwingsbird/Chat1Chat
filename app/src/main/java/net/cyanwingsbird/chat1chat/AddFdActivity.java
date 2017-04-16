package net.cyanwingsbird.chat1chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.networking.APIStatus;
import net.cyanwingsbird.chat1chat.networking.RetrofitClient;
import net.cyanwingsbird.chat1chat.networking.StatusUtils;
import net.cyanwingsbird.chat1chat.utility.MainLoadingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFdActivity extends AppCompatActivity {

    @Bind(R.id.fd_userID_editText)
    EditText fd_userID_editText;

    @Bind(R.id.add_friend_button)
    Button add_friend_button;

    @Bind(R.id.search_friend_button)
    Button search_friend_button;

    @Bind(R.id.myUserIdText)
    TextView myUserIdText;

    @Bind(R.id.imageView_fd)
    ImageView imageView_fd;

    @Bind(R.id.qr_friend_button)
    Button qr_friend_button;

    @Bind(R.id.qr_gen_button)
    Button qr_gen_button;

    @Bind(R.id.textView_friend_nickname)
    TextView textView_friend_nickname;


    MainLoadingDialog loadingDialog;

    Friend search_fd;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fd);
        ButterKnife.bind(AddFdActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadingDialog = new MainLoadingDialog(this);

        myUserIdText.setText("Your User id: " + Global.getAccountInfo().getUserID());

        qr_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(AddFdActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        qr_gen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(Global.getAccountInfo().getUserID().toString(), BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    showImage(bitmap);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
            }
        });

        add_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search_fd==null) {
                    Toast.makeText(AddFdActivity.this, "You should search your friend using the id first !", Toast.LENGTH_SHORT).show();
                }else {
                    String username = Global.getLoginInfo().getUsername();
                    String password = Global.getLoginInfo().getPassword();
                    String fdUserId = search_fd.getUserID();

                    loadingDialog.show();

                    RetrofitClient retrofitClient = new RetrofitClient();
                    Call<APIStatus> call = retrofitClient.addFd(username, password, fdUserId);
                    call.enqueue(new Callback<APIStatus>() {
                        @Override
                        public void onResponse(Call<APIStatus> call, Response<APIStatus> response) {
                            loadingDialog.dismiss();
                            if (response.isSuccessful()) {
                                if (response.code() == 202) {
                                    if (response.body() == null) {
                                        Toast.makeText(AddFdActivity.this, "User ID not exist !", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddFdActivity.this, "Request success !", Toast.LENGTH_SHORT).show();
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
            }
        });
        search_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Global.getLoginInfo().getUsername();
                String password = Global.getLoginInfo().getPassword();
                String fdUserId = fd_userID_editText.getText().toString();

                loadingDialog.show();

                RetrofitClient retrofitClient = new RetrofitClient();
                Call<Friend> call = retrofitClient.getFd(username, password, fdUserId);
                call.enqueue(new Callback<Friend>() {
                    @Override
                    public void onResponse(Call<Friend> call, Response<Friend> response) {
                        loadingDialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.code() == 202) {
                                if(response.body()==null) {
                                    Toast.makeText(AddFdActivity.this, "User ID not exist !", Toast.LENGTH_SHORT).show();
                                }else {
                                    search_fd = response.body();
                                    if(search_fd.getProfilePic()!=null) {
                                        Picasso.with(AddFdActivity.this)
                                                .load(Global.getServerURL() + search_fd.getProfilePic().substring(2))
                                                .placeholder(R.drawable.ic_account_circle_blue_24dp)
                                                .into(imageView_fd);
                                    }
                                    textView_friend_nickname.setVisibility(View.VISIBLE);
                                    textView_friend_nickname.setText(search_fd.getDisplayName());
                                }
                            }
                        } else {
                            APIStatus error = StatusUtils.parseError(response);
                            Toast.makeText(AddFdActivity.this, "Error code: " + error.status() + ": " + error.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Friend> call, Throwable t) {
                        loadingDialog.dismiss();
                        Toast.makeText(AddFdActivity.this, "Network Connection fail !!", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
            }
        });


    }

    public void showImage(Bitmap bitmap) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                600,600));
        builder.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                fd_userID_editText.setText(result.getContents());
                search_friend_button.performClick();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
