package com.fentiaozi.hikvision.nvr.util;

import com.fentiaozi.hikvision.nvr.service.hk.HCNetSDK;
import com.sun.jna.Native;

public class CommonKit {
    /**
     * 获取DLL文件路径
     *
     * @return
     */
    public static String getWebPath() {
        String path = CommonKit.class.getClassLoader().getResource("").getPath().substring(1);
        return path + "dll\\";
    }

    /**
     * 加载dll
     *
     * @return
     */
    public static HCNetSDK loadDll() {
        HCNetSDK INSTANCE;
        try {
            //IDE 中加载dll
            INSTANCE = (HCNetSDK) Native.loadLibrary(CommonKit.getWebPath() + "HCNetSDK.dll", HCNetSDK.class);
        } catch (UnsatisfiedLinkError e) {
            try {
                // 打包后,加载jar包同目录下dll文件夹中的dll
                INSTANCE = (HCNetSDK) Native.loadLibrary("dll\\HCNetSDK", HCNetSDK.class);
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }

        }

        return INSTANCE;
    }
}
