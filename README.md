# android-lame-mp3


#Usage

1. start record

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
                
2. pause record
         
         mAudioRecordInstance.setPaused(true);
         
3.  resume record

        mAudioRecordInstance.setPaused(false);
        
4. stop record

        mAudioRecordInstance.setRecording(false);
        