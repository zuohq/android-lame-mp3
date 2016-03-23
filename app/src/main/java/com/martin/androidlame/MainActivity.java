package com.martin.androidlame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.martin.androidlame.support.AudioRecordHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int IDLE = 0;
    private static final int RECORDING = 1;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.fab)
    ImageView mBtnRecord;

    private int status = IDLE;

    private AudioRecordHandler mAudioRecordInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.fab)
    public void onRecordClick() {
        if (status == IDLE) {
            doActionRecord();
            status = RECORDING;
            mBtnRecord.setImageResource(R.mipmap.ic_record_pause);
        } else if (status == RECORDING) {
            doActionStopRecord();
            status = IDLE;
            mBtnRecord.setImageResource(R.mipmap.ic_record_start);
        }
    }

    private void doActionRecord() {
        //开始录音
        mAudioRecordInstance = new AudioRecordHandler("/sdcard/hello.mp3");
        Thread th = new Thread(mAudioRecordInstance);
        th.start();
        mAudioRecordInstance.setRecording(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioRecordInstance != null) {
            mAudioRecordInstance.setRecording(false);
        }
    }

    private void doActionStopRecord() {
        if (mAudioRecordInstance != null) {
            mAudioRecordInstance.setRecording(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
