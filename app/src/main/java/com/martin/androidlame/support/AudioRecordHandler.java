package com.martin.androidlame.support;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.martin.library.Encoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description:　录音
 * @author: Created by martin on 16-3-22.
 */
public class AudioRecordHandler implements Runnable {

    private static final String TAG = AudioRecordHandler.class.getSimpleName();

    /****
     * 采样率
     */
    private static final int SAMPLERATEINHZ = 44100;

    /****
     * 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
     */
    private static final int CHANNELCONFIG = AudioFormat.CHANNEL_IN_MONO;

    /***
     * 音频数据格式:PCM 16位每个样本
     */
    private static final int AUDIOFORMAT = AudioFormat.ENCODING_PCM_16BIT;
    /***
     * 音频源：指的是从哪里采集音频。这里我们当然是从麦克风采集音频，所以此参数的值为MIC
     */
    private static final int AUDIOSOURCE = MediaRecorder.AudioSource.MIC;
    private Encoder encoder;

    private AudioRecord audioRecord;

    private String filePath;

    private FileOutputStream outputStream;

    private Object mutex = new Object();

    //是否暂停
    private boolean paused;
    //是否正在录音
    private boolean isRecording;

    private int recordTime;

    public AudioRecordHandler(String filePath, ProgressListener progressListener) {
        this.filePath = filePath;
        this.mProgressListener = progressListener;
        this.encoder = new Encoder.Builder(SAMPLERATEINHZ, CHANNELCONFIG, SAMPLERATEINHZ, 16).create();
    }


    @Override
    public void run() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLERATEINHZ, CHANNELCONFIG, AUDIOFORMAT);

        try {
            outputStream = new FileOutputStream(new File(filePath));
            audioRecord = new AudioRecord(AUDIOSOURCE, SAMPLERATEINHZ, CHANNELCONFIG, AUDIOFORMAT, bufferSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //todo:通知录音失败
            return;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            //todo:通知录音失败
            releaseAndCloseStream();
            return;
        }

        short[] buffer = new short[SAMPLERATEINHZ * 2 * 5];
        byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

        audioRecord.startRecording();

        int bufferRead = 0;
        int duration = 0;

        while (isRecording) {

            while (isPaused()) {
                try {
                    if (!isRecording)
                        break;
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            bufferRead = audioRecord.read(buffer, 0, bufferSize);
            recordTime = duration / SAMPLERATEINHZ;

            Log.i(TAG, "record time =" + recordTime);

            if (isRecording && !mProgressListener.reportProgress(recordTime)) {
                break;
            }

            if (bufferRead > 0) {
                duration += bufferRead;

                Log.d(TAG, "encoding bytes to mp3 buffer......duration=" + duration);
                int bytesEncoded = encoder.encode(buffer, buffer, bufferRead, mp3buffer);

                if (bytesEncoded > 0) {
                    try {
                        outputStream.write(mp3buffer, 0, bytesEncoded);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                releaseAndCloseStream();
                //todo:通知录音失败
                return;
            }
        }

        int outputMp3buf = encoder.flush(mp3buffer);

        if (outputMp3buf > 0) {
            try {
                outputStream.write(mp3buffer, 0, outputMp3buf);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        release();
    }

    private void releaseAndCloseStream() {
        try {
            if (outputStream != null)
                outputStream.close();
            release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void release() {
        audioRecord.stop();
        audioRecord.release();

        encoder.close();

        isRecording = false;
        audioRecord = null;
    }

    public void setPaused(boolean paused) {
        synchronized (mutex) {
            this.paused = paused;
        }
    }

    public boolean isPaused() {
        synchronized (mutex) {
            return paused;
        }
    }

    public void setRecording(boolean isRec) {
        synchronized (mutex) {
            this.isRecording = isRec;
        }
    }

    public boolean isRecording() {
        synchronized (mutex) {
            return isRecording;
        }
    }

    private ProgressListener mProgressListener;

    public int getRecordTime() {
        return recordTime;
    }

    // Progress listener interface.
    public interface ProgressListener {

        boolean reportProgress(double fractionComplete);
    }
}
