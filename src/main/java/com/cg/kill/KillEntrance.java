package com.cg.kill;

import com.alibaba.fastjson.JSONObject;
import com.cg.kill.constants.ProductEnum;
import com.cg.kill.constants.RequestParamConstants;
import com.cg.kill.core.JudgeKillBegin;
import com.cg.kill.core.KillCoreService;
import com.cg.kill.login.LoginService;

import com.sun.webkit.network.CookieManager;
import com.xatu.jdkill.test.HttpUrlConnectionUtil;
import com.xatu.jdkill.test.Start;
import java.io.IOException;
import java.net.CookieHandler;

public class KillEntrance {
    static CookieManager cookieManager = new CookieManager();

    public static void main(String[] args) throws Exception {
        String productSku = ProductEnum.TEST.getSku();

        JSONObject headers = new JSONObject();
        headers.put(RequestParamConstants.headerAgent, RequestParamConstants.headerAgentArg);
        headers.put(RequestParamConstants.Referer, RequestParamConstants.RefererArg);

        CookieHandler.setDefault(cookieManager);
        //login
        String ticket = new LoginService().login(cookieManager);
        //wait to begin
        new JudgeKillBegin().isKillBegin(productSku);
        //add to cart(product num limit 1)
        HttpUrlConnectionUtil
            .get(headers, "https://cart.jd.com/gate.action?pcount=1&ptype=1&pid=" + productSku);
        //thread！

        //submit order
        new KillCoreService().run(cookieManager,ticket,productSku);

    }
}
