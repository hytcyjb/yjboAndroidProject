package yjbo.yy.yjbodownmanager.downmanager.request;


import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import yjbo.yy.yjbodownmanager.OkLogger;
import yjbo.yy.yjbodownmanager.downmanager.HttpUtils;
import yjbo.yy.yjbodownmanager.downmanager.model.HttpHeaders;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/1/16
 * 描    述：Options请求
 * 修订历史：
 * ================================================
 */
public class OptionsRequest extends BaseBodyRequest<OptionsRequest> {

    public OptionsRequest(String url) {
        super(url);
        method = "OPTIONS";
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        try {
            headers.put(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.e(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        return requestBuilder.method("OPTIONS", requestBody).url(url).tag(tag).build();
    }
}