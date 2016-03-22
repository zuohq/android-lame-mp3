package com.martin.androidlame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.martin.androidlame.support.AudioRecordHandler;
import com.martin.library.LameBuilder;
import com.martin.library.Mp3Encoder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/***
 * 录音
 */
public class RecordActivity extends AppCompatActivity {

    private static final int IDLE = 0;
    private static final int RECORDING = 1;

    @InjectView(R.id.btn_start)
    ImageView mBtnRecord;

    private AudioRecordHandler mAudioRecordInstance;

    private int status = IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ButterKnife.inject(this);
    }


    @OnClick(R.id.btn_start)
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
        final Mp3Encoder encoder = new LameBuilder()
                .setInSampleRate(44100)
                .setOutChannels(1)
                .setOutBitrate(16)
                .setOutSampleRate(44100)
                .build();

        //开始录音
        mAudioRecordInstance = new AudioRecordHandler("/sdcard/hello.mp3", encoder);
        Thread th = new Thread(mAudioRecordInstance);
        th.start();
        mAudioRecordInstance.setRecording(true);
    }

    private void doActionStopRecord() {
        if (mAudioRecordInstance != null) {
            mAudioRecordInstance.setRecording(false);
        }
    }

}
