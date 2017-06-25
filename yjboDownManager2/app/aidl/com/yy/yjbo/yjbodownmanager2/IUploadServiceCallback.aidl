package com.enmc.bag;

interface IUploadServiceCallback{

    long updateProgress(int progress,int totalLength,int id);
    void setMaxProgress(int maxProgress,int id);
    boolean uploadState(int state,int id);
}