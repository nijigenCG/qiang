package com.cg.kill;

import com.cg.kill.core.KillCoreService;
import com.cg.kill.login.LoginService;

public class KillEntrance {
    public static void main(String[] args) {
        //登录
        new LoginService().login();
        //开启多线程

        //调用kill core service开始
        new KillCoreService();

    }
}
