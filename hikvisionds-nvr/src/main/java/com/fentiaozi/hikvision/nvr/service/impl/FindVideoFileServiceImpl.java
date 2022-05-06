package com.fentiaozi.hikvision.nvr.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fentiaozi.hikvision.nvr.domain.*;
import com.fentiaozi.hikvision.nvr.service.FindVideoFileService;
import com.fentiaozi.hikvision.nvr.service.hk.HCNetSDK;
import com.fentiaozi.hikvision.nvr.util.*;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wgq
 */
@Slf4j
@Service
public class FindVideoFileServiceImpl implements FindVideoFileService {

    /*获取IP接入配置信息*/
    public static final int NET_DVR_GET_IPPARACFG = 1048;
    /*允许加入的最多IP通道数*/
    public static final int MAX_IP_CHANNEL = 32;
    /*获得文件信息*/
    public static final int NET_DVR_FILE_SUCCESS = 1000;
    /*正在查找文件*/
    public static final int NET_DVR_ISFINDING = 1002;
    /*没有文件*/
    public static final int NET_DVR_FILE_NOFIND = 1001;
    /*开始播放*/
    public static final int NET_DVR_PLAYSTART = 1;
    /*获取文件回放的进度*/
    public static final int NET_DVR_PLAYGETPOS = 13;

    NativeLong lUserID;

    /*下载句柄*/
    NativeLong m_lDownloadHandle;


    /*设备信息*/
    NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;
    HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataUtil dataUtil;

    /**
     * 根据时间搜索视频
     *
     * @param requestVo
     */
    @Override
    public List<VideoFile> playback(RequestVo requestVo) throws InterruptedException {
        SignIn signIn = requestVo.getSignIn();
        PlayBack playBack = requestVo.getPlayBack();
        NativeLong m_lUserID = userId(signIn);
        /*设置检索开始结束时间*/
        NvrTime startTime = dataUtil.dateToNum(playBack.getStartTime());
        NvrTime endTime = dataUtil.dateToNum(playBack.getEndTime());
        NET_DVR_TIME struStartTime;
        NET_DVR_TIME struStopTime;
        NET_DVR_FILECOND m_strFilecond = new NET_DVR_FILECOND();
        m_strFilecond.struStartTime = new NET_DVR_TIME();
        m_strFilecond.struStopTime = new NET_DVR_TIME();
        /*组装开始时间*/
        m_strFilecond.struStartTime.dwYear = startTime.getDwYear();
        m_strFilecond.struStartTime.dwMonth = startTime.getDwMonth();
        m_strFilecond.struStartTime.dwDay = startTime.getDwDay();
        m_strFilecond.struStartTime.dwHour = startTime.getDwHour();
        m_strFilecond.struStartTime.dwMinute = startTime.getDwMinute();
        m_strFilecond.struStartTime.dwSecond = startTime.getDwSecond();
        /*组装结束时间*/
        m_strFilecond.struStopTime.dwYear = endTime.getDwYear();
        m_strFilecond.struStopTime.dwMonth = endTime.getDwMonth();
        m_strFilecond.struStopTime.dwDay = endTime.getDwDay();
        m_strFilecond.struStopTime.dwHour = endTime.getDwHour();
        m_strFilecond.struStopTime.dwMinute = endTime.getDwMinute();
        m_strFilecond.struStopTime.dwSecond = endTime.getDwSecond();
        /*文件类型*/
        m_strFilecond.dwFileType = 0;
        m_strFilecond.dwIsLocked = 0xff;
        m_strFilecond.dwUseCardNo = 0;
        /*通道号*/
        m_strFilecond.lChannel = new NativeLong(playBack.getChannelNumber());
        /*获取文件*/
        NativeLong lFindFile = hCNetSDK.NET_DVR_FindFile_V30(m_lUserID, m_strFilecond);
        NET_DVR_FINDDATA_V30 strFile = new NET_DVR_FINDDATA_V30();
        long findFile = lFindFile.longValue();
        if (findFile > -1) {
            log.info("file:{}" + findFile);
        }
        NativeLong lnext;
        strFile = new NET_DVR_FINDDATA_V30();
        List<VideoFile> videoFiles = new ArrayList<>();
        while (true) {
            lnext = hCNetSDK.NET_DVR_FindNextFile_V30(lFindFile, strFile);
            if (lnext.longValue() == NET_DVR_FILE_SUCCESS) {
                log.info("搜索成功");
                /*添加文件名信息*/
                String[] s = new String[2];
                s = new String(strFile.sFileName).split("\0", 2);
                VideoFile videoFile = new VideoFile();
                /*添加文件大小信息*/
                int iTemp;
                String MyString;
                if (strFile.dwFileSize < 1024 * 1024) {
                    iTemp = (strFile.dwFileSize) / (1024);
                    MyString = iTemp + "K";
                } else {
                    iTemp = (strFile.dwFileSize) / (1024 * 1024);
                    MyString = iTemp + "M   ";
                    iTemp = ((strFile.dwFileSize) % (1024 * 1024)) / (1204);
                    MyString = MyString + iTemp + "K";
                }
                videoFile.setFileNme(new String(s[0]));
                videoFile.setFileSize(MyString);
                videoFile.setStartTime(strFile.struStartTime.toStringTime());
                videoFile.setEndTime(strFile.struStopTime.toStringTime());
                videoFiles.add(videoFile);
            } else {
                /*搜索中*/
                if (lnext.longValue() == NET_DVR_ISFINDING) {
                    log.info("搜索中");
                    continue;
                } else {
                    if (lnext.longValue() == NET_DVR_FILE_NOFIND) {
                        log.info("没有搜到文件");
                        return videoFiles;
                    } else {
                        log.info("搜索文件结束");
                        boolean flag = hCNetSDK.NET_DVR_FindClose_V30(lFindFile);
                        if (flag == false) {
                            log.info("结束搜索失败");
                        }
                        return videoFiles;
                    }
                }
            }
        }
    }

    /**
     * 按照文件下载
     *
     * @param fileName
     * @param requestVo
     * @return
     */
    @Override
    public boolean downloadByFileName(String fileName, RequestVo requestVo) {
        /*初始化用户*/
        NativeLong nativeLong = userId(requestVo.getSignIn());
        /*初始化下载值*/
        m_lDownloadHandle = new NativeLong(-1);
        if (m_lDownloadHandle.intValue() == -1) {
            /*根据输入的文件名查找文件*/
            m_lDownloadHandle = hCNetSDK.NET_DVR_GetFileByName(nativeLong, fileName, "D:\\fileNme.3GP");
            if (m_lDownloadHandle.intValue() >= 0) {
                /*下载文件*/
                boolean downloadFlag = hCNetSDK.NET_DVR_PlayBackControl(m_lDownloadHandle, NET_DVR_PLAYSTART, 0, null);
                int tmp = -1;
                IntByReference pos = new IntByReference();
                while (true) {
                    boolean backFlag = hCNetSDK.NET_DVR_PlayBackControl(m_lDownloadHandle, NET_DVR_PLAYGETPOS, 0, pos);
                    /*防止单个线程死循环*/
                    if (!backFlag) {
                        return downloadFlag;
                    }
                    int produce = pos.getValue();
                    /*获取下载进度*/
                    if ((produce % 10) == 0 && tmp != produce) {
                        tmp = produce;
                        log.info("视频下载进度：{}", produce + "%");
                    }
                    /*下载成功*/
                    if (produce == 100) {
                        hCNetSDK.NET_DVR_StopGetFile(m_lDownloadHandle);
                        m_lDownloadHandle.setValue(-1);
                        /*退出录像机*/
                        hCNetSDK.NET_DVR_Logout(lUserID);
                        log.info("退出状态：{}", hCNetSDK.NET_DVR_GetLastError());
                        return true;
                    }
                    /*下载失败*/
                    if (produce > 100) {
                        hCNetSDK.NET_DVR_StopGetFile(m_lDownloadHandle);
                        m_lDownloadHandle.setValue(-1);
                        log.warn("由于网络原因或NVR较忙,下载异常终止!错误原因：{}", hCNetSDK.NET_DVR_GetLastError());
                        hCNetSDK.NET_DVR_Logout(lUserID);
                        return false;
                    }
                }
            } else {
                log.info("视频下载失败！失败原因：{}", hCNetSDK.NET_DVR_GetLastError());
                return false;
            }
        }
        return true;
    }

    /**
     * 按照时间下载文件
     *
     * @param requestVo
     * @return
     */
    @Override
    public boolean downloadByFileTime(RequestVo requestVo) {
        PlayBack playBack = requestVo.getPlayBack();
        /*初始化用户*/
        NativeLong nativeLong = userId(requestVo.getSignIn());
        if (nativeLong.intValue() == -1) {
            return false;
        }
        m_lDownloadHandle = new NativeLong(-1);
        if (m_lDownloadHandle.intValue() == -1) {
            /*根据输入的文件名查找文件*/
            m_lDownloadHandle = hCNetSDK.NET_DVR_GetFileByTime(nativeLong, new NativeLong(playBack.getChannelNumber().longValue())
                    , dataUtil.getHkTime(playBack.getStartTime()), dataUtil.getHkTime(playBack.getEndTime()), "D:\\zkl\\fileName.mkv");
            if (m_lDownloadHandle.intValue() >= 0) {
                /*下载文件*/
                boolean downloadFlag = hCNetSDK.NET_DVR_PlayBackControl(m_lDownloadHandle, NET_DVR_PLAYSTART, 0, null);
                int tmp = -1;
                IntByReference pos = new IntByReference();
                while (true) {
                    boolean backFlag = hCNetSDK.NET_DVR_PlayBackControl(m_lDownloadHandle, NET_DVR_PLAYGETPOS, 0, pos);
                    /*防止单个线程死循环*/
                    if (!backFlag) {
                        return downloadFlag;
                    }
                    int produce = pos.getValue();
                    /*获取下载进度*/
                    if ((produce % 10) == 0 && tmp != produce) {
                        tmp = produce;
                        log.info("视频下载进度：{}", produce + "%");
                    }
                    /*下载成功*/
                    if (produce == 100) {
                        hCNetSDK.NET_DVR_StopGetFile(m_lDownloadHandle);
                        m_lDownloadHandle.setValue(-1);
                        /*退出录像机*/
                        hCNetSDK.NET_DVR_Logout(lUserID);
                        log.info("退出状态：{}", hCNetSDK.NET_DVR_GetLastError());
                        return true;
                    }
                    /*下载失败*/
                    if (produce > 100) {
                        hCNetSDK.NET_DVR_StopGetFile(m_lDownloadHandle);
                        m_lDownloadHandle.setValue(-1);
                        log.warn("由于网络原因或NVR较忙,下载异常终止!错误原因：{}", hCNetSDK.NET_DVR_GetLastError());
                        hCNetSDK.NET_DVR_Logout(lUserID);
                        return false;
                    }
                }
            } else {
                log.info("视频下载失败！失败原因：{}", hCNetSDK.NET_DVR_GetLastError());
                return false;
            }
        }
        return true;
    }


    /**
     * 获取用户登录信息
     *
     * @return 返回用户状态
     */
    public NativeLong userId(SignIn signIn) {
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true) {
            log.info("初始化失败");
        }
        /*判断用户状态*/
        if (lUserID != null && lUserID.longValue() > -1) {
            hCNetSDK.NET_DVR_Logout_V30(lUserID);
            lUserID = new NativeLong(-1);
        }
        /*用户登录*/
        lUserID = hCNetSDK.NET_DVR_Login_V30(signIn.getIp(),
                (short) signIn.getPort(), signIn.getUserName(), signIn.getPassword(), m_strDeviceInfo);
        return lUserID;
    }

    /**
     * 用户登录 初始化设备
     *
     * @param requestVo
     * @return
     */
    @Override
    public List<SignIn> getDeviceInformation(RequestVo requestVo) {
        SignIn sign = requestVo.getSignIn();
        /*IP参数*/
        NET_DVR_IPPARACFG m_strIpparaCfg;
        /* 设备列表*/
        List<SignIn> signIns = new ArrayList<>();
        m_strDeviceInfo = new NET_DVR_DEVICEINFO_V30();
        NativeLong lUserID = userId(sign);
        long userID = lUserID.longValue();
        if (userID == -1) {
            log.info("注册失败");
            return signIns;
        } else {
            log.info("注册成功");
            /*通道树节点数目*/
            int m_iTreeNodeNum = 0;
            /*获取IP接入配置参数*/
            IntByReference ibrBytesReturned = new IntByReference(0);
            /*IP接入配置结构获取*/
            m_strIpparaCfg = new NET_DVR_IPPARACFG();
            m_strIpparaCfg.write();
            Pointer lpIpParaConfig = m_strIpparaCfg.getPointer();
            /*获取通道参数*/
            boolean bRet = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, NET_DVR_GET_IPPARACFG, new NativeLong(0),
                    lpIpParaConfig, m_strIpparaCfg.size(), ibrBytesReturned);
            m_strIpparaCfg.read();
            if (!bRet) {
                /*设备不支持,则表示没有IP通道*/
                for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++) {
                    SignIn s = new SignIn();
                    s.setDeviceId("Camera" + (iChannum + m_strDeviceInfo.byStartChan));
                    signIns.add(s);
                }
            } else {
                /*设备支持IP通道*/
                for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++) {
                    if (m_strIpparaCfg.byAnalogChanEnable[iChannum] == 1) {
                        SignIn s = new SignIn();
                        s.setDeviceId("Camera" + (iChannum + m_strDeviceInfo.byStartChan));
                        signIns.add(s);
                        m_iTreeNodeNum++;
                    }
                }
                for (int iChannum = 0; iChannum < MAX_IP_CHANNEL; iChannum++) {
                    /*判断该通道号是否存在摄像头*/
                    if (m_strIpparaCfg.struIPChanInfo[iChannum].byChannel == 1) {
                        SignIn s = new SignIn();
                        NET_DVR_IPDEVINFO dev = m_strIpparaCfg.struIPDevInfo[iChannum];
                        s.setIp(new String(dev.struIP.sIpV4).trim());
                        s.setUserName(new String(dev.sUserName).trim());
                        s.setPort(dev.wDVRPort);
                        s.setIsLine(String.valueOf(m_strIpparaCfg.struIPChanInfo[iChannum].byEnable));
                        s.setDeviceId("IPCamera" + (iChannum + m_strDeviceInfo.byStartChan));
                        Integer channelNumber = getChannelNumber(s.getDeviceId());
                        s.setChannelNumber(channelNumber);
                        signIns.add(s);
                    }
                }
            }
            log.info("摄像头资源：{}", signIns);
        }
        return signIns;
    }

    /**
     * 回放拉流
     *
     * @param requestVo
     * @return
     */
    @Override
    public Map getBackUrl(RequestVo requestVo) {
        Map map = new HashMap();
        /*初始化用户*/
        SignIn sign = requestVo.getSignIn();
        PlayBack playBack = requestVo.getPlayBack();
        NativeLong nativeLong = userId(sign);
        if (nativeLong.intValue() == -1) {
            log.info("回放推流用户初始化失败");
            map.put("msg", "回放推流用户初始化失败");
            return map;
        }
        String url = sign.getAppId() + "/" + sign.getDeviceId();
        /*获取设备通道号*/
        List<SignIn> signIns = getDeviceInformation(requestVo);
        int channelNum = 0;
        for (SignIn signIn : signIns) {
            if (signIn.getIp().equals(sign.getDeviceIp())) {
                channelNum = signIn.getChannelNumber();
            }
        }
        if (channelNum == 0) {
            log.info("获取设备通道失败");
            map.put("msg", "获取设备通道失败！");
            return map;
        }
        String backUrl = "";
        if (playBack.getUrlType() == 1) {
            /*组装回放流地址*/
            backUrl = "rtsp://admin:" + sign.getPassword() + "@" + sign.getIp() + ":554/Streaming/tracks/" + (channelNum - 32) + "01/?" +
                    "starttime=" + dataUtil.backTimeAssemble(playBack.getStartTime()) + "&endtime=" + dataUtil.backTimeAssemble(playBack.getEndTime());
        } else {
            /*组装实时流地址*/
            backUrl = "rtsp://admin:" + sign.getPassword() + "@" + sign.getDeviceIp() + ":554/Streaming/tracks/" + (channelNum - 32) + "02/?" +
                    "transportmode=multicast";
        }
        map.put("msg", backUrl);
        return map;
    }


    /**
     * 获取选中的通道名,对通道名进行分析:
     *
     * @param sChannelName
     * @return
     */
    public int getChannelNumber(String sChannelName) {
        int iChannelNum = -1;
        /*Camara开头表示模拟通道*/
        if (sChannelName.charAt(0) == 'C') {
            /*子字符串中获取通道号*/
            iChannelNum = Integer.parseInt(sChannelName.substring(6));
        } else {
            /*IPCamara开头表示IP通道*/
            if (sChannelName.charAt(0) == 'I') {
                /*子字符创中获取通道号,IP通道号要加32,如IPCamera3 == 35*/
                iChannelNum = Integer.parseInt(sChannelName.substring(8)) + 32;
            } else {
                return -1;
            }
        }
        return iChannelNum;
    }

}