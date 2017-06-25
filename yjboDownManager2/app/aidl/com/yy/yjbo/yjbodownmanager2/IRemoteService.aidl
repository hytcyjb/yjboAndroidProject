package com.enmc.bag;
import com.enmc.bag.IRemoteServiceCallback;
interface IRemoteService{

    int download(String uri,String downloadTitle,int downloadId,String img,String intro,String category,String node);

    //int pause(int downloadId);//暂停
    //int resume(int downloadId);//恢复
    int delete(int downloadId);
    void registerCallback(IRemoteServiceCallback callBack);

    void unregisterCallback(IRemoteServiceCallback callBack);
}