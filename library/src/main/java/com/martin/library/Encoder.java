/*
 * Copyright (C) 2011-2012 Yuichi Hirano
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package com.martin.library;

/**
 * LAME interface class.
 * <p/>
 * This class is object-oriented interface.
 * {@link "https://github.com/yhirano/SimpleLameLibForAndroid/blob/master/SimpleLameLibForAndroid/src/com/uraroji/garage/android/lame/Encoder.java"}
 */
public class Encoder {

    static {
        System.loadLibrary("mp3encoder");
    }

    /**
     * Lame builder class.
     */
    public static class Builder {

        /**
         * Input sample rate in Hz.
         */
        private final int mInSamplerate;

        /**
         * Number of channels in input stream.
         */
        private final int mOutChannel;

        /**
         * Output sample rate in Hz.
         */
        private final int mOutSamplerate;

        /**
         * Brate compression ratio in KHz.
         */
        private final int mOutBitrate;

        /**
         * Encode quality.
         */
        private int mQuality = 7;

        /**
         * ID3 Tag title.
         */
        private String mId3tagTitle = null;

        /**
         * ID3 Tag artist.
         */
        private String mId3tagArtist = null;

        /**
         * ID3 Tag album.
         */
        private String mId3tagAlbum = null;

        /**
         * ID3 Tag year.
         */
        private String mId3tagYear = null;

        /**
         * ID3 Tag comment.
         */
        private String mId3tagComment = null;

        /**
         * Constructor.
         *
         * @param inSamplerate  Input sample rate in Hz.
         * @param outChannel    Number of channels in input stream.
         * @param outSamplerate Output sample rate in Hz.
         * @param outBitrate    Brate compression ratio in KHz.
         */
        public Builder(int inSamplerate, int outChannel,
                       int outSamplerate, int outBitrate) {
            mInSamplerate = inSamplerate;
            mOutChannel = outChannel;
            mOutSamplerate = outSamplerate;
            mOutBitrate = outBitrate;
        }

        /**
         * Encode quality.
         *
         * @param quality quality=0..9. 0=best (very slow). 9=worst.<br />
         *                recommended:<br />
         *                2 near-best quality, not too slow<br />
         *                5 good quality, fast<br />
         *                7 ok quality, really fast
         * @return Builder
         */
        public Builder quality(int quality) {
            mQuality = quality;
            return this;
        }

        /**
         * ID3 Tag title.
         *
         * @param id3tagTitle ID3 title.
         * @return Builder
         */
        public Builder id3tagTitle(String id3tagTitle) {
            mId3tagTitle = id3tagTitle;
            return this;
        }

        /**
         * ID3 Tag artist.
         *
         * @param id3tagArtist ID3 artist.
         * @return Builder
         */
        public Builder id3tagArtist(String id3tagArtist) {
            mId3tagArtist = id3tagArtist;
            return this;
        }

        /**
         * ID3 Tag album.
         *
         * @param id3tagAlbum ID3 album.
         * @return Builder
         */
        public Builder id3tagAlbum(String id3tagAlbum) {
            mId3tagAlbum = id3tagAlbum;
            return this;
        }

        /**
         * ID3 Tag year.
         *
         * @param id3tagYear ID3 year.
         * @return Builder
         */
        public Builder id3tagYear(String id3tagYear) {
            mId3tagYear = id3tagYear;
            return this;
        }

        /**
         * ID3 Tag comment.
         *
         * @param id3tagComment ID3 comment.
         * @return Builder
         */
        public Builder id3tagComment(String id3tagComment) {
            mId3tagComment = id3tagComment;
            return this;
        }

        /**
         * Create {@link Encoder} instance.
         *
         * @return {@link Encoder} instance.
         */
        public Encoder create() {
            return new Encoder(this);
        }
    }


    /**
     * Constructor.
     *
     * @param builder Builder.
     */
    private Encoder(Builder builder) {
        init(builder.mInSamplerate, builder.mOutChannel, builder.mOutSamplerate,
                builder.mOutBitrate, builder.mQuality, builder.mId3tagTitle, builder.mId3tagArtist,
                builder.mId3tagAlbum, builder.mId3tagYear, builder.mId3tagComment);
    }

    /**
     * Initialize LAME.
     *
     * @param inSamplerate  input sample rate in Hz.
     * @param outChannel    number of channels in input stream.
     * @param outSamplerate output sample rate in Hz.
     * @param outBitrate    brate compression ratio in KHz.
     * @param quality       quality=0..9. 0=best (very slow). 9=worst.<br />
     *                      recommended:<br />
     *                      2 near-best quality, not too slow<br />
     *                      5 good quality, fast<br />
     *                      7 ok quality, really fast
     * @param id3tagTitle   ID3 Tag title.
     * @param id3tagArtist  ID3 Tag artist.
     * @param id3tagAlbum   ID3 Tag album.
     * @param id3tagYear    ID3 Tag year.
     * @param id3tagComment ID3 Tag comment.
     */
    public native static void init(int inSamplerate, int outChannel,
                                   int outSamplerate, int outBitrate, int quality, String id3tagTitle,
                                   String id3tagArtist, String id3tagAlbum, String id3tagYear,
                                   String id3tagComment);

    /**
     * Encode buffer to mp3.
     *
     * @param buffer_l      PCM data for left channel.
     * @param buffer_r      PCM data for right channel.
     * @param samples       number of samples per channel.
     * @param mp3buf        result encoded MP3 stream. You must specified
     *                      "7200 + (1.25 * samples)" length array.
     * @return number of bytes output in mp3buf. Can be 0.<br />
     * -1: mp3buf was too small<br />
     * -2: malloc() problem<br />
     * -3: lame_init_params() not called<br />
     * -4: psycho acoustic problems
     */
    public native static int encode(short[] buffer_l, short[] buffer_r, int samples, byte[] mp3buf);

    /**
     * Encode buffer L & R channel data interleaved to mp3.
     *
     * @param pcm           PCM data for left and right channel, interleaved.
     * @param samples       number of samples per channel. <strong>not</strong> number
     *                      of samples in pcm[].
     * @param mp3buf        result encoded MP3 stream. You must specified
     *                      "7200 + (1.25 * samples)" length array.
     * @return number of bytes output in mp3buf. Can be 0.<br />
     * -1: mp3buf was too small<br />
     * -2: malloc() problem<br />
     * -3: lame_init_params() not called<br />
     * -4: psycho acoustic problems
     */
    public native static int encodeBufferInterleaved(short[] pcm, int samples, byte[] mp3buf);

    /**
     * Flush LAME buffer.
     *
     * @param mp3buf        result encoded MP3 stream. You must specified at least 7200
     *                      bytes.
     * @return number of bytes output to mp3buf. Can be 0.
     */
    public native static int flush(byte[] mp3buf);

    /**
     * Close LAME.
     *
     */
    public native static void close();
}