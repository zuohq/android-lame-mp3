package com.martin.androidlame.support;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.martin.library.Mp3Encoder;

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
    private Mp3Encoder mp3Encoder;

    private AudioRecord audioRecord;

    private String filePath;

    private FileOutputStream outputStream;

    private boolean isRecording;

    private Object mutex = new Object();

    public AudioRecordHandler(String filePath, Mp3Encoder mp3Encoder) {
        this.filePath = filePath;
        this.mp3Encoder = mp3Encoder;
    }

    @Override
    public void run() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLERATEINHZ, CHANNELCONFIG, AUDIOFORMAT);

        try {
            outputStream = new FileOutputStream(new File(filePath));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        audioRecord = new AudioRecord(AUDIOSOURCE, SAMPLERATEINHZ, CHANNELCONFIG, AUDIOFORMAT, bufferSize);
        //5 seconds data
        short[] buffer = new short[SAMPLERATEINHZ * 2 * 5];

        // 'mp3buf' should be at least 7200 bytes long to hold all possible emitted data.
        byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];


        audioRecord.startRecording();

        int bufferRead = 0;

        while (isRecording) {
            bufferRead = audioRecord.read(buffer, 0, bufferSize);
            Log.d(TAG, "bytes read=" + bufferRead);

            if (bufferRead > 0) {

                Log.d(TAG, "encoding bytes to mp3 buffer..");
                int bytesEncoded = mp3Encoder.encode(buffer, buffer, bufferRead, mp3buffer);
                Log.d(TAG, "bytes encoded=" + bytesEncoded);

                if (bytesEncoded > 0) {
                    try {
                        Log.d(TAG, "writing mp3 buffer to outputstream with " + bytesEncoded + " bytes");
                        outputStream.write(mp3buffer, 0, bytesEncoded);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        int outputMp3buf = mp3Encoder.flush(mp3buffer);

        if (outputMp3buf > 0) {
            try {
                Log.d(TAG, "writing final mp3buffer to outputstream");
                outputStream.write(mp3buffer, 0, outputMp3buf);
                Log.d(TAG, "closing output stream");
                outputStream.close();
                Log.d(TAG, "Output recording saved in " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        audioRecord.stop();
        audioRecord.release();

        mp3Encoder.close();

        isRecording = false;
    }

    public void setRecording(boolean isRec) {
        synchronized (mutex) {
            this.isRecording = isRec;
        }
    }
}
