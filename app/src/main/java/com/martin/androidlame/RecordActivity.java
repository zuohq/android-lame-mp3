package com.martin.androidlame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.martin.androidlame.support.AudioRecordHandler;
import com.martin.library.Encoder;

import butterknife.ButterKnife;
import butterknife.InjectView;

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




        mBtnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }




    private void doActionRecord() {
//        final Encoder encoder = new LameBuilder()
//                .setInSampleRate(44100)
//                .setOutChannels(1)
//                .setOutBitrate(16)
//                .setOutSampleRate(44100)
//                .build();



        //开始录音
        mAudioRecordInstance = new AudioRecordHandler("/sdcard/hello.mp3");
        Thread th = new Thread(mAudioRecordInstance);
        th.start();
        mAudioRecordInstance.setRecording(true);
    }

    private void doActionStopRecord() {
        if (mAudioRecordInstance != null) {
            mAudioRecordInstance.setRecording(false);
        }
    }

//    static {
//        System.loadLibrary("mp3encoder");
//    }

}
