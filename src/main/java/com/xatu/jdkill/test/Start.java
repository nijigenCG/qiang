package com.xatu.jdkill.test;

import com.alibaba.fastjson.JSONObject;
import com.sun.webkit.network.CookieManager;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.concurrent.*;

public class Start {
    final static String headerAgent = "User-Agent";
    final static String headerAgentArg = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36";
    final static String Referer = "Referer";
    final static String RefererArg = "https://passport.jd.com/new/login.aspx";
    //6700xt 100019084146
    //茅台     100012043978
    //r9000k   100019736886

    static String pid = "100012043978";
    //eid
    static String eid = "W2HEXZSRULGOBXAMFF6J44UTIGCP5QGKRQO5M7KZHYUAU7RT2JBTXRG2ZNRUWHKYX2PHNKRJI2KOM7BZIZ2V3F3C64";
    //fp
    static String fp = "4ce08fcab2f99f47724c9c7cdf771d9f";
    //抢购数量
    volatile static Integer ok = 2;

    static CookieManager manager = new CookieManager();


    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, ParseException {
        CookieHandler.setDefault(manager);
        //获取venderId
//        String shopDetail = util.get(null, "https://item.jd.com/" + RushToPurchase.pid + ".html");
//        String venderID = shopDetail.split("isClosePCShow: false,\n" +
//                "                venderId:")[1].split(",")[0];
//        RushToPurchase.venderId = venderID;
        //登录
        Login.Login();
        //判断是否开始抢购
        judgePruchase();
        JSONObject headers = new JSONObject();
        headers.put(Start.headerAgent, Start.headerAgentArg);
        headers.put(Start.Referer, Start.RefererArg);
        //HttpUrlConnectionUtil.get(headers, "https://cart.jd.com/gate.action?pcount=1&ptype=1&pid=" + Start.pid);
        //开始抢购
       /* ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new RushToPurchase());
        }*/
        new RushToPurchase().run();
    }

    public static void judgePruchase() throws IOException, ParseException, InterruptedException {
        //获取开始时间
        JSONObject headers = new JSONObject();
        headers.put(Start.headerAgent, Start.headerAgentArg);
        headers.put(Start.Referer, Start.RefererArg);
        JSONObject shopDetail = JSONObject.parseObject(HttpUrlConnectionUtil.get(headers, "https://item-soa.jd.com/getWareBusiness?skuId=" + pid));
        if (shopDetail.get("yuyueInfo") != null) {
            String buyDate = JSONObject.parseObject(shopDetail.get("yuyueInfo").toString()).get("buyTime").toString();
            String startDate = buyDate.split("-202")[0] + ":00";
            Long startTime = HttpUrlConnectionUtil.dateToTime(startDate);
            //开始抢购
            while (true) {
                //获取京东时间
                try{
                    JSONObject jdTime = JSONObject.parseObject(HttpUrlConnectionUtil.get(headers, "https://api.m.jd.com/client.action?functionId=queryMaterialProducts&client=wh5"));
                    Long serverTime = Long.valueOf(jdTime.get("currentTime2").toString());
                    if (startTime >= serverTime) {
                        System.out.println("正在等待抢购时间");
                        Thread.sleep(300);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }

            }
        }
    }
}
