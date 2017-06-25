package yjbo.yy.yjbodownmanager.downmanager.db;




import java.io.Serializable;

import yjbo.yy.yjbodownmanager.downmanager.cache.CacheMode;
import yjbo.yy.yjbodownmanager.downmanager.model.HttpHeaders;
import yjbo.yy.yjbodownmanager.downmanager.model.HttpParams;
import yjbo.yy.yjbodownmanager.downmanager.request.BaseRequest;
import yjbo.yy.yjbodownmanager.downmanager.request.DeleteRequest;
import yjbo.yy.yjbodownmanager.downmanager.request.GetRequest;
import yjbo.yy.yjbodownmanager.downmanager.request.HeadRequest;
import yjbo.yy.yjbodownmanager.downmanager.request.OptionsRequest;
import yjbo.yy.yjbodownmanager.downmanager.request.PostRequest;
import yjbo.yy.yjbodownmanager.downmanager.request.PutRequest;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/8/8
 * 描    述：与BaseRequest对应,主要是为了序列化
 * 修订历史：
 * ================================================
 */
public class DownloadRequest implements Serializable {

    private static final long serialVersionUID = -6883956320373276785L;

    public String method;
    public String url;
    public CacheMode cacheMode;
    public String cacheKey;
    public long cacheTime;
    public HttpParams params;
    public HttpHeaders headers;

    public static String getMethod(BaseRequest request) {
        if (request instanceof GetRequest) return "get";
        if (request instanceof PostRequest) return "post";
        if (request instanceof PutRequest) return "put";
        if (request instanceof DeleteRequest) return "delete";
        if (request instanceof OptionsRequest) return "options";
        if (request instanceof HeadRequest) return "head";
        return "";
    }

    public static BaseRequest createRequest(String url, String method) {
        if (method.equals("get")) return new GetRequest(url);
        if (method.equals("post")) return new PostRequest(url);
        if (method.equals("put")) return new PutRequest(url);
        if (method.equals("delete")) return new DeleteRequest(url);
        if (method.equals("options")) return new OptionsRequest(url);
        if (method.equals("head")) return new HeadRequest(url);
        return null;
    }
}
