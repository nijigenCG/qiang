package com.cg.kill.core;

import com.alibaba.fastjson.JSONObject;
import com.cg.kill.constants.RequestParamConstants;
import com.sun.webkit.network.CookieManager;
import com.xatu.jdkill.test.HttpUrlConnectionUtil;
import com.xatu.jdkill.test.Login;
import com.xatu.jdkill.test.Start;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillCoreService {
  volatile static Integer times = 0;


  //core
  public void run(CookieManager cookieManager,String ticket,String productSku) throws URISyntaxException, IOException {
    JSONObject headers = new JSONObject();
    headers.put(RequestParamConstants.headerAgent, RequestParamConstants.headerAgentArg);
    headers.put(RequestParamConstants.Referer, RequestParamConstants.RefererArg);

    while (times < 1) {
      Map<String, List<String>> stringListMap = new HashMap<>();
      stringListMap = cookieManager.get(new URI("https://trade.jd.com/shopping/order/getOrderInfo.action"), stringListMap);
      List<String> cookie = stringListMap.get("Cookie");
      headers.put("Cookie", cookie.get(0));

      String orderInfo = HttpUrlConnectionUtil
          .get(headers, "https://trade.jd.com/shopping/order/getOrderInfo.action");

      //提交订单
      JSONObject subData = new JSONObject();
      headers = new JSONObject();
      subData.put("overseaPurchaseCookies", "");
      subData.put("vendorRemarks", "[]");
      subData.put("submitOrderParam.sopNotPutInvoice", "false");
      subData.put("submitOrderParam.ignorePriceChange", "1");
      subData.put("submitOrderParam.btSupport", "0");
      subData.put("submitOrderParam.isBestCoupon", "1");
      subData.put("submitOrderParam.jxj", "1");
      subData.put("submitOrderParam.trackID", ticket);
      subData.put("submitOrderParam.eid", RequestParamConstants.eid);
      subData.put("submitOrderParam.fp", RequestParamConstants.fp);
      subData.put("submitOrderParam.needCheck", "1");

      headers.put("Referer", "http://trade.jd.com/shopping/order/getOrderInfo.action");
      headers.put("origin", "https://trade.jd.com");
      headers.put("Content-Type", "application/json");
      headers.put("x-requested-with", "XMLHttpRequest");
      headers.put("upgrade-insecure-requests", "1");
      headers.put("sec-fetch-user", "?1");

      stringListMap.clear();
      try {
        stringListMap = cookieManager.get(new URI("https://trade.jd.com/shopping/order/getOrderInfo.action"), stringListMap);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
      cookie = stringListMap.get("Cookie");
      headers.put("Cookie", cookie.get(0).toString());
      String submitOrder = null;
      try {
        if (times < 1) {
          submitOrder = HttpUrlConnectionUtil.post(headers, "https://trade.jd.com/shopping/order/submitOrder.action", null);
        } else {
          System.out.println("已抢购" + 2 + "件，请尽快完成付款");
          break;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (submitOrder.contains("刷新太频繁了") || submitOrder.contains("抱歉，您访问的内容不存在")) {
        System.out.println("刷新太频繁了,您访问的内容不存在");
        continue;
      }
      JSONObject jsonObject = JSONObject.parseObject(submitOrder);
      String success = null;
      String message = null;
      if (jsonObject != null && jsonObject.get("success") != null) {
        success = jsonObject.get("success").toString();
      }
      if (jsonObject != null && jsonObject.get("message") != null) {
        message = jsonObject.get("message").toString();
      }

      if (success == "true") {
        System.out.println("抢购成功，请尽快完成付款");
        break;
      } else {
        if (message != null) {
          System.out.println(message);
        } else if (submitOrder.contains("很遗憾没有抢到")) {
          System.out.println("很遗憾没有抢到，再接再厉哦");
        } else if (submitOrder.contains("抱歉，您提交过快，请稍后再提交订单！")) {
          System.out.println("抱歉，您提交过快，请稍后再提交订单！");
        } else if (submitOrder.contains("系统正在开小差，请重试~~")) {
          System.out.println("系统正在开小差，请重试~~");
        } else if (submitOrder.contains("您多次提交过快")) {
          System.out.println("您多次提交过快，请稍后再试");
        } else {
          System.out.println("获取用户订单信息失败");
        }
      }

    }

  }
}
