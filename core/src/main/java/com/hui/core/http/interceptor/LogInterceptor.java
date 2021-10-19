package com.hui.core.http.interceptor;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class LogInterceptor implements Interceptor {
    private static final String KEY_TAG = "LiveHttp";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Charset UTF8 = Charset.forName("UTF-8");

        // 打印请求报文
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String reqBody = null;
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            reqBody = buffer.readString(charset);
        }
        LiveLog.i(KEY_TAG, String.format("发送请求\nmethod：%s\nurl：%s\nheaders: %s\nbody：%s",
                request.method(), request.url(), request.headers(), reqBody));

        // 打印返回报文
        // 先执行请求，才能够获取报文
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String respBody = null;
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
            }
            respBody = buffer.clone().readString(charset);
        }
        LiveLog.i(KEY_TAG, String.format("收到响应\n%s %s\n请求url：%s\n请求body：%s\n响应body：%s",
                response.code(), response.message(), response.request().url(), reqBody, respBody));
        return response;

    }
}
