package com.martin.androidlame;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.martin.androidlame.support.AudioRecordHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int IDLE = 0;
    private static final int RECORDING = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.btn_record)
    ImageView mBtnRecord;

    @InjectView(R.id.et_path)
    EditText mPath;

    private int status = IDLE;

    private AudioRecordHandler mAudioRecordInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.btn_record)
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
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        Log.i(TAG, "i =" + permissionCheck);
        //开始录音
        mAudioRecordInstance = new AudioRecordHandler("/sdcard/hello.mp3",
                new AudioRecordHandler.ProgressListener() {
                    @Override
                    public boolean reportProgress(double duration) {
                        Log.i(TAG, "duration = " + duration);
                        return true;
                    }
                });
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
}
