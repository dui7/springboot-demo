package com.fentiaozi.hikvision.nvr.service.hk;

import com.fentiaozi.hikvision.nvr.util.*;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface HCNetSDK extends StdCallLibrary {

    /*加载海康DLL*/
    HCNetSDK INSTANCE = CommonKit.loadDll();    /// 部署

    /*初始化*/
    boolean NET_DVR_Init();

    /*用户登录*/
    NativeLong NET_DVR_Login_V30(String sDVRIP, short wDVRPort, String sUserName, String sPassword, NET_DVR_DEVICEINFO_V30 lpDeviceInfo);

    /* 参数配置 begin*/
    boolean NET_DVR_GetDVRConfig(NativeLong lUserID, int dwCommand, NativeLong lChannel, Pointer lpOutBuffer, int dwOutBufferSize, IntByReference lpBytesReturned);

    /*查询视频文件*/
    NativeLong NET_DVR_FindFile_V30(NativeLong lUserID, NET_DVR_FILECOND pFindCond);

    /*查找下一个文件*/
    NativeLong NET_DVR_FindNextFile_V30(NativeLong lFindHandle, NET_DVR_FINDDATA_V30 lpFindData);

    /*结束搜索文件*/
    boolean NET_DVR_FindClose_V30(NativeLong lFindHandle);

    /*用户注销*/
    boolean NET_DVR_Logout_V30(NativeLong lUserID);

    /* 根据名称搜索文件 */
    NativeLong NET_DVR_GetFileByName(NativeLong lUserID, String sDVRFileName, String sSavedFileName);

    /*下载文件*/
    boolean NET_DVR_PlayBackControl(NativeLong lPlayHandle, int dwControlCode, int dwInValue, IntByReference LPOutValue);

    /* 停止文件下载 */
    boolean  NET_DVR_StopGetFile(NativeLong lFileHandle);

    /*退出NVR*/
    boolean  NET_DVR_Logout(NativeLong lUserID);

    /*获取退出状态*/
    int  NET_DVR_GetLastError();

    /*根据时间下载视频*/
    NativeLong  NET_DVR_GetFileByTime(NativeLong lUserID, NativeLong lChannel, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime, String sSavedFileName);

}
