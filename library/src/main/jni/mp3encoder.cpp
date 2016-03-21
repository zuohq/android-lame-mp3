//
// Created by martin on 16-3-21.
//
#include "com_martin_library_Mp3Encoder.h"

JNIEXPORT jstring JNICALL Java_com_martin_library_Mp3Encoder_getStr(JNIEnv *env, jobject) {
    return (*env).NewStringUTF("hello ,world");
}
