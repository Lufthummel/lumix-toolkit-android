package org.secretwpn.lumixtoolkit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.secretwpn.lumixtoolkit.net.VideoFeed;
import org.secretwpn.lumixtoolkit.ui.StreamDisplay;
import org.secretwpn.lumixtoolkit.util.NetUtils;
import org.secretwpn.lumixtoolkit.util.XmlUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StreamDisplayActivity extends AppCompatActivity {
    private ScheduledExecutorService executor;
    private VideoFeed videoFeed;
    private StreamDisplay display;
    private FloatingActionButton recButton;
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar, notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stream_display);

        display = (StreamDisplay) findViewById(R.id.stream_display);
        recButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        executor = Executors.newSingleThreadScheduledExecutor();
        videoFeed = new VideoFeed(display);
        executor.scheduleAtFixedRate(() -> NetUtils.makeRequest(NetUtils.apiUrl("getstate"), result -> XmlUtils.parseState(display, result)), 0, 10, TimeUnit.SECONDS);
        NetUtils.makeRequest(NetUtils.apiUrl("camcmd", "recmode"));
        NetUtils.makeRequest(NetUtils.apiUrl("startstream", NetUtils.serverPort));

        videoFeed.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onDestroy() {
        videoFeed.kill();
        executor.shutdown();
        super.onDestroy();
    }

    public void displayClicked(View view) {
        display.toggleZoomMode();
    }

    public void toggleRecording() {
        isRecording = !isRecording;
        recButton.setImageResource(isRecording ? R.drawable.ic_stop_black_24dp : R.drawable.ic_fiber_manual_record_black_24dp);
    }

    private void startRecording() {
        if ("gh3".equalsIgnoreCase(display.getState().getModel())) {
            NetUtils.makeRequest(NetUtils.apiUrl("stopstream"));
        }
        NetUtils.makeRequest(NetUtils.apiUrl("camcmd", "video_recstart"), result -> toggleRecording());
    }

    private void stopRecording() {
        if ("gh3".equalsIgnoreCase(display.getState().getModel())) {
            NetUtils.makeRequest(NetUtils.apiUrl("camcmd", "playmode"), result -> {
                toggleRecording();
                NetUtils.makeRequest(NetUtils.apiUrl("camcmd", "recmode"));
                NetUtils.makeRequest(NetUtils.apiUrl("startstream", NetUtils.serverPort));
            });
        } else {
            NetUtils.makeRequest(NetUtils.apiUrl("camcmd", "video_recstop"), result -> toggleRecording());
        }
    }

    public void recordClicked(View view) {
        if (isRecording)
            stopRecording();
        else
            startRecording();
    }
}
