package com.enmc.bag;

interface IRemoteServiceCallback{

    long updateProgress(int progress,int totalLength,int id);
    void setMaxProgress(int maxProgress,int id);
    void downloadState(int state,int id);
}