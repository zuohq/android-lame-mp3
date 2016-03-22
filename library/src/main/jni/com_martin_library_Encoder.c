#include "com_martin_library_Encoder.h"
#include "simple_lame_lib.h"
#include <android/log.h>

static const char *TAG = "Encoder";

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__))

lame_global_flags *glf;

JNIEXPORT void JNICALL Java_com_martin_library_Encoder_init(
        JNIEnv *env, jclass cls, jint inSamplerate, jint outChannel,
        jint outSamplerate, jint outBitrate, jint quality,
        jstring id3tagTitle, jstring id3tagArtist, jstring id3tagAlbum,
        jstring id3tagYear, jstring id3tagComment) {
    LOGI("encoder init");
    glf = simple_lame_lib_init(env, inSamplerate, outChannel, outSamplerate, outBitrate,
                                                  quality, id3tagTitle, id3tagArtist, id3tagAlbum, id3tagYear, id3tagComment);
}

JNIEXPORT jint JNICALL Java_com_martin_library_Encoder_encode(
        JNIEnv *env, jclass cls, jshortArray buffer_l,
        jshortArray buffer_r, jint samples, jbyteArray mp3buf) {
    return simple_lame_lib_encode(env, glf, buffer_l, buffer_r, samples, mp3buf);
}

JNIEXPORT jint JNICALL Java_com_martin_library_Encoder_encodeBufferInterleaved(
        JNIEnv *env, jclass cls, jshortArray pcm,
        jint samples, jbyteArray mp3buf) {
    return simple_lame_lib_encodeBufferInterleaved(env, glf, pcm, samples, mp3buf);
}

JNIEXPORT jint JNICALL Java_com_martin_library_Encoder_flush(
        JNIEnv *env, jclass cls, jbyteArray mp3buf) {
    return simple_lame_lib_flush(env, glf, mp3buf);
}

JNIEXPORT void Java_com_martin_library_Encoder_close(
        JNIEnv *env, jclass cls) {
    simple_lame_lib_close(glf);
}