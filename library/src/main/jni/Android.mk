LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
	mp3encoder.cpp

LOCAL_MODULE:= mp3encoder

include $(BUILD_SHARED_LIBRARY)