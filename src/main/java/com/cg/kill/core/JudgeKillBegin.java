package com.cg.kill.core;

import com.alibaba.fastjson.JSONObject;
import com.cg.kill.constants.RequestParamConstants;
import com.xatu.jdkill.test.HttpUrlConnectionUtil;
import com.xatu.jdkill.test.Start;
import java.io.IOException;
import java.text.ParseException;

public class JudgeKillBegin {
  public void isKillBegin(String productSku) throws IOException, ParseException, InterruptedException {
    JSONObject headers = new JSONObject();
    headers.put(RequestParamConstants.headerAgent, RequestParamConstants.headerAgentArg);
    headers.put(RequestParamConstants.Referer, RequestParamConstants.RefererArg);

    JSONObject shopDetail = JSONObject.parseObject(
        HttpUrlConnectionUtil.get(headers, "https://item-soa.jd.com/getWareBusiness?skuId=" + productSku));

    if (shopDetail.get("yuyueInfo") != null) {
      String buyDate = JSONObject.parseObject(shopDetail.get("yuyueInfo").toString()).get("buyTime").toString();
      String startDate = buyDate.split("-202")[0] + ":00";
      Long startTime = HttpUrlConnectionUtil.dateToTime(startDate);
      //开始抢购
      while (true) {
        //获取京东时间
        JSONObject jdTime = JSONObject.parseObject(HttpUrlConnectionUtil.get(headers, "https://api.m.jd.com/client.action?functionId=queryMaterialProducts&client=wh5"));
        Long serverTime = Long.valueOf(jdTime.get("currentTime2").toString());
        if (startTime >= serverTime) {
          System.out.println("正在等待抢购时间");
          Thread.sleep(300);
        } else {
          break;
        }
      }
    }
  }

}
