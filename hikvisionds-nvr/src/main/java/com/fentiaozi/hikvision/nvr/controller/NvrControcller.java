package com.fentiaozi.hikvision.nvr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fentiaozi.hikvision.nvr.common.AjaxResult;
import com.fentiaozi.hikvision.nvr.domain.RequestVo;
import com.fentiaozi.hikvision.nvr.service.FindVideoFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(value = "/nvr")
@Api(value = "调用NVR接口", tags = {"调用NVR接口"})
public class NvrControcller {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FindVideoFileService downloadVideoService;


    @RequestMapping(value = "/getDeviceInformation")
    @ApiOperation(value = "获取设备信息", httpMethod = "POST", notes = "获取设备信息")
    public AjaxResult getDeviceInformation(@RequestBody RequestVo requestVo) {
        return AjaxResult.success(downloadVideoService.getDeviceInformation(requestVo));
    }

    @RequestMapping(value = "/downloadByFileTime")
    @ApiOperation(value = "根据时间下载", httpMethod = "POST", notes = "根据时间下载")
    public AjaxResult downloadByFileTime(@RequestBody RequestVo requestVo) {
        return AjaxResult.success(downloadVideoService.downloadByFileTime(requestVo));
    }

    @RequestMapping(value = "/playback")
    @ApiOperation(value = "根据时间获取文件", httpMethod = "POST", notes = "根据时间获取文件")
    public AjaxResult playback(@RequestBody RequestVo requestVo) throws InterruptedException {
        return AjaxResult.success(downloadVideoService.playback(requestVo));
    }

    @RequestMapping(value = "/downloadByFileName")
    @ApiOperation(value = "根据文件名下载", httpMethod = "POST", notes = "根据文件名下载")
    public AjaxResult downloadByFileName(@RequestParam String fileName, @RequestBody RequestVo requestVo) {
        return AjaxResult.success(downloadVideoService.downloadByFileName(fileName, requestVo));
    }

    @RequestMapping(value = "/getBackUrl")
    @ApiOperation(value = "获取回放视频流", httpMethod = "POST", notes = "获取回放视频流")
    public AjaxResult getBackUrl(@RequestBody RequestVo requestVo) {
        requestVo.getPlayBack().setUrlType(1);
        Map map = downloadVideoService.getBackUrl(requestVo);
        String value = (String) map.get("msg");
        if (value.startsWith("rtsp:")) {
            return AjaxResult.success(map);
        }
        return AjaxResult.error();
    }

    @RequestMapping(value = "/getLiveUrl")
    @ApiOperation(value = "获取实时视频流", httpMethod = "POST", notes = "获取实时视频流")
    public AjaxResult getLiveUrl(@RequestBody RequestVo requestVo) throws Exception {
        requestVo.getPlayBack().setUrlType(0);
        Map map = downloadVideoService.getBackUrl(requestVo);
        String value = (String) map.get("msg");
        if (value.startsWith("rtsp:")) {
            return AjaxResult.success(map);
        }
        return AjaxResult.error(objectMapper.writeValueAsString(map));
    }
}