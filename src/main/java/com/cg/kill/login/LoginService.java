package com.cg.kill.login;

import com.alibaba.fastjson.JSONObject;
import com.cg.kill.constants.RequestParamConstants;
import com.cg.kill.http.OkhttpClient;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.webkit.network.CookieManager;
import com.xatu.jdkill.test.HttpUrlConnectionUtil;
import com.xatu.jdkill.test.Start;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;

public class LoginService {
    private Map<String, List<String>> requestHeaders = new HashMap<>(16);

    public String login(CookieManager cookieManager) throws Exception {
        //获取二维码并弹出扫描
        //从二维码扫描结果获取Cookie
        //弹出二维码登录
        //登录完成获取cookie进行存储，以便请求

        JSONObject headers = new JSONObject();
        headers.put(RequestParamConstants.headerAgent, RequestParamConstants.headerAgentArg);
        headers.put(RequestParamConstants.Referer, RequestParamConstants.RefererArg);
        //获取二维码
        Long now = System.currentTimeMillis();
        Response response = OkhttpClient.get(headers,"https://qr.m.jd.com/show?appid=133&size=147&t=" + now);
        streamToQRFile(response);

        HttpUrlConnectionUtil.getQCode(headers, "https://qr.m.jd.com/show?appid=133&size=147&t=" + now);
        //打开二维码
        Runtime.getRuntime().exec("cmd /c QCode.png");
        URI url = new URI("https://qr.m.jd.com/show?appid=133&size=147&t=" + now);
        Map<String, List<String>> stringListMap;
        stringListMap = cookieManager.get(url, requestHeaders);
        List cookieList = stringListMap.get("Cookie");
        String cookies = cookieList.get(0).toString();
        String token = cookies.split("wlfstk_smdl=")[1];
        headers.put("Cookie", cookies);
        String ticket = "";
        //判断是否扫二维码
        while (true) {
            String checkUrl = "https://qr.m.jd.com/check?appid=133&callback=jQuery" + (int) ((Math.random() * (9999999 - 1000000 + 1)) + 1000000) + "&token=" + token + "&_=" + System.currentTimeMillis();
            String qrCode = HttpUrlConnectionUtil.get(headers, checkUrl);
            if (qrCode.indexOf("二维码未扫描") != -1) {
                System.out.println("二维码未扫描，请扫描二维码登录");
            } else if (qrCode.indexOf("请手机客户端确认登录") != -1) {
                System.out.println("请手机客户端确认登录");
            } else {
                ticket = qrCode.split("\"ticket\" : \"")[1].split("\"\n" +
                    "}\\)")[0];
                System.out.println("已完成二维码扫描登录");
                close();
                break;
            }
            Thread.sleep(3000);
        }
        //验证，获取cookie
        String qrCodeTicketValidation = HttpUrlConnectionUtil.get(headers, "https://passport.jd.com/uc/qrCodeTicketValidation?t=" + ticket);
        stringListMap = cookieManager.get(url, requestHeaders);
        cookieList = stringListMap.get("Cookie");
        cookies = cookieList.get(0).toString();
        headers.put("Cookie", cookies);

        return ticket;
    }

    private void streamToQRFile(Response response) throws IOException {
        InputStream inputStream = response.body().byteStream();
        OutputStream outputStream = new FileOutputStream("QCode.png");
        byte[] buffer;
        int length;
        buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();
    }

    public void close() {
        //通过窗口标题获取窗口句柄
        WinDef.HWND hWnd;
        final User32 user32 = User32.INSTANCE;
        user32.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer arg1) {
                char[] windowText = new char[512];
                user32.GetWindowText(hWnd, windowText, 512);
                String wText = Native.toString(windowText);
                // get rid of this if block if you want all windows regardless of whether
                // or not they have text
                if (wText.isEmpty()) {
                    return true;
                }
                if (wText.contains("照片")) {
                    hWnd = User32.INSTANCE.FindWindow(null, wText);
                    WinDef.LRESULT lresult = User32.INSTANCE.SendMessage(hWnd, 0X10, null, null);
                }
                return true;
            }
        }, null);
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
