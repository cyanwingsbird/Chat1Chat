package net.cyanwingsbird.chat1chat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andy on 20/7/2016.
 */
public class VideoViewActivity extends AppCompatActivity {

    static final String TAG ="VideoViewActivity";
    @Bind(R.id.videoViewer)
    VideoView videoViewer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        ButterKnife.bind(VideoViewActivity.this);

        MediaController mc = new MediaController(VideoViewActivity.this);
        videoViewer.setMediaController(mc);
        String videoURL = getIntent().getStringExtra("videoURL");
        Uri uri = Uri.parse(videoURL);
        videoViewer.setVideoURI(uri);
        videoViewer.requestFocus();
        videoViewer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoViewer.start();
            }
        });
        videoViewer.start();
    }
}
