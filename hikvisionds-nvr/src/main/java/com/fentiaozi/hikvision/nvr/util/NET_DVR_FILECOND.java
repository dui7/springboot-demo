package com.fentiaozi.hikvision.nvr.util;

import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

/**
 * @author wgq
 */
public class NET_DVR_FILECOND extends Structure {
    /*通道号*/
    public NativeLong lChannel;
    /*录象文件类型0xff－全部，0－定时录像,1-移动侦测 ，2－报警触发，3-报警|移动侦测 4-报警&移动侦测 5-命令触发 6-手动录像*/
    public int dwFileType;
    /* 是否锁定 0-正常文件,1-锁定文件, 0xff表示所有文件*/
    public int dwIsLocked;
    /*是否使用卡号*/
    public int dwUseCardNo;
    /* 卡号*/
    public byte[] sCardNumber = new byte[32];
    /*开始时间*/
    public NET_DVR_TIME struStartTime;
    /*结束时间*/
    public NET_DVR_TIME struStopTime;
}
