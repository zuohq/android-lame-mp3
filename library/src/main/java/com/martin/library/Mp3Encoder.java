package com.martin.library;

/**
 * Created by martin on 16-3-21.
 */
public class Mp3Encoder {

    static{
        System.loadLibrary("mp3encoder");
    }

    public native String getStr();
}
