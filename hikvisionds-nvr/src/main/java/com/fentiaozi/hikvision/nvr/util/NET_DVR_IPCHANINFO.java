package com.fentiaozi.hikvision.nvr.util;

import com.sun.jna.Structure;
import lombok.Data;

/**
 * IP通道匹配参数
 * @author wgq
 */
@Data
public class NET_DVR_IPCHANINFO extends Structure {
    /* 该通道是否启用 */
    public byte byEnable;
    /* IP设备ID 取值1- MAX_IP_DEVICE */
    public byte byIPID;
    /* 通道号 */
    public byte byChannel;
    /* 保留 */
    public byte[] byres = new byte[33];
}
