package com.enmc.bag;

import com.enmc.bag.IUploadServiceCallback;
interface IUploadService{

    int upload(String uri,String title,int kpId,String img,String content,int flag,String node,int sourceId,String ip,int port,int wilitype,int nodeId);
    int pause(int logId);
    void resume(int logId);
    void resumeDraft(int logId);
    void registerCallback(IUploadServiceCallback callBack);

    void unregisterCallback(IUploadServiceCallback callBack);
}