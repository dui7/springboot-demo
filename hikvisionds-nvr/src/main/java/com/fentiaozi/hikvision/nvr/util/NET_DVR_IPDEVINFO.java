package com.fentiaozi.hikvision.nvr.util;

import com.sun.jna.Structure;
import lombok.Data;

@Data
public class NET_DVR_IPDEVINFO extends Structure {
    /*用户名长度*/
    public static final int NAME_LEN = 32;
    /*密码长度*/
    public static final int PASSWD_LEN = 16;
    /* 该IP设备是否启用 */
    public int dwEnable;
    /* 用户名 */
    public byte[] sUserName = new byte[NAME_LEN];
    /* 密码 */
    public byte[] sPassword = new byte[PASSWD_LEN];
    /* IP地址 */
    public NET_DVR_IPADDR struIP = new NET_DVR_IPADDR();
    /* 端口号 */
    public short wDVRPort;
    /* 保留 */
    public byte[] byres = new byte[34];
}
