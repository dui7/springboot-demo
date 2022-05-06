package com.fentiaozi.hikvision.nvr.util;

import com.sun.jna.Structure;

/** 录象文件参数(9000)
 * @author wgq
 */
public class NET_DVR_FINDDATA_V30 extends Structure {
    /*文件名*/
    public byte[] sFileName = new byte[100];
    /*文件的开始时间*/
    public NET_DVR_TIME struStartTime;
    /*文件的结束时间*/
    public NET_DVR_TIME struStopTime;
    /*文件的大小*/
    public int dwFileSize;
    public byte[] sCardNum = new byte[32];
    /*9000设备支持,1表示此文件已经被锁定,0表示正常的文件*/
    public byte byLocked;
    public byte[] byRes = new byte[3];
}
