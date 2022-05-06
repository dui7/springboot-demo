package com.fentiaozi.hikvision.nvr.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author wgq
 */
@Data
@ApiModel(value = "条件搜索", description = "条件搜索")
public class PlayBack {
    @ApiModelProperty(value = "开始时间", example = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "摄像机通道", example = "摄像机通道")
    private Integer channelNumber ;

    @ApiModelProperty(value = "获取流类型 0-实时视频流,1-回放视频流", example = "获取流类型 0-实时视频流,1-回放视频流")
    private int urlType;
}
