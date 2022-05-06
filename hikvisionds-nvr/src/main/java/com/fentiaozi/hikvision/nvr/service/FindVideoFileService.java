package com.fentiaozi.hikvision.nvr.service;

import com.fentiaozi.hikvision.nvr.domain.RequestVo;
import com.fentiaozi.hikvision.nvr.domain.SignIn;
import com.fentiaozi.hikvision.nvr.domain.VideoFile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public interface FindVideoFileService {
    List<VideoFile> playback(RequestVo requestVo) throws InterruptedException;

    boolean downloadByFileName(String fileName,RequestVo requestVo);

    boolean downloadByFileTime(RequestVo requestVo);

    List<SignIn> getDeviceInformation(RequestVo requestVo);

    Map getBackUrl(RequestVo requestVo);
}
