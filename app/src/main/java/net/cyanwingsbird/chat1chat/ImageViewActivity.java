package net.cyanwingsbird.chat1chat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andy on 20/7/2016.
 */
public class ImageViewActivity extends AppCompatActivity {

    static final String TAG ="ImageViewActivity";
    @Bind(R.id.imageViewer)
    ImageView imageViewer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(ImageViewActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String imageURL = getIntent().getStringExtra("imageURL");
        Picasso.with(this)
                .load(imageURL)
                .placeholder(R.drawable.ic_sync_black_24dp)
                .into(imageViewer);
    }
}
