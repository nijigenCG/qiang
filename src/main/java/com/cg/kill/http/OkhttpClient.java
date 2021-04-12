package com.cg.kill.http;

import com.alibaba.fastjson.JSONObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;

public class OkhttpClient {
  public static Response get(JSONObject headers, String url) throws IOException {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    Request request = new Request.Builder()
        .url(url)
        .method("GET", null)
        .build();
    return client.newCall(request).execute();
  }
  /*public String post(){
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, requestJson);
    Request request = new Request.Builder()
        .url(url)
        .method("POST", body)
        .addHeader("Content-Type", "text/plain")
        .build();
    client.newCall(request).execute();
    return null;
  }*/

}
