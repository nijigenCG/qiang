package com.cg.kill.login;

import com.alibaba.fastjson.JSONObject;
import com.cg.kill.constants.RequestParamConstants;
import com.xatu.jdkill.test.HttpUrlConnectionUtil;
import com.xatu.jdkill.test.Start;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginService {
    public void login() throws IOException {
        //获取二维码并弹出扫描
        getQRCodeAndPopIt();
        //从二维码扫描结果获取Cookie


        //弹出二维码登录

        //登录完成获取cookie进行存储，以便请求

    }

    private void getQRCodeAndPopIt() throws IOException {
        JSONObject headers = new JSONObject();
        headers.put(RequestParamConstants.headerAgent, RequestParamConstants.headerAgentArg);
        headers.put(RequestParamConstants.Referer, RequestParamConstants.RefererArg);
        //获取二维码
        Long now = System.currentTimeMillis();
        HttpUrlConnectionUtil.getQCode(headers, "https://qr.m.jd.com/show?appid=133&size=147&t=" + now);
        //打开二维码
        Runtime.getRuntime().exec("cmd /c QCode.png");
    }
}
