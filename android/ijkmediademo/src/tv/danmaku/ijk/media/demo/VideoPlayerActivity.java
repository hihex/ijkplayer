package tv.danmaku.ijk.media.demo;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import tv.danmaku.ijk.media.widget.MediaController;
import tv.danmaku.ijk.media.widget.VideoView;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class VideoPlayerActivity extends Activity {
    private VideoView mVideoView;
    private View mBufferingIndicator;
    private MediaController mMediaController;

    private String mVideoPath;
    private static String serverApi="http://192.168.6.28:8080/string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mVideoPath = "http://k.youku.com/player/getFlvPath/sid/642644060724712db0d16_00/st/flv/fileid/03000201005127BB9CE50702758DA73D86DF01-8B2F-B60C-DB78-8065732929FA?K=53acd5f16935369e261e3b33&ctype=12&ev=1&oip=2043096855&token=8921&ep=dCaWHE2JVsgA4iHbjz8bM37iIHYJXP4J9h%2BFg9JjALshSeq460zYtOi1TvhCFP4celZ3F%2BiFo9aTHEAVYfgxqx4QrUioSvqUjPfr5aVTwpUHZRs%2FcMShxVSWSjLw";

        Intent intent = getIntent();
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction)
                && intentAction.equals(Intent.ACTION_VIEW)) {
            mVideoPath = intent.getDataString();
        }

        if (TextUtils.isEmpty(mVideoPath)) {
            mVideoPath = new File(Environment.getExternalStorageDirectory(),
                    "download/test.mp4").getAbsolutePath();
        }
        mBufferingIndicator = findViewById(R.id.buffering_indicator);
        mMediaController = new MediaController(this);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setMediaBufferingIndicator(mBufferingIndicator);
        mVideoView.requestFocus();
        syncServer();
    }

    private final void play() {
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.start();
    }

    private final void syncServer() {
        try {
            String JsonString = NetworkUtil.httpGet(serverApi);
            JSONObject jsonObject = new JSONObject(JsonString);
            mVideoPath = (jsonObject.getString("videoUrl"));
            Log.e("video", "get server data:contentUri=" + serverApi);
            updateEditText();
            play();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private final void updateEditText(){
        EditText inputUrl= (EditText) findViewById(R.id.inputUrl);
        if (inputUrl!=null){
            inputUrl.setText(serverApi.toString());
        }
    }
    public final void onClickGo(View v){
        EditText inputUrl= (EditText) findViewById(R.id.inputUrl);

        serverApi=inputUrl.getText().toString();
        syncServer();
    }

}
