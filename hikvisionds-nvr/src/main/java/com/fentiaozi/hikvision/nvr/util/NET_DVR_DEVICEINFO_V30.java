package com.fentiaozi.hikvision.nvr.util;

import com.sun.jna.Structure;
import lombok.Data;

/**
 * @author wgq
 */
@Data
public class NET_DVR_DEVICEINFO_V30 extends Structure {
    //序列号长度
    public static final int SERIALNO_LEN = 48;
    //序列号
    public byte[] sSerialNumber = new byte[SERIALNO_LEN];
    //报警输入个数
    public byte byAlarmInPortNum;
    //报警输出个数
    public byte byAlarmOutPortNum;
    //硬盘个数
    public byte byDiskNum;
    //设备类型, 1:DVR 2:ATM DVR 3:DVS ......
    public byte byDVRType;
    //模拟通道个数
    public byte byChanNum;
    //起始通道号,例如DVS-1,DVR - 1
    public byte byStartChan;
    //语音通道数
    public byte byAudioChanNum;
    //最大数字通道个数
    public byte byIPChanNum;
    //保留
    public byte[] byRes1 = new byte[24];
}
