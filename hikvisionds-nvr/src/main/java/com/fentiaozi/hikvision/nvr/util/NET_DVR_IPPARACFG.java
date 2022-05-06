package com.fentiaozi.hikvision.nvr.util;

import com.sun.jna.Structure;
import lombok.Data;

@Data
public class NET_DVR_IPPARACFG extends Structure {
    /*允许接入的最大IP设备数*/
    public static final int MAX_IP_DEVICE = 32;
    /*最大32个模拟通道*/
    public static final int MAX_ANALOG_CHANNUM = 32;
    /* 允许加入的最多IP通道数*/
    public static final int MAX_IP_CHANNEL = 32;
    /* 结构大小 */
    public int dwSize;
    /* IP设备 */
    public NET_DVR_IPDEVINFO[] struIPDevInfo = new NET_DVR_IPDEVINFO[MAX_IP_DEVICE];
    /* 模拟通道是否启用，从低到高表示1-32通道，0表示无效 1有效 */
    public byte[] byAnalogChanEnable = new byte[MAX_ANALOG_CHANNUM];
    /* IP通道 */
    public NET_DVR_IPCHANINFO[] struIPChanInfo = new NET_DVR_IPCHANINFO[MAX_IP_CHANNEL];
}
